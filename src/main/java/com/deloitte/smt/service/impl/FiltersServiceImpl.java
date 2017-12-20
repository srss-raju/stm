package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.FilterTypes;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.FilterDataObject;
import com.deloitte.smt.dto.FilterResponse;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.entity.Filters;
import com.deloitte.smt.entity.FiltersStatus;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.repository.FilterRepository;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.repository.TopicSignalDetectionAssignmentAssigneesRepository;
import com.deloitte.smt.service.FiltersService;
import com.deloitte.smt.util.ServerResponseObject;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FiltersServiceImpl<E> implements FiltersService  {
	private static final Logger LOGGER = Logger.getLogger(FiltersServiceImpl.class);
	@Autowired
	private FilterRepository filterRepository;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private AssessmentPlanRepository assessmentPlanRepository;
	@Autowired
	private TopicSignalDetectionAssignmentAssigneesRepository topicSignalDetectionAssignmentAssigneesRepository;

	@Autowired
	private ConditionLevelRepository conditionLevelRepository;
	
	@Autowired
	private ProductLevelRepository productLevelRepository;
	
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
				LOGGER.info(listfi.size());
				filterList = getAllFiltersTypes(listfi);
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return filterList;
	}

	private List<FilterDTO> getAllFiltersTypes(List<Filters> listfi) {
		List<FilterDTO> filterList = null;
		try {
			filterList = new ArrayList<>();
			for (Filters filter : listfi) {
				FilterDTO dto = new FilterDTO();
				dto.setFilterKey(filter.getKey());
				dto.setFilterName(filter.getName());
				String name = filter.getName();
				getFiltersList(filterList, filter, dto, name);
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return filterList;
	}

	private void getFiltersList(List<FilterDTO> filterList, Filters filter, FilterDTO dto, String name) {
		List<?> data;
		LOGGER.info("name...."+name);
		switch (name) {
		case "Status":
		case "Assessment Task Status":
		case "Risk Plan Action Status":
		case "Frequency":
			filterList.add(getFiltersType(filter));
			break;
		case "Product":
			productLevelFilter(filterList);
			break;
		case "Condition":
			conditionLevelFilter(filterList);
			break;
		case "Owner":
			data = topicRepository.findDistinctOwnerNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			break;
		case "Assigned To":
			data = topicSignalDetectionAssignmentAssigneesRepository.getDetectionAssignedUsers();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "Signal Confirmation":
			data = topicRepository.findDistinctSignalConfirmationNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "Signal Source":
			data = topicRepository.getSourceNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "Final Disposition":
			data = assessmentPlanRepository.getAssessmentRiskStatus();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "Detected Date":
		case "Due Date":
		case "Created Date":
		case "Last Run Date":
		case "Next Run Date":
			getEmptyFilterValues(filter, filterList);
			break;

		default:
			break;
		}
	}
	

	private void productLevelFilter(List<FilterDTO> filterList) {
		FilterDTO dto;
		List<ProductLevels> productLevels = productLevelRepository.findAllByOrderByIdAsc();
		if (!CollectionUtils.isEmpty(productLevels)) {
			for (ProductLevels productLevel : productLevels) {
				dto = new FilterDTO();
				dto.setFilterKey(productLevel.getValue().replace(" ", ""));
				dto.setFilterName(productLevel.getValue());
				dto.setFilterValues(new ArrayList<>());
				filterList.add(dto);
			}
		}
	}

	private void conditionLevelFilter(List<FilterDTO> filterList) {
		FilterDTO dto;
		List<ConditionLevels> conditionLevelList = conditionLevelRepository.findAllByOrderByIdAsc();
		if (!CollectionUtils.isEmpty(conditionLevelList)) {
			for (ConditionLevels condLevel : conditionLevelList) {
				dto = new FilterDTO();
				dto.setFilterKey(condLevel.getValue().replace(" ", ""));
				dto.setFilterName(condLevel.getValue());
				dto.setFilterValues(new ArrayList<>());
				filterList.add(dto);
			}
		}
	}

	private void getEmptyFilterValues(Filters filter, List<FilterDTO> filterList) {
		LOGGER.info("getEmptyFilterValues----"+filter.getName());
		FilterDTO dto;
		dto = new FilterDTO();
		dto.setFilterKey(filter.getKey());
		dto.setFilterName(filter.getName());
		dto.setFilterValues(Arrays.asList(FilterTypes.EMPTY.type(),FilterTypes.EMPTY.type()));
		filterList.add(dto);
	}

	private FilterDTO getFiltersType(Filters filter) {
		FilterDTO dto = new FilterDTO();
		dto.setFilterKey(filter.getKey());
		dto.setFilterName(filter.getName());
		LOGGER.info(filter.getType() +"..."+filter.getName());
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
	
	@SuppressWarnings("unchecked")
	@Override
	public ServerResponseObject getSignalDataByFilter(String type, SearchCriteriaDTO searchCriteria) {
		LOGGER.info("SignalDataByFilter....."+searchCriteria);
		CriteriaQuery<E> query = null;
		Root<E> rootTopic = null;
		Join<E,E> joinAssignees=null;
		CriteriaBuilder criteriaBuilder=null;
		List<Predicate> predicates =null;
		Map<String,Object> filMap=null;
		try {
			List<FilterDTO> filters = searchCriteria.getFilters();
			if(!CollectionUtils.isEmpty(filters))
			{
				criteriaBuilder = entityManager.getCriteriaBuilder();
				predicates = new ArrayList<>();
				filMap = new HashMap<>();
				switch(type)
				{
					case "signal":
						query = (CriteriaQuery<E>) criteriaBuilder.createQuery(Topic.class);
						rootTopic = (Root<E>) query.from(Topic.class);
						joinAssignees = rootTopic.join("topicSignalValidationAssignmentAssignees", JoinType.INNER);
						break;
					case "risk":
						query = (CriteriaQuery<E>) criteriaBuilder.createQuery(RiskPlan.class);
						rootTopic = (Root<E>) query.from(RiskPlan.class);
						//joinAssignees =(Join<E,E>) rootTopic.join("topicRiskPlanAssignmentAssignees", JoinType.INNER);
						break;
					case "assessment":
						query = (CriteriaQuery<E>) criteriaBuilder.createQuery(AssessmentPlan.class);
						rootTopic = (Root<E>) query.from(AssessmentPlan.class);
						joinAssignees =(Join<E,E>) rootTopic.join("topicAssessmentAssignmentAssignees", JoinType.INNER);
						break;	
					case "detection":
						query = (CriteriaQuery<E>) criteriaBuilder.createQuery(SignalDetection.class);
						rootTopic = (Root<E>) query.from(SignalDetection.class);
						joinAssignees =(Join<E,E>) rootTopic.join("topicSignalDetectionAssignmentAssignees", JoinType.INNER);
						break;		
					default:
						break;
				}
				
				for (FilterDTO dto : filters) {
					filMap.put(dto.getFilterKey(), dto.getFilterValues()) ;
				}
				LOGGER.info("filMap.........."+filMap);
				
				Set<Entry<String, Object>> st = filMap.entrySet();
				for (Entry<String, Object> me : st) {
					String key = me.getKey();
					buildPredicates(criteriaBuilder, rootTopic, predicates, me, key,type);
				}
				
				//Create OWNER AND ASSIGNEE PREDICATE
				addOwnersAssignees(filMap, criteriaBuilder,joinAssignees,predicates,rootTopic);
				
				Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				
				if (rootTopic != null && andPredicate != null){
					query.select(rootTopic).where(andPredicate)
							.orderBy(criteriaBuilder.desc(rootTopic.get(SmtConstant.CREATED_DATE.getDescription())))
							.distinct(true);
				}
			}
			FilterResponse smtResponse=new FilterResponse();
			TypedQuery<E> q = entityManager.createQuery(query);
			if (searchCriteria.getFetchSize() >= 0) {
				q.setFirstResult(searchCriteria.getFromRecord());
				q.setMaxResults(searchCriteria.getFetchSize());
				List<E> list = q.getResultList();
				LOGGER.info("FILTER DATA FROM DB----"+list+"----"+list.size());
				if(!CollectionUtils.isEmpty(list))
				{
					smtResponse.setTotalRecords(list.size());
					List<FilterDataObject> fres = prepareSignalResponse(list,type);
					smtResponse.setFetchSize(searchCriteria.getFetchSize());
					smtResponse.setFromRecord(searchCriteria.getFromRecord());
					smtResponse.setResult(fres);
				}
			}
			ServerResponseObject response = new ServerResponseObject();
			response.setResponse(smtResponse);
			response.setStatus("SUCCESS");
			return response;
		}catch (Exception e) {
			LOGGER.error(e);
		}
		return null;
	}

	private List<FilterDataObject> prepareSignalResponse(List<E> resultList, String type) {
		List<FilterDataObject> fres = null;
		switch (type) {
		case "signal":
			fres = constructSignalObj(resultList);
			break;
		case "risk":
			fres = constructRiskObj(resultList);
			break;
		case "assessment":
			fres = constructAssessmentObj(resultList);
			break;
		case "detection":
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
			res.setName(assessment.getAssessmentName());
			res.setStatus(assessment.getAssessmentPlanStatus());
			res.setDueDate(assessment.getAssessmentDueDate());
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

	private void buildPredicates(CriteriaBuilder criteriaBuilder, Root<E> rootTopic, List<Predicate> predicates,
			Entry<String, Object> me, String key, String type) {
		switch (key) {
		case "statuses":
			addStatuses(me.getValue(), criteriaBuilder, rootTopic, predicates,type);
			break;
		case "signalsource":
			addSourceNames(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "signalconfirmation":
			addSignalConfirmations(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "dueDates":	
			addDueDate(me.getValue(), criteriaBuilder, rootTopic, predicates,type);
			break;
		case "createdDates":
		case "detectedDates":	
			addCreatedDate(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;	
		case "assessmenttaskstatus":	
			addAssessmentTaskStatus(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "finaldispositions":	
			addFinalDispositions(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "riskplanactionstatus":	
			addRiskPlanActionStatus(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "frequency":	
			addFrequency(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "lastRunDates":
			addRunDates(me.getValue(), criteriaBuilder, rootTopic, predicates,"lastRunDate");
			break;
		case "nextRunDates": 
			addRunDates(me.getValue(), criteriaBuilder, rootTopic, predicates,"nextRunDate");
			break;
		default:
			break;
		}
	}

	private void addRunDates(Object dateValues, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates, String type) {
		Set<String> emptyDates =  prepareFieldValuesSet(dateValues);
		if(!"".equalsIgnoreCase(emptyDates.iterator().next()))
		{
			getDatePredicates(dateValues, criteriaBuilder, rootTopic, predicates, type);
		}
		
	}

	private void addFrequency(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("runFrequency").in(list)));
		}
	}

	private void addFinalDispositions(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get(SmtConstant.ASSESSMENT_RISK_STATUS.getDescription()).in(list)));
		}
		
	}

	private void addAssessmentTaskStatus(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get(SmtConstant.ASSESSMENT_TASK_STATUS.getDescription()).in(list)));
		}

	}

	private void addRiskPlanActionStatus(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("riskTaskStatus").in(list)));
		}

	}

	

	private void addDueDate(Object value, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates, String type) {
		Set<String> emptyDates =  prepareFieldValuesSet(value);
		String dateType;
		switch(type)
		{
			case "risk":
				dateType="riskDueDate";
				break;
			case "assessment":
				dateType="assessmentDueDate";
				break;	
			default:
				dateType = "dueDate";
				break;
		}
			
		if(!"".equalsIgnoreCase(emptyDates.iterator().next()))
		{
			getDatePredicates(value, criteriaBuilder, rootTopic, predicates, dateType);
		}
	}

	protected void getDatePredicates(Object value, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates, String dateType) {
		List<?> l = (ArrayList<?>) value;
		for (int i = 0; i < l.size(); i++) {
			Object obj = l.get(i);
			if(i==0 && !"".equalsIgnoreCase(obj.toString()))
			{
				Date date1=SignalUtil.convertStringToDate(obj.toString());
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(rootTopic.get(dateType), date1));
			}
			if(i==1 && !"".equalsIgnoreCase(obj.toString()))
			{
				Date date2=SignalUtil.convertStringToDate(obj.toString());
				predicates.add(criteriaBuilder.lessThanOrEqualTo(rootTopic.get(dateType), date2));
			}
			
		}
	}
	private void addCreatedDate(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		Set<String> emptyDates =  prepareFieldValuesSet(statusValue);
		String dateType = SmtConstant.CREATED_DATE.getDescription();
		if(!"".equalsIgnoreCase(emptyDates.iterator().next()))
		{
			getDatePredicates(statusValue, criteriaBuilder, rootTopic, predicates, dateType);
		}
	}
	private void addSignalConfirmations(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		 Set<String> statusList = prepareFieldValuesSet(statusValue);
		 if (!CollectionUtils.isEmpty(statusList)) {
		predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalConfirmation").in(statusValue)));
	}
		
	}

	private void addSourceNames(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates) {
		 Set<String> statusList = prepareFieldValuesSet(statusValue);
		 if (!CollectionUtils.isEmpty(statusList)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("sourceName").in(statusList)));
		}
		
	}

	private void addStatuses(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> rootTopic,
			List<Predicate> predicates, String type) {
		 Set<String> statusList = prepareFieldValuesSet(statusValue);
		 
		if (!CollectionUtils.isEmpty(statusList)) {
			
			switch(type)
			{
				case "risk":
					predicates.add(criteriaBuilder.isTrue(rootTopic.get("status").in(statusList)));
					break;
				case "assessment":
					predicates.add(criteriaBuilder.isTrue(rootTopic.get("assessmentPlanStatus").in(statusList)));
					break;	
				default:
					predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalStatus").in(statusList)));
					break;
			}
		}
		
	}

	private void addOwnersAssignees(Map<String, Object> filMap, CriteriaBuilder criteriaBuilder,
			Join<E, E> joinAssignees, List<Predicate> predicates,
			Root<E> rootTopic) {
		
		Set<String> ownerSet=null;
		Set<String> userSet1=null;
		Set<String> groupSet1=null;
		Predicate owner= null;
		Predicate user = null;
		Predicate group= null;
		try {
			Object ownerMap = filMap.get("owners");
			Object assigneesMap = filMap.get("assignees");
			if(null!=ownerMap)
			{
				ownerSet = prepareFieldValuesSet(ownerMap);
				owner = criteriaBuilder.isTrue(rootTopic.get(SmtConstant.OWNER.getDescription()).in(ownerSet));
			}
			
			if(null!=assigneesMap)
			{
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
				userSet1=userSet;
				groupSet1= groupSet;
			}

			LOGGER.info("ownerSet........" + ownerSet);
			LOGGER.info("userKey  >>>>>>>>>>>>>>>>>>" + userSet1);
			LOGGER.info("user_group_key >>>>>>>>>>>>>>>>>" + groupSet1);
			
			if (!CollectionUtils.isEmpty(userSet1)) {
				user = criteriaBuilder.isTrue(joinAssignees.get(SmtConstant.USER_KEY.getDescription()).in(userSet1));
			} 
			if (!CollectionUtils.isEmpty(groupSet1)) 
			{
				group = criteriaBuilder.isTrue(joinAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(groupSet1));
			}
			
			buildOwnerUserGroupPredicate(criteriaBuilder, predicates, owner, user, group);
			
			
		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

	private void buildOwnerUserGroupPredicate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
			Predicate owner, Predicate user, Predicate group) {
		if(owner!=null)
		{
			if(user!=null && group!=null)
				predicates.add(criteriaBuilder.or(user,group,owner));
			else
			{
				if(user!=null)
					predicates.add(criteriaBuilder.or(user,owner));
				else if (group!=null)
					predicates.add(criteriaBuilder.or(group,owner));
				else
					predicates.add(criteriaBuilder.or(owner));
			}
		}
		else
		{
			if(user!=null && group!=null)
				predicates.add(criteriaBuilder.or(user,group));
			else
			{
				if(user!=null)
					predicates.add(criteriaBuilder.or(user));
				else if(group!=null)
					predicates.add(criteriaBuilder.or(group));
			}
		}
	}

	private Set<String> prepareFieldValuesSet(Object ownerMap) {
		Set<String> ownersSet = new HashSet<>();
		List<?> l = (ArrayList<?>) ownerMap;
		for (Object obj : l) {
			if (!"".equals(obj.toString()))
				ownersSet.add(obj.toString());
		}
		return ownersSet;
	}

}
