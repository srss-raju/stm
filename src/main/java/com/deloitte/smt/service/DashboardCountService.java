package com.deloitte.smt.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class DashboardCountService { 

	@PersistenceContext
	private EntityManager entityManager;
	
	private String ownerQuery = "( S.OWNER IS NULL OR S.OWNER IN (?)";
	private String userKeyQuery = " OR A.USER_KEY IN (";
	private String userGroupKeyQuery = " OR A.USER_GROUP_KEY IN (";
	private String signalMainQuery = "SELECT count(*) FROM SM_TOPIC S LEFT JOIN SM_TOPIC_SIGNAL_VALIDATION_ASSIGNEES A ON A.TOPIC_ID = S.ID WHERE ";
	private String signalSubQuery = ") AND S.SIGNAL_STATUS <> 'Completed'";
	private String assessmentMainQuery = "SELECT count(*) FROM SM_ASSESSMENT_PLAN S LEFT JOIN SM_TOPIC_ASSESSMENT_ASSIGNMENT_ASSIGNEES A ON A.ASSESSMENT_ID = S.ID WHERE ";
	private String assessmentSubQuery = ") AND S.ASSESSMENT_PLAN_STATUS <> 'Completed'";
	private String riskMainQuery = "SELECT count(*) FROM SM_RISK_PLAN S LEFT JOIN SM_TOPIC_RISKPLAN_ASSIGNMENT_ASSIGNEES A ON A.RISK_ID = S.ID WHERE ";
	private String riskSubQuery = ") AND S.STATUS <> 'Completed'";
	

	public Long getValidateAndPrioritizeCount(String owner, List<Long> userKeys, List<Long> userGroupKeys) {
		return getCount(owner, userKeys, userGroupKeys, signalMainQuery, signalSubQuery);
	}
	
	public Long getAssessmentCount(String owner, List<Long> userKeys, List<Long> userGroupKeys) {
		return getCount(owner, userKeys, userGroupKeys, assessmentMainQuery, assessmentSubQuery);
	}
	
	public Long getRiskCount(String owner, List<Long> userKeys, List<Long> userGroupKeys) {
		return getCount(owner, userKeys, userGroupKeys, riskMainQuery, riskSubQuery);
	}
	
	@SuppressWarnings("unchecked")
	public Long getCount(String owner, List<Long> userKeys, List<Long> userGroupKeys, String mainQuery, String subQuery) {
		Long count = 0l;
		StringBuilder queryBuilder = new StringBuilder(mainQuery);
		queryBuilder.append(ownerQuery);
		StringBuilder keyBuilder;
		String userKey;
		if(!CollectionUtils.isEmpty(userKeys)){
			queryBuilder.append(userKeyQuery);
			keyBuilder = new StringBuilder();
			for(Long key:userKeys){
				keyBuilder.append(key);
				keyBuilder.append(",");
			}
			userKey = keyBuilder.toString().substring(0, keyBuilder.lastIndexOf(","));
			queryBuilder.append(userKey);
			queryBuilder.append(")");
		}
		StringBuilder groupKeyBuilder;
		String groupKey;
		if(!CollectionUtils.isEmpty(userGroupKeys)){
			queryBuilder.append(userGroupKeyQuery);
			groupKeyBuilder = new StringBuilder();
			for(Long key:userGroupKeys){
				groupKeyBuilder.append(key);
				groupKeyBuilder.append(",");
			}
			groupKey = groupKeyBuilder.toString().substring(0, groupKeyBuilder.lastIndexOf(","));
			queryBuilder.append(groupKey);
			queryBuilder.append(")");
		}
		
		queryBuilder.append(subQuery);
		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		query.setParameter(1, owner);
		List<Object> topicCount = query.getResultList();
		if(topicCount != null){
			Integer size = topicCount.size();
			count = size.longValue();
			
		}
		return count;
	}
	
}
