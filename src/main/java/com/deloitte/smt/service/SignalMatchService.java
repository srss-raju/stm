package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TopicRepository;

/**
 * Created by RKB on 23-05-2017.
 */
@Service
public class SignalMatchService {

	private static final Logger LOG = Logger.getLogger(SignalService.class);

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	SignalURLRepository signalURLRepository;

	@Autowired
	AttachmentService attachmentService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	AttachmentRepository attachmentRepository;
	
	@Autowired
    MeetingService meetingService;
	
	@Autowired
    MeetingRepository meetingRepository;

	/**
	 * @param topic
	 */
	public Topic findMatchingSignal(Topic topic) {
		LOG.info("Algorith started");
		List<Topic> matchingSignals = getMatchingSignals(topic);
		List<Topic> ciSignals = checkConfidenceIndex(matchingSignals, topic);
		Topic topicUpdated = checkCohortPercentage(ciSignals, topic);
		return topicUpdated;
	}

	private List<Topic> getMatchingSignals(Topic topic) {
		StringBuilder ptBuilder = new StringBuilder();
		String tempPt = null;
		
		List<Soc> socs = topic.getSocs();
		if (!CollectionUtils.isEmpty(socs)) {
			List<Pt> pts;
			for (Soc soc : socs) {
				pts = soc.getPts();
				if (!CollectionUtils.isEmpty(pts)) {
					for (Pt pt : pts) {
							ptBuilder.append('\'');
							ptBuilder.append(pt.getPtName().replaceAll("'", "''"));
							ptBuilder.append('\'');
							ptBuilder.append(",");
					}
					tempPt=ptBuilder.toString().substring(0, ptBuilder.lastIndexOf(","));
				}
			}
		}
		StringBuilder queryBuilder = new StringBuilder(
				"select distinct signal.* from sm_topic signal INNER JOIN sm_ingredient ing ON  (signal.id = ing.topic_id) LEFT OUTER JOIN  sm_pt pt ON (signal.id = pt.topic_id )  where signal.created_date < ?  and ing.ingredient_name=? ");

		if (topic.getSourceName() != null) {
			queryBuilder.append(" and source_name =\'");
			queryBuilder.append(topic.getSourceName());
			queryBuilder.append("\'");
		}
		if (null!=tempPt) {
			queryBuilder.append(" and pt.pt_name IN (");
			queryBuilder.append(tempPt);
			queryBuilder.append(")");
		}
		queryBuilder.append(" order by signal.created_date desc");
		Query q = entityManager.createNativeQuery(queryBuilder.toString(), Topic.class);
		q.setParameter(1, topic.getCreatedDate());
		q.setParameter(2, topic.getIngredient().getIngredientName());
		return q.getResultList();
	}

	public List<Topic> checkConfidenceIndex(List<Topic> signals, Topic createdTopic) {
		List<Topic> ciSignals = new ArrayList<>();
		if (!CollectionUtils.isEmpty(signals)) {
			for (Topic matchingSignal : signals) {
				if (createdTopic.getConfidenceIndex() > matchingSignal.getConfidenceIndex()) {
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

		if (!CollectionUtils.isEmpty(ciSignals)) {
			for (Topic ciSignal : ciSignals) {
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
		}

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
	 * @param createdTopic
	 * @param cohort95PercentageSignals
	 */
	private void applyMatchingSignalDetails(Topic createdTopic, List<Topic> cohort95PercentageSignals) {
		// 95 signals
		Topic prevTopic = null;
		Topic final95Signal = null;
		for (Topic cohort95PercentageSignal : cohort95PercentageSignals) {

			if (prevTopic != null) {
				if (final95Signal.getCohortPercentage() < prevTopic.getCohortPercentage()) {
					final95Signal = cohort95PercentageSignal;
					prevTopic = cohort95PercentageSignal;
				} else {
					prevTopic = cohort95PercentageSignal;
				}
			} else {
				System.out.println("Cohort P ->> "+cohort95PercentageSignal.getCohortPercentage());
				prevTopic = cohort95PercentageSignal;
				final95Signal = cohort95PercentageSignal;
			}

		}
		
		final95Signal = getLatestMatchingSignal(cohort95PercentageSignals, final95Signal);

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
		createdTopic.setAssignTo(final95Signal.getAssignTo());
		createdTopic.setStartDate(final95Signal.getStartDate());
		createdTopic.setEndDate(final95Signal.getEndDate());
		topicRepository.save(createdTopic);
		List<Attachment> matchingTopicAttachments = attachmentService
				.findByResourceIdAndAttachmentType(final95Signal.getId(), AttachmentType.TOPIC_ATTACHMENT);
		if (!CollectionUtils.isEmpty(matchingTopicAttachments)) {
			for (Attachment attachment : matchingTopicAttachments) {
				attachment.setAttachmentResourceId(createdTopic.getId());
			}
		}
		attachmentRepository.save(matchingTopicAttachments);
		List<SignalURL> matchingTopicSignalUrls = signalURLRepository.findByTopicId(final95Signal.getId());
		if (!CollectionUtils.isEmpty(matchingTopicSignalUrls)) {
			for (SignalURL url : matchingTopicSignalUrls) {
				url.setTopicId(createdTopic.getId());
			}
		}
		signalURLRepository.save(matchingTopicSignalUrls);
		
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
	private Topic getLatestMatchingSignal(
			List<Topic> cohort95PercentageSignals, Topic final95Signal) {
		if(cohort95PercentageSignals.size() > 1){
			for(Topic cohort95PercentageSignal:cohort95PercentageSignals){
				if(cohort95PercentageSignal.getCohortPercentage() == final95Signal.getCohortPercentage()){
					if(final95Signal.getCreatedDate().compareTo(cohort95PercentageSignal.getCreatedDate()) < 0){
						final95Signal = cohort95PercentageSignal;
					}
				}
			}
		}
		return final95Signal;
	}

}
