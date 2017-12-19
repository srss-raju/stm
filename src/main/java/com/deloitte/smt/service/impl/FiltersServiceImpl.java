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
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.entity.Filters;
import com.deloitte.smt.entity.FiltersStatus;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicSignalValidationAssignmentAssignees;
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
public class FiltersServiceImpl implements FiltersService  {
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
	
	@Override
	public ServerResponseObject getSignalDataByFilter(String type, SearchCriteriaDTO searchCriteria) {
		LOGGER.info("SignalDataByFilter....."+searchCriteria);
		try {
			List<FilterDTO> filters = searchCriteria.getFilters();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Topic> query = criteriaBuilder.createQuery(Topic.class);
			Root<Topic> rootTopic = query.from(Topic.class);
			if(!CollectionUtils.isEmpty(filters))
			{
				Join<Topic,TopicSignalValidationAssignmentAssignees> joinAssignees = rootTopic.join("topicSignalValidationAssignmentAssignees", JoinType.INNER);
				List<Predicate> predicates = new ArrayList<>(10);
				Map<String,Object> filMap = new HashMap<>();
				for (FilterDTO dto : filters) {
					filMap.put(dto.getFilterKey(), dto.getFilterValues()) ;
				}
				LOGGER.info("filMap.........."+filMap);
				//Create OWNER AND ASSIGNEE PREDICATE
				addOwnersAssignees(filMap, criteriaBuilder,joinAssignees,predicates,rootTopic);
				Set<Entry<String, Object>> st = filMap.entrySet();
				for (Entry<String, Object> me : st) {
					String key = me.getKey();
					buildPredicates(criteriaBuilder, rootTopic, predicates, me, key);
				}
				Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				query.select(rootTopic).where(andPredicate)
						.orderBy(criteriaBuilder.desc(rootTopic.get(SmtConstant.CREATED_DATE.getDescription())))
						.distinct(true);
			}
			FilterResponse smtResponse=new FilterResponse();
			TypedQuery<Topic> q = entityManager.createQuery(query);
			
			if (!CollectionUtils.isEmpty(q.getResultList())) {
				smtResponse.setTotalRecords(q.getResultList().size());
				if (searchCriteria.getFetchSize() != 0) {
					q.setFirstResult(searchCriteria.getFromRecord());
					q.setMaxResults(searchCriteria.getFetchSize());
					List<Topic> q1 = q.getResultList();
					List<FilterDataObject> fres = prepareSignalResponse(q1);
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

	private void buildPredicates(CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic, List<Predicate> predicates,
			Entry<String, Object> me, String key) {
		switch (key) {
		case "statuses":
			addStatuses(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "signalsource":
			addSourceNames(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "signalconfirmation":
			addSignalConfirmations(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "dueDates":	
			addDueDate(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;
		case "createdDates":
		case "detectedDates":	
			addCreatedDate(me.getValue(), criteriaBuilder, rootTopic, predicates);
			break;	
		default:
			break;
		}
	}

	private List<FilterDataObject> prepareSignalResponse(List<Topic> q1) {
		List<FilterDataObject> fres = new ArrayList<>();
		for (Topic topic : q1) {
			FilterDataObject res = new FilterDataObject();
			res.setSignalId(topic.getId());
			res.setName(topic.getName());
			res.setSignalStatus(topic.getSignalStatus());
			res.setDescription(topic.getDescription());
			res.setSignalConfirmation(topic.getSignalConfirmation());
			res.setSourceName(topic.getSourceName());
			res.setCreatedDate(topic.getCreatedDate());
			fres.add(res);
		}
		return fres;
	}

	private void addDueDate(Object value, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		Set<String> emptyDates =  prepareFieldValuesSet(value);
		String dateType = "dueDate";
		if(!"".equalsIgnoreCase(emptyDates.iterator().next()))
		{
			getDatePredicates(value, criteriaBuilder, rootTopic, predicates, dateType);
		}
	}

	protected void getDatePredicates(Object value, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
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
	private void addCreatedDate(Object statusValue, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		Set<String> emptyDates =  prepareFieldValuesSet(statusValue);
		String dateType = SmtConstant.CREATED_DATE.getDescription();
		if(!"".equalsIgnoreCase(emptyDates.iterator().next()))
		{
			getDatePredicates(statusValue, criteriaBuilder, rootTopic, predicates, dateType);
		}
	}
	private void addSignalConfirmations(Object statusValue, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		 Set<String> statusList = prepareFieldValuesSet(statusValue);
		 if (!CollectionUtils.isEmpty(statusList)) {
		predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalConfirmation").in(statusValue)));
	}
		
	}

	private void addSourceNames(Object statusValue, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		 Set<String> statusList = prepareFieldValuesSet(statusValue);
		 if (!CollectionUtils.isEmpty(statusList)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("sourceName").in(statusList)));
		}
		
	}

	private void addStatuses(Object statusValue, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		 Set<String> statusList = prepareFieldValuesSet(statusValue);
		 System.out.println("------statusList----"+statusList);
		 
		 if (!CollectionUtils.isEmpty(statusList)) {
				predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalStatus").in(statusList)));
			}
		
	}

	private void addOwnersAssignees(Map<String, Object> filMap, CriteriaBuilder criteriaBuilder,
			Join<Topic, TopicSignalValidationAssignmentAssignees> joinAssignees, List<Predicate> predicates,
			Root<Topic> rootTopic) {
		
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
