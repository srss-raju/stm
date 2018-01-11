package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.TopicRepository;

/**
 * Created by RKB on 23-05-2017.
 */
@Service
public class SignalMatchService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private TopicRepository topicRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
    MeetingService meetingService;
	
	@Autowired
    MeetingRepository meetingRepository;

	/**
	 * @param topic
	 */
	public Topic findMatchingSignal(Topic topic) {
		logger.info("Algorith started");
		List<Topic> matchingSignals = getMatchingSignals(topic);
		List<Topic> ciSignals = checkConfidenceIndex(matchingSignals, topic);
		return checkCohortPercentage(ciSignals, topic);
	}

	@SuppressWarnings("unchecked")
	public List<Topic> getMatchingSignals(Topic topic) {
		StringBuilder socBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		boolean noSocFlag = false;
		boolean noProductFlag = false;
		
		StringBuilder queryBuilder = new StringBuilder(
				"select DISTINCT a.* from sm_topic a LEFT JOIN sm_topic_product_assignment_configuration p ON a.id = p.topic_id LEFT JOIN sm_topic_soc_assignment_configuration c ON a.id = c.topic_id  where a.created_date < ? ");

		if(!CollectionUtils.isEmpty(topic.getConditions())){
			for(TopicCondition socConfig : topic.getConditions()){
				socBuilder.append("'").append(socConfig.getRecordKey()).append("'");
			}
			queryBuilder.append(" and c.record_key IN (");
			queryBuilder.append(socBuilder.toString());
			queryBuilder.append(")");
			noSocFlag = true;
		}
		if(!CollectionUtils.isEmpty(topic.getProducts())){
			for(TopicProduct productConfig : topic.getProducts()){
				productBuilder.append("'").append(productConfig.getRecordKey()).append("'");
			}
			queryBuilder.append(" and p.record_key IN (");
			queryBuilder.append(productBuilder.toString());
			queryBuilder.append(")");
			noProductFlag = true;
		}
		if(!noSocFlag){
			queryBuilder.append(" and c.record_key is null");
		}
		if(!noProductFlag){
			queryBuilder.append(" and p.record_key is null");
		}
		if(topic.getSourceName() != null){
			queryBuilder.append(" and a.source_name = '");
			queryBuilder.append(topic.getSourceName());
			queryBuilder.append("'");
		}
		queryBuilder.append(" order by a.created_date desc");
		Query q = entityManager.createNativeQuery(queryBuilder.toString(), Topic.class);
		q.setParameter(1, topic.getCreatedDate());
		return q.getResultList();
	}

	/**
	 * @param ptBuilder
	 * @param tempPt
	 * @param pts
	 * @return
	 */
	public String buildPts(StringBuilder ptBuilder, List<Pt> pts) {
		String tempPt = null;
		if (!CollectionUtils.isEmpty(pts)) {
			for (Pt pt : pts) {
					ptBuilder.append('\'');
					ptBuilder.append(pt.getPtName().replaceAll("'", "''"));
					ptBuilder.append('\'');
					ptBuilder.append(",");
			}
			tempPt=ptBuilder.toString().substring(0, ptBuilder.lastIndexOf(","));
		}
		return tempPt;
	}

	public List<Topic> checkConfidenceIndex(List<Topic> signals, Topic createdTopic) {
		List<Topic> ciSignals = new ArrayList<>();
		if (!CollectionUtils.isEmpty(signals) && createdTopic!=null) {
			for (Topic matchingSignal : signals) {
				if (createdTopic.getConfidenceIndex() >= matchingSignal.getConfidenceIndex()) {
					ciSignals.add(matchingSignal);
				}
			}
		}
		return ciSignals;
	}

	public Topic checkCohortPercentage(List<Topic> ciSignals, Topic createdTopic) {
		List<Topic> cohort75PercentageSignals = new ArrayList<>();
		List<Topic> cohort75To95PercentageSignals = new ArrayList<>();
		List<Topic> cohort95PercentageSignals = new ArrayList<>();
		List<AssessmentPlan> matchingAssessments = new ArrayList<>();

		getCohorPercentages(ciSignals, cohort75PercentageSignals,
				cohort75To95PercentageSignals, cohort95PercentageSignals,
				matchingAssessments);

		if (!CollectionUtils.isEmpty(cohort75PercentageSignals)) {
			if (!CollectionUtils.isEmpty(cohort75To95PercentageSignals)) {
				// 75 and 7595 signals
				createdTopic.setAssessmentPlans(matchingAssessments);
				if (!CollectionUtils.isEmpty(cohort95PercentageSignals)) {
					// 75, 7595 and 95 signals
					applyMatchingSignalDetails(createdTopic, cohort95PercentageSignals);
					createdTopic.setAssessmentPlans(null);
				}
			} else if (!CollectionUtils.isEmpty(cohort95PercentageSignals)) {
				// 75 and 95 signals
				applyMatchingSignalDetails(createdTopic, cohort95PercentageSignals);
			} else {
				// only 75 Signals - Do Nothing
			}
		} else if (!CollectionUtils.isEmpty(cohort75To95PercentageSignals)) {
			if (!CollectionUtils.isEmpty(cohort95PercentageSignals)) {
				// 7595 and 95 signals
				applyMatchingSignalDetails(createdTopic, cohort95PercentageSignals);
			} else {
				// 7595 signals
				createdTopic.setAssessmentPlans(matchingAssessments);
			}

		} else if (!CollectionUtils.isEmpty(cohort95PercentageSignals)) {
			applyMatchingSignalDetails(createdTopic, cohort95PercentageSignals);
		}

		return createdTopic;
	}

	/**
	 * @param ciSignals
	 * @param cohort75PercentageSignals
	 * @param cohort75To95PercentageSignals
	 * @param cohort95PercentageSignals
	 * @param matchingAssessments
	 */
	private void getCohorPercentages(List<Topic> ciSignals,
			List<Topic> cohort75PercentageSignals,
			List<Topic> cohort75To95PercentageSignals,
			List<Topic> cohort95PercentageSignals,
			List<AssessmentPlan> matchingAssessments) {
		if (!CollectionUtils.isEmpty(ciSignals)) {
			for (Topic ciSignal : ciSignals) {
				calculateCohortPercentages(cohort75PercentageSignals,
						cohort75To95PercentageSignals,
						cohort95PercentageSignals, matchingAssessments,
						ciSignal);
			}
		}
	}

	/**
	 * @param cohort75PercentageSignals
	 * @param cohort75To95PercentageSignals
	 * @param cohort95PercentageSignals
	 * @param matchingAssessments
	 * @param ciSignal
	 */
	private void calculateCohortPercentages(
			List<Topic> cohort75PercentageSignals,
			List<Topic> cohort75To95PercentageSignals,
			List<Topic> cohort95PercentageSignals,
			List<AssessmentPlan> matchingAssessments, Topic ciSignal) {
		if (ciSignal.getCohortPercentage() < 75) {
			cohort75PercentageSignals.add(ciSignal);
		} else if (ciSignal.getCohortPercentage() >= 95) {
			cohort95PercentageSignals.add(ciSignal);
		} else {
			cohort75To95PercentageSignals.add(ciSignal);
			if (ciSignal.getAssessmentPlan() != null) {
				ciSignal.getAssessmentPlan().setCohortPercentage(ciSignal.getCohortPercentage());
				matchingAssessments.add(ciSignal.getAssessmentPlan());
			}
		}
	}

	/**
	 * @param createdTopic
	 * @param cohort95PercentageSignals
	 */
	private void applyMatchingSignalDetails(Topic createdTopic, List<Topic> cohort95PercentageSignals) {
		// 95 signals
		Topic prevTopic = null;
		Topic final95Signal = null;
		for (Topic cohort95PercentageSignal : cohort95PercentageSignals) {

			if (prevTopic != null) {
				if (final95Signal.getCohortPercentage() < cohort95PercentageSignal.getCohortPercentage()) {
					final95Signal = cohort95PercentageSignal;
					prevTopic = cohort95PercentageSignal;
				} else {
					prevTopic = cohort95PercentageSignal;
				}
			} else {
				prevTopic = cohort95PercentageSignal;
				final95Signal = cohort95PercentageSignal;
			}

		}
		
		final95Signal = getLatestMatchingSignal(cohort95PercentageSignals, final95Signal);
		if(final95Signal != null){
			createdTopic.setValidationComments(final95Signal.getValidationComments());
			createdTopic.setSignalStrength(final95Signal.getSignalStrength());
			createdTopic.setSignalConfirmation(final95Signal.getSignalConfirmation());
			createdTopic.setSignalValidation(final95Signal.getSignalValidation());
			createdTopic.setAssessmentPlan(final95Signal.getAssessmentPlan());
			createdTopic.setSignalStatus(final95Signal.getSignalStatus());
			createdTopic.setCohortPercentage(final95Signal.getCohortPercentage());
			createdTopic.setCasesCount(final95Signal.getCasesCount());
			createdTopic.setCaselistId(final95Signal.getCaselistId());
			createdTopic.setDueDate(final95Signal.getDueDate());
			createdTopic.setSourceName(final95Signal.getSourceName());
			createdTopic.setSourceUrl(final95Signal.getSourceUrl());
			createdTopic.setStartDate(final95Signal.getStartDate());
			createdTopic.setEndDate(final95Signal.getEndDate());
			topicRepository.save(createdTopic);
			applyMatchingSignalAttachments(createdTopic, final95Signal);
			applyMatchingSignalMeetings(final95Signal);
		}
	}

	/**
	 * @param createdTopic
	 * @param final95Signal
	 * @return
	 */
	public List<Attachment> applyMatchingSignalAttachments(Topic createdTopic,
			Topic final95Signal) {
		List<Attachment> matchingTopicAttachments = final95Signal.getAttachments();
		if (!CollectionUtils.isEmpty(matchingTopicAttachments)) {
			for (Attachment attachment : matchingTopicAttachments) {
				attachment.setAttachmentResourceId(createdTopic.getId());
			}
		}
		return matchingTopicAttachments;
	}

	/**
	 * @param final95Signal
	 */
	public void applyMatchingSignalMeetings(Topic final95Signal) {
		List<Meeting> meetings = meetingService.findAllyByResourceIdAndMeetingType(final95Signal.getId(), MeetingType.getByDescription("Signal Meeting"));
		if(!CollectionUtils.isEmpty(meetings)){
			List<Meeting> matchingMeetings = new ArrayList<>();
			for(Meeting meeting:meetings){
				Meeting matchingMeeting = new Meeting();
				matchingMeeting.setName(meeting.getName());
				matchingMeeting.setDescription(meeting.getDescription());
				matchingMeeting.setMeetingResourceId(final95Signal.getId());
				matchingMeeting.setCreatedDate(new Date());
				matchingMeeting.setMeetingType(MeetingType.getByDescription("Signal Meeting"));
				matchingMeetings.add(matchingMeeting);
			}
			meetingRepository.save(matchingMeetings);
		}
	}

	/**
	 * @param cohort95PercentageSignals
	 * @param final95Signal
	 * @return
	 */
	/**
	 * @param cohort95PercentageSignals
	 * @param final95Signal
	 * @return
	 */
	private Topic getLatestMatchingSignal(List<Topic> cohort95PercentageSignals, Topic final95Signal) {
		Topic result = null;
		if(cohort95PercentageSignals.size() > 1){
			for(Topic cohort95PercentageSignal:cohort95PercentageSignals){
				if(cohort95PercentageSignal.getCohortPercentage() == final95Signal.getCohortPercentage()){
					result = getResult(final95Signal, cohort95PercentageSignal);
				}
			}
		}
		return result;
	}

	/**
	 * @param final95Signal
	 * @param cohort95PercentageSignal
	 * @return
	 */
	private Topic getResult(Topic final95Signal, Topic cohort95PercentageSignal) {
		Topic result;
		if(final95Signal.getCreatedDate().compareTo(cohort95PercentageSignal.getCreatedDate()) < 0){
			result = cohort95PercentageSignal;
		}else{
			result = final95Signal;
		}
		return result;
	}

}
