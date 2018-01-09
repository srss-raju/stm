package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.FilterTypes;
import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.FilterDataObject;
import com.deloitte.smt.dto.FilterResponse;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.FilterValues;
import com.deloitte.smt.entity.Filters;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.FilterRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.FiltersService;
import com.deloitte.smt.util.ServerResponseObject;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FiltersServiceImpl<E> implements FiltersService {
	private final Logger logger = LogManager.getLogger(this.getClass());

	private static final String ASSESSMENT = "assessment";
	private static final String RISK = "risk";
	private static final String SIGNAL = "signal";
	private static final String DETECTION = "detection";
	@Autowired
	private FilterRepository filterRepository;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private AssessmentPlanRepository assessmentPlanRepository;
	
	@Autowired
	private ProductFilterServiceImpl productFilterServiceImpl;

	@Autowired
	private ConditionFilterServiceImpl conditionFilterServiceImpl;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<FilterDTO> getFiltersByType(String type) {
		List<FilterDTO> filterList = null;
		try {
			List<String> typeList = Arrays.asList("generic", type);
			logger.info("typeList.." + typeList);
			List<Filters> listfi = filterRepository.findByFilterTypes(typeList);
			if (!CollectionUtils.isEmpty(listfi)) {
				logger.info("RESULT......" + listfi);
				filterList = getAllFiltersTypes(listfi, type);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return filterList;
	}

	private List<FilterDTO> getAllFiltersTypes(List<Filters> listfi, String type) {
		List<FilterDTO> filterList = null;
		try {
			filterList = new ArrayList<>();
			for (Filters filter : listfi) {
				FilterDTO dto = new FilterDTO();
				dto.setFilterKey(filter.getKey());
				dto.setFilterName(filter.getName());
				String name = filter.getKey();
				getFiltersList(filterList, filter, dto, name, type);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return filterList;
	}

	private void getFiltersList(List<FilterDTO> filterList, Filters filter, FilterDTO dto, String key, String type) {
		List<?> data;
		logger.info("KEY...." + key);
		switch (key) {
		case "statuses":
		case "assessmenttaskstatus":
		case "riskplanactionstatus":
		case "frequency":
			filterList.add(getFiltersType(filter));
			break;
		case "products":
			productFilterServiceImpl.productLevelFilter(filterList, type);
			break;
		case "conditions":
			conditionFilterServiceImpl.conditionLevelFilter(filterList, type);
			break;
		case "owners":
			data = topicRepository.findDistinctOwnerNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		/*case "assignees":
			data = topicSignalDetectionAssignmentAssigneesRepository.getDetectionAssignedUsers();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;*/
		case "signalconfirmation":
			data = topicRepository.findDistinctSignalConfirmationNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "signalsource":
			data = topicRepository.getSourceNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "finaldispositions":
			data = assessmentPlanRepository.getAssessmentRiskStatus();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "detectedDates":
		case "dueDates":
		case "createdDates":
		case "lastRunDates":
		case "nextRunDates":
			getEmptyFilterValues(filter, filterList);
			break;

		default:
			break;
		}
	}

	private void getEmptyFilterValues(Filters filter, List<FilterDTO> filterList) {
		logger.info("getEmptyFilterValues----" + filter.getName());
		FilterDTO dto;
		dto = new FilterDTO();
		dto.setFilterKey(filter.getKey());
		dto.setFilterName(filter.getName());
		dto.setFilterValues(Arrays.asList(FilterTypes.EMPTY.type(), FilterTypes.EMPTY.type()));
		dto.setFilterType("date");
		filterList.add(dto);
	}

	private FilterDTO getFiltersType(Filters filter) {
		FilterDTO dto = new FilterDTO();
		dto.setFilterKey(filter.getKey());
		dto.setFilterName(filter.getName());
		logger.info(filter.getType() + "..." + filter.getName());
		List<FilterValues> statusList = filter.getFiltersValues();
		List<String> statuslist = null;
		if (!CollectionUtils.isEmpty(statusList)) {
			statuslist = new ArrayList<>();
			for (FilterValues filtersStatus : statusList) {
				statuslist.add(filtersStatus.getName());
			}
		}
		dto.setFilterValues(statuslist);
		return dto;
	}

	// GET DATA BY FILTER TYPE
	@SuppressWarnings("unchecked")
	@Override
	public ServerResponseObject getSignalDataByFilter(String type, SearchCriteriaDTO searchCriteria) {
		logger.info("SignalDataByFilter....." + searchCriteria);
		ServerResponseObject response = null;
		Map<String, Object> filMap = null;
		StringBuilder queryBuilder = new StringBuilder();
		Map<String,Object> parameterMap = new LinkedHashMap<>();
		try {
			List<FilterDTO> filters = searchCriteria.getFilters();
			if (!CollectionUtils.isEmpty(filters)) {
				filMap = new HashMap<>();
				switch (type) {
				case SIGNAL:
						queryBuilder.append("select distinct root from Topic root, TopicSignalValidationAssignmentAssignees joinAss,TopicSocAssignmentConfiguration condition, TopicProductAssignmentConfiguration product ");
						queryBuilder.append("where  root.id=joinAss.topicId ");
						break;
				case RISK:
					queryBuilder.append("select distinct root from RiskPlan root, AssessmentPlan ass, Topic topic, TopicRiskPlanAssignmentAssignees joinAss,TopicSocAssignmentConfiguration condition, TopicProductAssignmentConfiguration product ");
					queryBuilder.append("where root.id=ass.riskPlan.id and topic.assessmentPlan.id = ass.id and root.id=joinAss.riskId.id ");
					break;
				case ASSESSMENT:
					queryBuilder.append("select distinct root from AssessmentPlan root, Topic topic,TopicAssessmentAssignmentAssignees joinAss,TopicSocAssignmentConfiguration condition, TopicProductAssignmentConfiguration product ");
					queryBuilder.append("where topic.assessmentPlan.id = root.id and root.id = joinAss.assessmentId ");
					break;
				case DETECTION:
					queryBuilder.append("select distinct root from SignalDetection root, TopicSignalDetectionAssignmentAssignees joinAss,TopicSocAssignmentConfiguration condition, TopicProductAssignmentConfiguration product ");
					queryBuilder.append("where  root.id=joinAss.detectionId ");
					break;
				default:
					break;
				}
				for (FilterDTO dto : filters) {
					filMap.put(dto.getFilterKey(), dto.getFilterValues());
				}
				logger.info("filMap.........." + filMap);
				Set<Entry<String, Object>> st = filMap.entrySet();
				for (Entry<String, Object> me : st) {
					String key = me.getKey();
					buildPredicates(queryBuilder, me, key, type,parameterMap);
				}
				logger.info("BUILDING OWNER AND ASSIGNEE PREDICATE------"+queryBuilder);
				// Create OWNER AND ASSIGNEE PREDICATE
				addOwnersAssignees(filMap, queryBuilder,parameterMap,type);
				
				buildProductAndConditionPredicate(filters,queryBuilder,type,parameterMap);

				logger.info("BUILD PREDICATE QUERY-------");
			} else {
				logger.info("FILTER SIZE = 0.....");
				switch (type) {
				case SIGNAL:
					queryBuilder.append("from Topic root");
					break;
				case RISK:
					queryBuilder.append("from RiskPlan root");
					break;
				case ASSESSMENT:
					queryBuilder.append("from AssessmentPlan root");
					break;
				case DETECTION:
					queryBuilder.append("from SignalDetection root");
					break;
				default:
					break;
				}
			}
			queryBuilder.append(" order by root.createdDate desc");
			String queryStr = queryBuilder.toString();
			logger.info("queryStr....."+queryStr);
			FilterResponse smtResponse = new FilterResponse();
			Query query = entityManager.createQuery(queryStr);
			logger.info("parameterMap....."+parameterMap);
			logger.info("query....."+query.toString());
			setParametersMapToQuery(parameterMap,query);
			
			if (!CollectionUtils.isEmpty(query.getResultList())) {
				smtResponse.setTotalRecords(query.getResultList().size());
			}
			if (searchCriteria.getFetchSize() >= 0) {
				query.setFirstResult(searchCriteria.getFromRecord());
				query.setMaxResults(searchCriteria.getFetchSize());
				smtResponse.setFetchSize(searchCriteria.getFetchSize());
				smtResponse.setFromRecord(searchCriteria.getFromRecord());
				List<FilterDataObject> fres = prepareSignalResponse(query.getResultList(), type);
				smtResponse.setResult(fres);
			}
			response = new ServerResponseObject();
			response.setResponse(smtResponse);
			response.setStatus("SUCCESS");
			return response;
		} catch (Exception e) {
			logger.error(e);
		}
		return response;
	}

	
	private void buildProductAndConditionPredicate(List<FilterDTO> filters, StringBuilder queryBuilder, String type, Map<String, Object> parameterMap) {
		Set<String> productSet = new HashSet<>();
		Set<String> conditionSet = new HashSet<>();
		for (FilterDTO filter : filters) {
			if("product".equalsIgnoreCase(filter.getFilterType()))
			{
				productSet = constructObjectToSet(filter.getFilterValues());
			}
			else if("condition".equalsIgnoreCase(filter.getFilterType()))
			{
				conditionSet = constructObjectToSet(filter.getFilterValues());
			}
		}
		
		logger.info("PRODUCT SET >>>>>>>>>>>>"+productSet);
		logger.info("CONDITION SET >>>>>>>>>>>>"+conditionSet);
		if(!CollectionUtils.isEmpty(productSet) && !CollectionUtils.isEmpty(conditionSet))
		{
			productFilterServiceImpl.constructProductPredicate(productSet,queryBuilder,type,parameterMap);
			conditionFilterServiceImpl.constructConditionPredicate(conditionSet,queryBuilder,type,parameterMap);
		}
		else
		{
			if(!CollectionUtils.isEmpty(productSet))
			{
				productFilterServiceImpl.constructProductPredicate(productSet,queryBuilder,type,parameterMap);
			}
			else
			{
				conditionFilterServiceImpl.constructConditionPredicate(conditionSet,queryBuilder,type,parameterMap);
			}
		}
		
		
	}
	
	
	private Set<String> constructObjectToSet(List<?> filterValues) {
		logger.info("filterValues...."+filterValues);
		Set<String> valueSet = null;
		try {
			valueSet = new HashSet<>();
			for (Object object : filterValues) {
				LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) object;
				valueSet.add(map.get("key").toString());
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return valueSet;
	}
	
	
	private void addOwnersAssignees(Map<String, Object> filMap, StringBuilder queryBuilder,
			Map<String, Object> parameterMap, String type) {
		Set<Object> ownerSet = null;
		Set<Object> userSet1 = null;
		Set<Object> groupSet1 = null;
		StringBuilder owner = null;
		StringBuilder user = null;
		StringBuilder group = null;
		try {
			Object ownerMap = filMap.get("owners");
			Object assigneesMap = filMap.get("assignees");
			if (null != ownerMap) {
				ownerSet = prepareFieldValuesSet(ownerMap);
				owner = new StringBuilder();
				owner.append(" root.owner in :owner");
				parameterMap.put("owner", ownerSet);
			}

			if (null != assigneesMap) {
				List<?> assigneeMap = (ArrayList<?>) assigneesMap;
				ObjectMapper oMapper = new ObjectMapper();
				Set<Object> userSet = new HashSet<>();
				Set<Object> groupSet = new HashSet<>();
				for (Object assignObj : assigneeMap) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = oMapper.convertValue(assignObj, Map.class);
					map.forEach((k, v) -> {
						if (k.contains("userKey")) {
							if (!"".equals(v.toString()))
								userSet.add(Long.parseLong(v.toString()));

						} else {
							List<?> l = (ArrayList<?>) v;
							for (Object obj : l) {
								if (!"".equals(obj.toString()))
									groupSet.add(Long.parseLong(obj.toString()));
							}
						}
					});
				}
				userSet1 = userSet;
				groupSet1 = groupSet;
			}
			logger.info("ownerSet........" + ownerSet);
			logger.info("userKey  >>>>>>>>>>>>>>>>>>" + userSet1);
			logger.info("user_group_key >>>>>>>>>>>>>>>>>" + groupSet1);

			if (!CollectionUtils.isEmpty(userSet1)) {
				user = new StringBuilder();
				user.append(" joinAss.userKey in :user");
				parameterMap.put("user", userSet1);
			}
			if (!CollectionUtils.isEmpty(groupSet1)) {
				group = new StringBuilder();
				group.append(" joinAss.userGroupKey in :group");
				parameterMap.put("group", groupSet1);
			}
			buildOwnerUserGroupPredicate(queryBuilder, owner, user, group);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void buildOwnerUserGroupPredicate(StringBuilder queryBuilder, StringBuilder owner, StringBuilder user,
			StringBuilder group) {
		if (owner != null) {
			checkOwnerUserAndGroupExists(queryBuilder, owner, user, group);
		} else {
			checkUserAndGroupExists(queryBuilder, user, group);
		}
	}

	private void checkOwnerUserAndGroupExists(StringBuilder queryBuilder, StringBuilder owner, StringBuilder user,
			StringBuilder group) {
		if (user != null && group != null)
			queryBuilder.append(" and (").append(user).append(" or ").append(group).append(" or ").append(owner).append(")");
		else {
			if (user != null)
				queryBuilder.append(" and (").append(user).append(" or ").append(owner).append(")");
			else if (group != null)
				queryBuilder.append(" and (").append(group).append(" or ").append(owner).append(")");
			else
				queryBuilder.append(" and (").append(owner).append(")");
		}
	}
	private void checkUserAndGroupExists(StringBuilder queryBuilder, StringBuilder user, StringBuilder group) {
		if (user != null && group != null)
			queryBuilder.append(" and (").append(user).append(" or ").append(group).append(")");
		else {
			if (user != null)
			queryBuilder.append(" and (").append(user).append(")");
			else if (group != null)
				queryBuilder.append(" and (").append(group).append(")");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void setParametersMapToQuery(Map<String, Object> parameterMap, Query query) {
		Set<Entry<String, Object>> st = parameterMap.entrySet();
		for (Entry<String, Object> me : st) {
			/*if("greaterDate".equalsIgnoreCase(me.getKey()) || "lessDate".equalsIgnoreCase(me.getKey()))
			{
				Calendar cal = Calendar.getInstance(); 
				cal.setTime((Date) me.getValue()); 
				logger.info("CALENDER....."+cal);
				query.setParameter(me.getKey(), cal,TemporalType.TIMESTAMP);
			}
			*/
			query.setParameter(me.getKey(), me.getValue());
		}

	}

	private void buildPredicates(StringBuilder queryBuilder, Entry<String, Object> me, String key, String type, Map<String, Object> parameterMap) {
		logger.info("key...."+key);
		switch (key) {
		case "statuses":
			addStatuses(me.getValue(), queryBuilder, type,parameterMap);
			break;
		case "signalsource":
			addSourceNames(me.getValue(), queryBuilder, parameterMap);
			break;
		case "signalconfirmation":
			addSignalConfirmations(me.getValue(), queryBuilder, parameterMap);
			break;
		case "dueDates":
			addDueDate(me.getValue(), queryBuilder, type,parameterMap);
			break;
		case "createdDates":
		case "detectedDates":
			addCreatedDate(me.getValue(), queryBuilder, type,parameterMap);
			break;
		case "assessmenttaskstatus":
			addAssessmentTaskStatus(me.getValue(), queryBuilder, parameterMap);
			break;
		case "finaldispositions":
			addFinalDispositions(me.getValue(), queryBuilder, parameterMap);
			break;
		case "riskplanactionstatus":
			addRiskPlanActionStatus(me.getValue(), queryBuilder, parameterMap);
			break;
		case "frequency":
			addFrequency(me.getValue(), queryBuilder, parameterMap);
			break;
		case "lastRunDates":
			addRunDates(me.getValue(), queryBuilder,parameterMap, "lastRunDate");
			break;
		case "nextRunDates":
			addRunDates(me.getValue(), queryBuilder,parameterMap,"nextRunDate");
			break;
		default:
			break;
		}
	}

	private void addCreatedDate(Object value, StringBuilder queryBuilder, String type,Map<String, Object> parameterMap) {
		Set<Object> emptyDates = prepareFieldValuesSet(value);
		String dateType ="root.createdDate";
		/*switch (type) {
		case RISK:
			dateType = "root.createdDate";
			break;
		case ASSESSMENT:
			dateType = "a.createdDate";
			break;
		case SIGNAL:
			dateType = "t.createdDate";
			break;	
		default:
			dateType = "d.createdDate";
			break;
		}*/
		if (!"".equalsIgnoreCase(emptyDates.iterator().next().toString())) {
			getDatePredicates(value, queryBuilder, parameterMap, dateType);
		}
	}

	private void addDueDate(Object value, StringBuilder queryBuilder, String type, Map<String, Object> parameterMap) {
		Set<Object> emptyDates = prepareFieldValuesSet(value);
		String dateType = null;
		switch (type) {
		case RISK:
			dateType = "root.riskDueDate";
			break;
		case ASSESSMENT:
			dateType = "root.assessmentDueDate";
			break;
		case SIGNAL:
			dateType = "root.dueDate";
			break;
		default:
			break;
		}
		if (!"".equalsIgnoreCase(emptyDates.iterator().next().toString())) {
			getDatePredicates(value, queryBuilder, parameterMap, dateType);
		}

	}

	protected void getDatePredicates(Object value, StringBuilder queryBuilder, Map<String, Object> parameterMap, String dateType)  {
		List<?> l = (ArrayList<?>) value;
			for (int i = 0; i < l.size(); i++) {
				Object obj = l.get(i);
				if (i == 0 && !"".equalsIgnoreCase(obj.toString())) {
					Date d1  = SignalUtil.convertStringToDate(obj.toString());
					queryBuilder.append(" and ");
					queryBuilder.append("TO_DATE(TO_CHAR("+dateType+", 'yyyy-MM-dd'), 'yyyy-MM-dd')");
					queryBuilder.append(">= :greaterDate");
					parameterMap.put("greaterDate", d1);
				}
				if (i == 1 && !"".equalsIgnoreCase(obj.toString())) {
					Date d2  = SignalUtil.convertStringToDate(obj.toString());
					queryBuilder.append(" and ");
					queryBuilder.append("TO_DATE(TO_CHAR("+dateType+", 'yyyy-MM-dd'), 'yyyy-MM-dd')");
					queryBuilder.append("<= :lessDate");
					parameterMap.put("lessDate", d2);
				}
			}
	}
	
	private List<FilterDataObject> prepareSignalResponse(List<E> resultList, String type) {
		List<FilterDataObject> fres = null;
		switch (type) {
		case SIGNAL:
			fres = constructSignalObj(resultList);
			break;
		case RISK:
			fres = constructRiskObj(resultList);
			break;
		case ASSESSMENT:
			fres = constructAssessmentObj(resultList);
			break;
		case DETECTION:
			fres = constructDetectionObj(resultList);

			break;
		default:
			break;
		}
		return fres;
	}

	@SuppressWarnings("unchecked")
	private List<FilterDataObject> constructDetectionObj(List<E> resultList) {
		List<FilterDataObject> fres;
		fres = new ArrayList<>();
		List<SignalDetection> detections = (List<SignalDetection>) resultList;
		for (SignalDetection detection : detections) {
			FilterDataObject res = new FilterDataObject();
			res.setId(detection.getId());
			res.setName(detection.getName());
			res.setRunFrequency(detection.getRunFrequency());
			res.setLastRunDate(detection.getLastRunDate());
			res.setNextRunDate(detection.getNextRunDate());
			res.setDenominatorForPoission(detection.getDenominatorForPoission());
			res.setSignalDetected(detection.getSignalDetected());
			res.setDescription(detection.getDescription());
			fres.add(res);
		}
		return fres;
	}

	@SuppressWarnings("unchecked")
	private List<FilterDataObject> constructAssessmentObj(List<E> resultList) {
		List<FilterDataObject> fres;
		fres = new ArrayList<>();
		List<AssessmentPlan> assessments = (List<AssessmentPlan>) resultList;
		for (AssessmentPlan assessment : assessments) {
			FilterDataObject res = new FilterDataObject();
			res.setId(assessment.getId());
			res.setAssessmentName(assessment.getAssessmentName());
			res.setAssessmentPlanStatus(assessment.getAssessmentPlanStatus());
			res.setAssessmentDueDate(assessment.getAssessmentDueDate());
			res.setCreatedDate(assessment.getCreatedDate());
			res.setPriority(assessment.getPriority());
			fres.add(res);
		}
		return fres;
	}

	@SuppressWarnings("unchecked")
	private List<FilterDataObject> constructRiskObj(List<E> resultList) {
		List<FilterDataObject> fres;
		fres = new ArrayList<>();
		List<RiskPlan> risks = (List<RiskPlan>) resultList;
		for (RiskPlan risk : risks) {
			FilterDataObject res = new FilterDataObject();
			res.setId(risk.getId());
			res.setName(risk.getName());
			res.setStatus(risk.getStatus());
			res.setCreatedDate(risk.getCreatedDate());
			fres.add(res);
		}
		return fres;
	}

	@SuppressWarnings("unchecked")
	private List<FilterDataObject> constructSignalObj(List<E> resultList) {
		List<FilterDataObject> fres;
		fres = new ArrayList<>();
		List<Topic> q1 = (List<Topic>) resultList;
		for (Topic topic : q1) {
			FilterDataObject res = new FilterDataObject();
			res.setId(topic.getId());
			res.setName(topic.getName());
			res.setStatus(topic.getSignalStatus());
			res.setDescription(topic.getDescription());
			res.setSignalConfirmation(topic.getSignalConfirmation());
			res.setSourceName(topic.getSourceName());
			res.setCreatedDate(topic.getCreatedDate());
			fres.add(res);
		}
		return fres;
	}

	private void addRunDates(Object value, StringBuilder queryBuilder, Map<String, Object> parameterMap,
			String dateType) {
		Set<Object> emptyDates = prepareFieldValuesSet(value);
		if (!"".equalsIgnoreCase(emptyDates.iterator().next().toString())) {
			getDatePredicates(value, queryBuilder, parameterMap, "root."+dateType);
		}
	}
	
	private void addFrequency(Object statusValue, StringBuilder queryBuilder, Map<String, Object> parameterMap) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			queryBuilder.append(" and root.runFrequency in :frequency");
			parameterMap.put("frequency", list);
		}
	}
	private void addRiskPlanActionStatus(Object statusValue, StringBuilder queryBuilder, Map<String, Object> parameterMap) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			queryBuilder.append(" and root.riskTaskStatus in :riskplanactionstatus");
			parameterMap.put("riskplanactionstatus", list);
		}
	}
	
	
	private void addFinalDispositions(Object statusValue, StringBuilder queryBuilder, Map<String, Object> parameterMap) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			queryBuilder.append(" and root.assessmentRiskStatus in :finaldispositions");
			parameterMap.put("finaldispositions", list);
		}

	}
	
	private void addAssessmentTaskStatus(Object statusValue, StringBuilder queryBuilder, Map<String, Object> parameterMap) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			queryBuilder.append(" and root.assessmentTaskStatus in :assessmenttaskstatus");
			parameterMap.put("assessmenttaskstatus", list);
		}

	}
	private void addSignalConfirmations(Object statusValue, StringBuilder queryBuilder, Map<String, Object> parameterMap) {
		Set<Object> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			queryBuilder.append(" and root.signalConfirmation in :signalconfirmation");
			parameterMap.put("signalconfirmation", statusList);
		}

	}
	private void addSourceNames(Object statusValue, StringBuilder queryBuilder, Map<String, Object> parameterMap) {
		Set<Object> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			queryBuilder.append(" and root.sourceName in :signalsource");
			parameterMap.put("signalsource", statusList);
		}
	}
	private void addStatuses(Object statusValue, StringBuilder queryBuilder,  String type, Map<String, Object> parameterMap) {
		Set<Object> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			switch (type) {
			case RISK:
				queryBuilder.append(" and root.status in :statuses");
				break;
			case ASSESSMENT:
				queryBuilder.append(" and root.assessmentPlanStatus in :statuses");
				break;
			default:
				queryBuilder.append(" and root.signalStatus in :statuses");
				break;
			}
			parameterMap.put("statuses", statusList);
		}

	}
	/*
	private void addOwnersAssignees(Map<String, Object> filMap, CriteriaBuilder criteriaBuilder,
			Join<E, E> joinAssignees, List<Predicate> predicates, Root<E> root) {
		Set<String> ownerSet = null;
		Set<String> userSet1 = null;
		Set<String> groupSet1 = null;
		Predicate owner = null;
		Predicate user = null;
		Predicate group = null;
		try {
			Object ownerMap = filMap.get("owners");
			Object assigneesMap = filMap.get("assignees");
			if (null != ownerMap) {
				ownerSet = prepareFieldValuesSet(ownerMap);
				owner = criteriaBuilder.isTrue(root.get(SmtConstant.OWNER.getDescription()).in(ownerSet));
			}

			if (null != assigneesMap) {
				List<?> assigneeMap = (ArrayList<?>) assigneesMap;
				ObjectMapper oMapper = new ObjectMapper();
				Set<String> userSet = new HashSet<>();
				Set<String> groupSet = new HashSet<>();
				for (Object assignObj : assigneeMap) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = oMapper.convertValue(assignObj, Map.class);
					map.forEach((k, v) -> {
						if (k.contains("userKey")) {
							if (!"".equals(v.toString()))
								userSet.add(v.toString());

						} else {
							List<?> l = (ArrayList<?>) v;
							for (Object obj : l) {
								if (!"".equals(obj.toString()))
									groupSet.add(obj.toString());
							}
						}
					});
				}
				userSet1 = userSet;
				groupSet1 = groupSet;
			}

			logger.info("ownerSet........" + ownerSet);
			logger.info("userKey  >>>>>>>>>>>>>>>>>>" + userSet1);
			logger.info("user_group_key >>>>>>>>>>>>>>>>>" + groupSet1);

			if (!CollectionUtils.isEmpty(userSet1)) {
				user = criteriaBuilder.isTrue(joinAssignees.get(SmtConstant.USER_KEY.getDescription()).in(userSet1));
			}
			if (!CollectionUtils.isEmpty(groupSet1)) {
				group = criteriaBuilder
						.isTrue(joinAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(groupSet1));
			}

			buildOwnerUserGroupPredicate(criteriaBuilder, predicates, owner, user, group);

		} catch (Exception e) {
			logger.error(e);
		}

	}

	private void buildOwnerUserGroupPredicate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
			Predicate owner, Predicate user, Predicate group) {
		if (owner != null) {
			checkOwnerUserAndGroupExists(criteriaBuilder, predicates, owner, user, group);
		} else {
			checkUserAndGroupExists(criteriaBuilder, predicates, user, group);
		}
	}

	private void checkOwnerUserAndGroupExists(CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
			Predicate owner, Predicate user, Predicate group) {
		if (user != null && group != null)
			predicates.add(criteriaBuilder.or(user, group, owner));
		else {
			if (user != null)
				predicates.add(criteriaBuilder.or(user, owner));
			else if (group != null)
				predicates.add(criteriaBuilder.or(group, owner));
			else
				predicates.add(criteriaBuilder.or(owner));
		}
	}

	private void checkUserAndGroupExists(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Predicate user,
			Predicate group) {
		if (user != null && group != null)
			predicates.add(criteriaBuilder.or(user, group));
		else {
			if (user != null)
				predicates.add(criteriaBuilder.or(user));
			else if (group != null)
				predicates.add(criteriaBuilder.or(group));
		}
	}*/

	private Set<Object> prepareFieldValuesSet(Object ownerMap) {
		Set<Object> ownersSet = new HashSet<>();
		List<?> l = (ArrayList<?>) ownerMap;
		for (Object obj : l) {
			if (!"".equals(obj.toString()))
				ownersSet.add(obj);
		}
		return ownersSet;
	}

}
