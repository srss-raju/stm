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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.FilterTypes;
import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.FilterDataObject;
import com.deloitte.smt.dto.FilterResponse;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Filters;
import com.deloitte.smt.entity.FiltersStatus;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.FilterRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicAssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalDetectionAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.service.FiltersService;
import com.deloitte.smt.util.ServerResponseObject;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FiltersServiceImpl<E> implements FiltersService {
	private static final Logger LOGGER = Logger.getLogger(FiltersServiceImpl.class);

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
	private TopicSignalDetectionAssignmentAssigneesRepository topicSignalDetectionAssignmentAssigneesRepository;

	@Autowired
	private ProductFilterServiceImpl productFilterServiceImpl;

	@Autowired
	private ConditionFilterServiceImpl conditionFilterServiceImpl;

	@Autowired
	private TopicSignalValidationAssignmentAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
	@Autowired
	private TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
	@Autowired
	private TopicAssessmentAssignmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
	@Autowired
	private RiskPlanRepository riskPlanRepository;
	@Autowired
	private SignalDetectionRepository signalDetectionRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<FilterDTO> getFiltersByType(String type) {
		List<FilterDTO> filterList = null;
		try {
			List<String> typeList = Arrays.asList("generic", type);
			LOGGER.info("typeList.." + typeList);
			List<Filters> listfi = filterRepository.findByFilterTypes(typeList);
			if (!CollectionUtils.isEmpty(listfi)) {
				LOGGER.info("RESULT......" + listfi);
				filterList = getAllFiltersTypes(listfi, type);
			}
		} catch (Exception e) {
			LOGGER.error(e);
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
			LOGGER.error(e);
		}
		return filterList;
	}

	private void getFiltersList(List<FilterDTO> filterList, Filters filter, FilterDTO dto, String key, String type) {
		List<?> data = null;
		LOGGER.info("KEY...." + key);
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
			switch (type) {
			case SIGNAL:
				data = topicRepository.findDistinctOwnerNames();
				break;
			case RISK:
				data = riskPlanRepository.findOwnersOnRiskPlan();
				break;
			case ASSESSMENT:
				data = assessmentPlanRepository.findOwnersOnAssessmentPlan();
				break;
			case DETECTION:
				data = signalDetectionRepository.findDistinctOwnerOnDetection();
				break;
			default:
				break;
			}
			
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "assignees":
			switch (type) {
			case SIGNAL:
				data = topicSignalValidationAssignmentAssigneesRepository.getSignalAssignedUsers();
				break;
			case RISK:
				data = topicRiskPlanAssignmentAssigneesRepository.getRiskAssignedUsers();
				break;
			case ASSESSMENT:
				data = topicAssessmentAssignmentAssigneesRepository.getAssessmentAssignedUsers();
				break;
			case DETECTION:
				data = topicSignalDetectionAssignmentAssigneesRepository.getDetectionAssignedUsers();
				break;
			default:
				break;
			}
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
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
		LOGGER.info("getEmptyFilterValues----" + filter.getName());
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
		LOGGER.info(filter.getType() + "..." + filter.getName());
		List<FiltersStatus> statusList = filter.getFiltersStatus();
		List<String> statuslist = null;
		if (!CollectionUtils.isEmpty(statusList)) {
			statuslist = new ArrayList<>();
			for (FiltersStatus filtersStatus : statusList) {
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
		LOGGER.info("SignalDataByFilter....." + searchCriteria);
		ServerResponseObject response = null;
		Map<String, Object> filMap = null;
		Map<String, Object> filTypeMap = null;
		StringBuilder queryBuilder = new StringBuilder();
		List<String> entitiesList = null;
		List<String> conditionsList = null;
		Map<String,Object> parameterMap = new LinkedHashMap<>();
		try {
			entitiesList = new ArrayList<>();
			conditionsList = new ArrayList<>();
			List<FilterDTO> filters = searchCriteria.getFilters();
			if (!CollectionUtils.isEmpty(filters)) {
				filMap = new LinkedHashMap<>();
				filTypeMap = new HashMap<>();
				switch (type) {
				case SIGNAL:
					entitiesList.add("select distinct root from Topic root ");
					break;
				case RISK:
					entitiesList.add("select  distinct root from RiskPlan root ");
					break;
				case ASSESSMENT:
					entitiesList.add("select  distinct root from AssessmentPlan root ");
					break;
				case DETECTION:
					entitiesList.add("select  distinct root from SignalDetection root ");
					break;
				default:
					break;
				}
				
				for (FilterDTO dto : filters) {
					filMap.put(dto.getFilterKey(), dto.getFilterValues());
					filTypeMap.put(dto.getFilterType(), dto.getFilterValues());
				}
				boolean assigneesExists =false;
				boolean productExists =false;
				boolean conditionExists =false;
				if(filMap.containsKey("assignees"))
				{
					List<?> l = (ArrayList<?>) filMap.get("assignees");
					if(l.size()>0)
						assigneesExists = true;
				}
				if(filTypeMap.containsKey("product"))
				{
					productExists=true;
				}
				if(filTypeMap.containsKey("condition"))
				{
					conditionExists=true;
				}
				
				
				if(assigneesExists)
				{
					switch (type) {
					case SIGNAL:
						entitiesList.add(",TopicSignalValidationAssignmentAssignees joinAss "); 
							break;
					case RISK:
						entitiesList.add(",TopicRiskPlanAssignmentAssignees joinAss ");
						break;
					case ASSESSMENT:
						entitiesList.add(",TopicAssessmentAssignmentAssignees joinAss ");
						break;
					case DETECTION:
						entitiesList.add(",TopicSignalDetectionAssignmentAssignees joinAss ");
						break;
					default:
						break;
					}
				}
				
				if(productExists && conditionExists)
				{
					switch (type) {
					case SIGNAL:
					case DETECTION:
							entitiesList.add(",TopicSocAssignmentConfiguration condition,TopicProductAssignmentConfiguration product ");
							break;
					case RISK:
						queryBuilder.append(",TopicSocAssignmentConfiguration condition,TopicProductAssignmentConfiguration product,AssessmentPlan ass, Topic topic ");
						break;
					case ASSESSMENT:
						queryBuilder.append(",TopicSocAssignmentConfiguration condition,TopicProductAssignmentConfiguration product,Topic topic ");
						break;
					default:
						break;
					}
					/*entitiesList.add(",TopicSocAssignmentConfiguration condition ");
					entitiesList.add(",TopicProductAssignmentConfiguration product ");*/
				}
				else
				{
					if(productExists)
					{
						switch (type) {
						case SIGNAL:
						case DETECTION:
								entitiesList.add(",TopicProductAssignmentConfiguration product ");
								break;
						case RISK:
							queryBuilder.append(",TopicProductAssignmentConfiguration product,AssessmentPlan ass, Topic topic ");
							break;
						case ASSESSMENT:
							queryBuilder.append(",TopicProductAssignmentConfiguration product,Topic topic ");
							break;
						default:
							break;
						}
					}
					if(conditionExists)
					{
						switch (type) {
						case SIGNAL:
						case DETECTION:
								entitiesList.add(",TopicSocAssignmentConfiguration condition ");
								break;
						case RISK:
							queryBuilder.append(",TopicSocAssignmentConfiguration condition,AssessmentPlan ass, Topic topic ");
							break;
						case ASSESSMENT:
							queryBuilder.append(",TopicSocAssignmentConfiguration condition,Topic topic ");
							break;
						default:
							break;
						}
					}
				}
				
				
				if(assigneesExists)
				{
					switch (type) {
					case SIGNAL:
							conditionsList.add(" root.id=joinAss.topicId ");
						break;
					case RISK:
						conditionsList.add(" root.id=joinAss.riskId.id ");
						break;
					case ASSESSMENT:
						conditionsList.add(" root.id = joinAss.assessmentId ");
						break;
					case DETECTION:
						conditionsList.add(" root.id=joinAss.detectionId ");
						break;
					default:
						break;
					}
				}
				
				buildProductAndConditionPredicate(filters,queryBuilder,type,parameterMap,conditionsList);
				
				
				LOGGER.info("filMap.........." + filMap);
				Set<Entry<String, Object>> st = filMap.entrySet();
				for (Entry<String, Object> me : st) {
					String key = me.getKey();
					buildPredicates(me, key, type,parameterMap,conditionsList);
				}
				LOGGER.info("BUILDING OWNER AND ASSIGNEE PREDICATE------"+queryBuilder);
				// Create OWNER AND ASSIGNEE PREDICATE
				addOwnersAssignees(filMap, parameterMap,type,conditionsList);
				
				queryBuilder = new StringBuilder();
				for (String entry : entitiesList) {
					queryBuilder.append(entry);
				}
				if(conditionsList.size()>0)
					queryBuilder.append(" where ");
				for (String wherecondition : conditionsList) {
					queryBuilder.append(wherecondition).append("and");
				}
				queryBuilder.delete(queryBuilder.length()-3, queryBuilder.length());
				LOGGER.info("BUILD PREDICATE QUERY-------" +queryBuilder);
			} else {
				LOGGER.info("FILTER SIZE = 0.....");
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
			queryBuilder.append(" order by root.").append(searchCriteria.getSortBy()).append(" ").append(searchCriteria.getOrderBy().toUpperCase());
			String queryStr = queryBuilder.toString();
			LOGGER.info("queryStr....."+queryStr);
			FilterResponse smtResponse = new FilterResponse();
			Query query = entityManager.createQuery(queryStr);
			LOGGER.info("parameterMap....."+parameterMap);
			LOGGER.info("query....."+query.toString());
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
			LOGGER.error(e);
		}
		return response;
	}

	
	private boolean buildProductAndConditionPredicate(List<FilterDTO> filters, StringBuilder queryBuilder, String type, Map<String, Object> parameterMap, List<String> conditionsList) {
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
		
		LOGGER.info("PRODUCT SET >>>>>>>>>>>>"+productSet);
		LOGGER.info("CONDITION SET >>>>>>>>>>>>"+conditionSet);
		if(!CollectionUtils.isEmpty(productSet) && !CollectionUtils.isEmpty(conditionSet))
		{
			productFilterServiceImpl.constructProductPredicate(productSet,type,parameterMap,conditionsList);
			conditionFilterServiceImpl.constructConditionPredicate(conditionSet,type,parameterMap,conditionsList);
			return true;
		}
		else
		{
			if(!CollectionUtils.isEmpty(productSet))
			{
				productFilterServiceImpl.constructProductPredicate(productSet,type,parameterMap,conditionsList);
				return true;
			}
			else if(!CollectionUtils.isEmpty(conditionSet))
			{
				conditionFilterServiceImpl.constructConditionPredicate(conditionSet,type,parameterMap,conditionsList);
				return true;
			}
		}
		
		return false;
	}
	
	
	private Set<String> constructObjectToSet(List<?> filterValues) {
		LOGGER.info("filterValues...."+filterValues);
		Set<String> valueSet = null;
		try {
			valueSet = new HashSet<>();
			for (Object object : filterValues) {
				LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) object;
				valueSet.add(map.get("key").toString());
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return valueSet;
	}
	
	
	private void addOwnersAssignees(Map<String, Object> filMap,
			Map<String, Object> parameterMap, String type, List<String> conditionsList) {
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
			LOGGER.info("ownerSet........" + ownerSet);
			LOGGER.info("userKey  >>>>>>>>>>>>>>>>>>" + userSet1);
			LOGGER.info("user_group_key >>>>>>>>>>>>>>>>>" + groupSet1);

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
			buildOwnerUserGroupPredicate(owner, user, group,conditionsList);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private void buildOwnerUserGroupPredicate(StringBuilder owner, StringBuilder user,
			StringBuilder group, List<String> conditionsList) {
		if (owner != null) {
			checkOwnerUserAndGroupExists(owner, user, group,conditionsList);
		} else {
			checkUserAndGroupExists(user, group,conditionsList);
		}
	}

	private void checkOwnerUserAndGroupExists(StringBuilder owner, StringBuilder user,
			StringBuilder group, List<String> conditionsList) {
		if (user != null && group != null)
			conditionsList.add(new StringBuilder().append(" (").append(user).append(" or ").append(group).append(" or ").append(owner).append(")").toString());
		else {
			if (user != null)
				conditionsList.add(new StringBuilder().append(" (").append(user).append(" or ").append(owner).append(")").toString());
			else if (group != null)
				conditionsList.add(new StringBuilder().append(" (").append(group).append(" or ").append(owner).append(")").toString());
			else
				conditionsList.add(new StringBuilder().append(" (").append(owner).append(")").toString());
		}
	}
	private void checkUserAndGroupExists(StringBuilder user, StringBuilder group, List<String> conditionsList) {
		if (user != null && group != null)
			conditionsList.add(new StringBuilder().append(" (").append(user).append(" or ").append(group).append(")").toString());
		else {
			if (user != null)
				conditionsList.add(new StringBuilder().append(" (").append(user).append(")").toString());
			else if (group != null)
				conditionsList.add(new StringBuilder().append(" (").append(group).append(")").toString());
		}
	}
	
	private void setParametersMapToQuery(Map<String, Object> parameterMap, Query query) {
		Set<Entry<String, Object>> st = parameterMap.entrySet();
		for (Entry<String, Object> me : st) {
			query.setParameter(me.getKey(), me.getValue());
		}

	}

	private void buildPredicates(Entry<String, Object> me, String key, String type, Map<String, Object> parameterMap, List<String> conditionsList) {
		LOGGER.info("key...."+key);
		switch (key) {
		case "statuses":
			addStatuses(me.getValue(), type,parameterMap,conditionsList);
			break;
		case "signalsource":
			addSourceNames(me.getValue(), parameterMap,conditionsList);
			break;
		case "signalconfirmation":
			addSignalConfirmations(me.getValue(), parameterMap,conditionsList);
			break;
		case "dueDates":
			addDueDate(me.getValue(), type,parameterMap,conditionsList);
			break;
		case "createdDates":
		case "detectedDates":
			addCreatedDate(me.getValue(), type,parameterMap,conditionsList);
			break;
		case "assessmenttaskstatus":
			addAssessmentTaskStatus(me.getValue(), parameterMap,conditionsList);
			break;
		case "finaldispositions":
			addFinalDispositions(me.getValue(), parameterMap,conditionsList);
			break;
		case "riskplanactionstatus":
			addRiskPlanActionStatus(me.getValue(), parameterMap,conditionsList);
			break;
		case "frequency":
			addFrequency(me.getValue(), parameterMap,conditionsList);
			break;
		case "lastRunDates":
			addRunDates(me.getValue(),parameterMap, "lastRunDate",conditionsList);
			break;
		case "nextRunDates":
			addRunDates(me.getValue(),parameterMap,"nextRunDate",conditionsList);
			break;
		default:
			break;
		}
	}

	private void addCreatedDate(Object value, String type,Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> emptyDates = prepareFieldValuesSet(value);
		String dateType =" root.createdDate ";
		if (!"".equalsIgnoreCase(emptyDates.iterator().next().toString())) {
			getDatePredicates(value, parameterMap, dateType,conditionsList);
		}
	}

	private void addDueDate(Object value, String type, Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> emptyDates = prepareFieldValuesSet(value);
		String dateType = null;
		switch (type) {
		case RISK:
			dateType = " root.riskDueDate ";
			break;
		case ASSESSMENT:
			dateType = " root.assessmentDueDate ";
			break;
		case SIGNAL:
			dateType = " root.dueDate ";
			break;
		default:
			break;
		}
		if (!"".equalsIgnoreCase(emptyDates.iterator().next().toString())) {
			getDatePredicates(value, parameterMap, dateType,conditionsList);
		}

	}

	protected void getDatePredicates(Object value, Map<String, Object> parameterMap, String dateType, List<String> conditionsList)  {
		List<?> l = (ArrayList<?>) value;
			for (int i = 0; i < l.size(); i++) {
				Object obj = l.get(i);
				if (i == 0 && !"".equalsIgnoreCase(obj.toString())) {
					Date d1  = SignalUtil.convertStringToDate(obj.toString());
					conditionsList.add(" TO_DATE(TO_CHAR("+dateType+", 'yyyy-MM-dd'), 'yyyy-MM-dd') >= :greaterDate ");
					parameterMap.put("greaterDate", d1);
				}
				if (i == 1 && !"".equalsIgnoreCase(obj.toString())) {
					Date d2  = SignalUtil.convertStringToDate(obj.toString());
					conditionsList.add(" TO_DATE(TO_CHAR("+dateType+", 'yyyy-MM-dd'), 'yyyy-MM-dd') <= :lessDate ");
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
			res.setLastRunDate(detection.getLastRunDate()!=null ? SignalUtil.convertDateTOString(detection.getLastRunDate()):null);
			res.setNextRunDate(detection.getNextRunDate()!=null ? SignalUtil.convertDateTOString(detection.getNextRunDate()):null);
			res.setDenominatorForPoisson(detection.getDenominatorForPoisson());
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
			res.setAssessmentDueDate(assessment.getAssessmentDueDate()!=null ? SignalUtil.convertDateTOString(assessment.getAssessmentDueDate()):null);
			res.setCreatedDate(assessment.getCreatedDate()!=null ? SignalUtil.convertDateTOString(assessment.getCreatedDate()):null);
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
			res.setCreatedDate(risk.getCreatedDate()!=null ?SignalUtil.convertDateTOString(risk.getCreatedDate()):null);
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
			res.setCreatedDate(topic.getCreatedDate()!=null ?SignalUtil.convertDateTOString(topic.getCreatedDate()):null);
			LOGGER.info("topic.getCreatedDate()....."+topic.getCreatedDate());
			LOGGER.info("res.getCreatedDate()....."+res.getCreatedDate());
			fres.add(res);
		}
		return fres;
	}

	private void addRunDates(Object value, Map<String, Object> parameterMap,
			String dateType, List<String> conditionsList) {
		Set<Object> emptyDates = prepareFieldValuesSet(value);
		if (!"".equalsIgnoreCase(emptyDates.iterator().next().toString())) {
			getDatePredicates(value, parameterMap, " root."+dateType,conditionsList);
		}
	}
	
	private void addFrequency(Object statusValue,Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			conditionsList.add(" root.runFrequency in :frequency ");
			parameterMap.put("frequency", list);
		}
	}
	private void addRiskPlanActionStatus(Object statusValue, Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			conditionsList.add(" root.riskTaskStatus in :riskplanactionstatus ");
			parameterMap.put("riskplanactionstatus", list);
		}
	}
	
	
	private void addFinalDispositions(Object statusValue, Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			conditionsList.add(" root.assessmentRiskStatus in :finaldispositions ");
			parameterMap.put("finaldispositions", list);
		}

	}
	
	private void addAssessmentTaskStatus(Object statusValue,  Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			conditionsList.add(" root.assessmentTaskStatus in :assessmenttaskstatus ");
			parameterMap.put("assessmenttaskstatus", list);
		}
	}
	
	private void addSignalConfirmations(Object statusValue,  Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			conditionsList.add(" root.signalConfirmation in :signalconfirmation ");
			parameterMap.put("signalconfirmation", statusList);
		}

	}
	private void addSourceNames(Object statusValue,  Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			conditionsList.add(" root.sourceName in :signalsource ");
			parameterMap.put("signalsource", statusList);
		}
	}
	private void addStatuses(Object statusValue, String type, Map<String, Object> parameterMap, List<String> conditionsList) {
		Set<Object> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			switch (type) {
			case RISK:
				conditionsList.add(" root.status in :statuses ");
				break;
			case ASSESSMENT:
				conditionsList.add(" root.assessmentPlanStatus in :statuses ");
				break;
			default:
				conditionsList.add(" root.signalStatus in :statuses ");
				break;
			}
			parameterMap.put("statuses", statusList);
		}
	}
	

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
