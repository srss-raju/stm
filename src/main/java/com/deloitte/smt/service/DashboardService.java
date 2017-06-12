package com.deloitte.smt.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.constant.AssessmentPlanStatus;
import com.deloitte.smt.constant.DashboardChartType;
import com.deloitte.smt.constant.ExecutionType;
import com.deloitte.smt.constant.RiskPlanStatus;
import com.deloitte.smt.constant.SignalStatus;
import com.deloitte.smt.dto.AssessmentPlanDTO;
import com.deloitte.smt.dto.DashboardDTO;
import com.deloitte.smt.dto.RiskPlanDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.TopicRepository;

import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.dto.TopicDTO;

@Service
public class DashboardService {

	private static final Logger LOG = Logger.getLogger(DashboardService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TopicRepository topicRepository;

	
	@SuppressWarnings("unchecked")
	public Map<String, List<SmtComplianceDto>> getSmtComplianceDetails() {
		LOG.info("Method Start getSmtComplianceDetails");
		Map<String, List<SmtComplianceDto>> smtComplianceMap = new HashMap<>();
		Query signalQuery = entityManager.createNativeQuery(
				"select case when current_timestamp > due_date then 'LATE' else 'ONTIME' end as STATUS,COUNT(*) from sm_topic where due_date is not null GROUP BY STATUS");
		List<Object[]> signals = signalQuery.getResultList();
		complianceResponse(smtComplianceMap, signals, "Signals");

		Query assessmentQuery = entityManager.createNativeQuery(
				"select case when current_timestamp > assessment_due_date then 'LATE' else 'ONTIME' end as STATUS,COUNT(*) from sm_assessment_plan where assessment_due_date is not null GROUP BY STATUS");
		List<Object[]> assessments = assessmentQuery.getResultList();
		complianceResponse(smtComplianceMap, assessments, "Assessment Plans");

		Query riskQuery = entityManager.createNativeQuery(
				"select case when current_timestamp > risk_due_date then 'LATE' else 'ONTIME' end as RISKSTATUS,COUNT(*) from sm_risk_plan where risk_due_date is not null GROUP BY RISKSTATUS");
		List<Object[]> risks = riskQuery.getResultList();
		complianceResponse(smtComplianceMap, risks, "Risk Plans");

		return smtComplianceMap;
	}

	/**
	 * @param smtComplianceMap
	 * @param authors
	 */
	public void complianceResponse(Map<String, List<SmtComplianceDto>> smtComplianceMap, List<Object[]> results,
			String type) {
		List<SmtComplianceDto> smtComplianceList=new ArrayList();
		//if (!CollectionUtils.isEmpty(authors)) {
			smtComplianceList = new ArrayList<>();
			for (Object[] row : results) {
				SmtComplianceDto dto = new SmtComplianceDto();
				dto.setCount(((BigInteger) row[1]).longValue());
				dto.setStatus((String) row[0]);
				smtComplianceList.add(dto);
			}
			smtComplianceMap.put(type, smtComplianceList);
		//}
		
		Set<String>  addedExecutionTypes=  smtComplianceList.stream().map(SmtComplianceDto:: getStatus).collect(Collectors.toSet());
		
		List<String> unAddedTypes=ExecutionType.getExecutionTypes().stream().filter( type1 -> !addedExecutionTypes.contains(type1)).collect(Collectors.toList());
		
		for (String unAddedType : unAddedTypes) {
			SmtComplianceDto dto = new SmtComplianceDto();
			dto.setCount(0l);
			dto.setStatus(unAddedType);
			smtComplianceList.add(dto);
		}
		
		System.out.println(unAddedTypes);
	}

	
	public DashboardDTO getDashboardData(){
		DashboardDTO dashboardData=new DashboardDTO();
		Map<String, Map<String, Long>> topicMetrics=calculateTopicMetrics(getSignalsDTO());
		dashboardData.getTopicMetrics().add(topicMetrics);
		Map<String, Map<String, Long>> assessmentMetrics=calculateAssessmentMetrics(getAssessmentPlanDTOS());
		dashboardData.getAssessmentMetrics().add(assessmentMetrics);
		Map<String, Map<String, Long>> riskPlanMetrics=calculatRiskMetrics(getRiskPlanDTOS());
		dashboardData.getRiskMetrics().add(riskPlanMetrics);
		return dashboardData;
	}
	
	
	private List<TopicDTO> getSignalsDTO() {
		List<TopicDTO> topics= topicRepository.findByIngredientName();
		return topics;
	}
	
	private Map<String, Map<String, Long>> calculateTopicMetrics(List<TopicDTO> topics){
		Map<String, Map<String, Long>> metrics= topics.stream().collect(
					Collectors.groupingBy(TopicDTO::getIngredientName,
								Collectors.groupingBy(TopicDTO::getSignalStatus,Collectors.counting())
							)
				);
		
		Set<Entry<String, Map<String, Long>>> set= metrics.entrySet();
		for (Entry<String, Map<String, Long>> entry : set) {
			Map<String,Long> ingredientMap=entry.getValue();
			
			for (String status: SignalStatus.getStatusValues()) {
				if(!ingredientMap.containsKey(status)){
					ingredientMap.put(status, 0l);
				}
			}
		}
		return metrics;
		
	}

	private Map<String, Map<String, Long>> calculateAssessmentMetrics(List<AssessmentPlanDTO> topics){
		Map<String, Map<String, Long>> metrics= topics.stream().collect(
					Collectors.groupingBy(AssessmentPlanDTO::getIngredientName,
								Collectors.groupingBy(AssessmentPlanDTO::getAssessmentPlanStatus,Collectors.counting())
							)
				);
		
		
		
		Set<Entry<String, Map<String, Long>>> set= metrics.entrySet();
		for (Entry<String, Map<String, Long>> entry : set) {
			Map<String,Long> ingredientMap=entry.getValue();
			
			for (String status: AssessmentPlanStatus.getStatusValues()) {
				if(!ingredientMap.containsKey(status)){
					ingredientMap.put(status, 0l);
				}
			}
		}
		
		return metrics;
		
	}
	
	
	private Map<String, Map<String, Long>> calculatRiskMetrics(List<RiskPlanDTO> topics){
		Map<String, Map<String, Long>> metrics= topics.stream().collect(
					Collectors.groupingBy(RiskPlanDTO::getIngredientName,
								Collectors.groupingBy(RiskPlanDTO::getRiskPlanStatus,Collectors.counting())
							)
				);
		
		
		Set<Entry<String, Map<String, Long>>> set= metrics.entrySet();
		for (Entry<String, Map<String, Long>> entry : set) {
			Map<String,Long> ingredientMap=entry.getValue();
			
			for (String status: RiskPlanStatus.getStatusValues()) {
				if(!ingredientMap.containsKey(status)){
					ingredientMap.put(status, 0l);
				}
			}
		}
		
		return metrics;
		
	}
	
	
	private List<AssessmentPlanDTO> getAssessmentPlanDTOS(){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssessmentPlanDTO> criteriaQuery = cb.createQuery(AssessmentPlanDTO.class);

		Root<Topic> topic = criteriaQuery.from(Topic.class);
		Root<Ingredient> ingredient = criteriaQuery.from(Ingredient.class);
		Join<Topic, AssessmentPlan> topicAssignmentJoin = topic.join("assessmentPlan", JoinType.INNER);

		List<Predicate> predicates = new ArrayList<Predicate>(10);
		predicates.add(cb.equal(ingredient.get("topicId"), topic.get("id")));

		Predicate andPredicate = cb.and(predicates.toArray(new Predicate[predicates.size()]));
		criteriaQuery
				.select(cb.construct(AssessmentPlanDTO.class, topicAssignmentJoin.get("id"),
						ingredient.get("ingredientName"), topicAssignmentJoin.get("assessmentName"),
						topicAssignmentJoin.get("assessmentPlanStatus")))
				.where(andPredicate).orderBy(cb.desc(ingredient.get("ingredientName")));

		TypedQuery<AssessmentPlanDTO> q = entityManager.createQuery(criteriaQuery);
		return q.getResultList();
	}
	
	private List<RiskPlanDTO> getRiskPlanDTOS(){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<RiskPlanDTO> criteriaQuery2 = cb.createQuery(RiskPlanDTO.class);

		Root<Ingredient> ingredient = criteriaQuery2.from(Ingredient.class);
		Root<Topic> topic2 = criteriaQuery2.from(Topic.class);
		Root<Ingredient> ingredient2 = criteriaQuery2.from(Ingredient.class);
		Join<Topic, AssessmentPlan> topicAssignmentJoin2 = topic2.join("assessmentPlan", JoinType.INNER);
		Join<AssessmentPlan, RiskPlan> assementRiskJoin = topicAssignmentJoin2.join("riskPlan", JoinType.INNER);
		List<Predicate> predicates2 = new ArrayList<Predicate>(10);
		predicates2.add(cb.equal(ingredient2.get("topicId"), topic2.get("id")));

		Predicate andPredicate2 = cb.and(predicates2.toArray(new Predicate[predicates2.size()]));
		criteriaQuery2
				.select(cb.construct(RiskPlanDTO.class, assementRiskJoin.get("id"), ingredient2.get("ingredientName"),
						assementRiskJoin.get("name"), assementRiskJoin.get("status")))
				.where(andPredicate2).orderBy(cb.desc(ingredient.get("ingredientName")));

		TypedQuery<RiskPlanDTO> q2 = entityManager.createQuery(criteriaQuery2);
		return q2.getResultList();
	}



}
