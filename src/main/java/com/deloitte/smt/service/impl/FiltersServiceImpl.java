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
import com.deloitte.smt.entity.Filters;
import com.deloitte.smt.entity.FiltersStatus;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.FilterRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.repository.TopicSignalDetectionAssignmentAssigneesRepository;
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
		List<?> data;
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
			data = topicRepository.findDistinctOwnerNames();
			dto.setFilterValues(data == null ? new ArrayList<>() : data);
			filterList.add(dto);
			break;
		case "assignees":
			data = topicSignalDetectionAssignmentAssigneesRepository.getDetectionAssignedUsers();
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
		CriteriaQuery<E> query = null;
		ServerResponseObject response = null;
		Root<E> root = null;
		Join<E, E> joinAssignees = null;
		CriteriaBuilder criteriaBuilder = null;
		List<Predicate> predicates = null;
		Map<String, Object> filMap = null;
		try {
			List<FilterDTO> filters = searchCriteria.getFilters();
			if (!CollectionUtils.isEmpty(filters)) {
				criteriaBuilder = entityManager.getCriteriaBuilder();
				predicates = new ArrayList<>();
				filMap = new HashMap<>();
				switch (type) {
				case SIGNAL:
					query = (CriteriaQuery<E>) criteriaBuilder.createQuery(Topic.class);
					root = (Root<E>) query.from(Topic.class);
					joinAssignees = root.join("topicSignalValidationAssignmentAssignees", JoinType.INNER);
					break;
				case RISK:
					query = (CriteriaQuery<E>) criteriaBuilder.createQuery(RiskPlan.class);
					root = (Root<E>) query.from(RiskPlan.class);
					joinAssignees = (Join<E, E>) root.join("topicRiskPlanAssignmentAssignees", JoinType.INNER);
					break;
				case ASSESSMENT:
					query = (CriteriaQuery<E>) criteriaBuilder.createQuery(AssessmentPlan.class);
					root = (Root<E>) query.from(AssessmentPlan.class);
					joinAssignees = (Join<E, E>) root.join("topicAssessmentAssignmentAssignees", JoinType.INNER);
					break;
				case DETECTION:
					query = (CriteriaQuery<E>) criteriaBuilder.createQuery(SignalDetection.class);
					root = (Root<E>) query.from(SignalDetection.class);
					joinAssignees = (Join<E, E>) root.join("topicSignalDetectionAssignmentAssignees", JoinType.INNER);
					break;
				default:
					break;
				}

				for (FilterDTO dto : filters) {
					filMap.put(dto.getFilterKey(), dto.getFilterValues());

				}
				LOGGER.info("filMap.........." + filMap);

				Set<Entry<String, Object>> st = filMap.entrySet();
				for (Entry<String, Object> me : st) {
					String key = me.getKey();
					buildPredicates(criteriaBuilder, root, predicates, me, key, type);
				}
				LOGGER.info("BUILDING OWNER AND ASSIGNEE PREDICATE------");
				// Create OWNER AND ASSIGNEE PREDICATE
				addOwnersAssignees(filMap, criteriaBuilder, joinAssignees, predicates, root);

				Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				LOGGER.info("BUILD PREDICATE QUERY-------");
				if (root != null && andPredicate != null) {
					query.select(root).where(andPredicate)
							.orderBy(criteriaBuilder.desc(root.get(SmtConstant.CREATED_DATE.getDescription())))
							.distinct(true);
				}

			}
			FilterResponse smtResponse = new FilterResponse();
			TypedQuery<E> q = entityManager.createQuery(query);
			if (!CollectionUtils.isEmpty(q.getResultList())) {
				smtResponse.setTotalRecords(q.getResultList().size());

			}
			if (searchCriteria.getFetchSize() >= 0) {
				q.setFirstResult(searchCriteria.getFromRecord());
				q.setMaxResults(searchCriteria.getFetchSize());
				smtResponse.setFetchSize(searchCriteria.getFetchSize());
				smtResponse.setFromRecord(searchCriteria.getFromRecord());
				List<FilterDataObject> fres = prepareSignalResponse(q.getResultList(), type);
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

	private void buildPredicates(CriteriaBuilder criteriaBuilder, Root<E> root, List<Predicate> predicates,
			Entry<String, Object> me, String key, String type) {
		switch (key) {
		case "statuses":
			addStatuses(me.getValue(), criteriaBuilder, root, predicates, type);
			break;
		case "signalsource":
			addSourceNames(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "signalconfirmation":
			addSignalConfirmations(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "dueDates":
			addDueDate(me.getValue(), criteriaBuilder, root, predicates, type);
			break;
		case "createdDates":
		case "detectedDates":
			addCreatedDate(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "assessmenttaskstatus":
			addAssessmentTaskStatus(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "finaldispositions":
			addFinalDispositions(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "riskplanactionstatus":
			addRiskPlanActionStatus(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "frequency":
			addFrequency(me.getValue(), criteriaBuilder, root, predicates);
			break;
		case "lastRunDates":
			addRunDates(me.getValue(), criteriaBuilder, root, predicates, "lastRunDate");
			break;
		case "nextRunDates":
			addRunDates(me.getValue(), criteriaBuilder, root, predicates, "nextRunDate");
			break;
		default:
			break;
		}
	}

	private void addRunDates(Object dateValues, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates, String type) {
		Set<String> emptyDates = prepareFieldValuesSet(dateValues);
		if (!"".equalsIgnoreCase(emptyDates.iterator().next())) {
			getDatePredicates(dateValues, criteriaBuilder, root, predicates, type);
		}

	}

	private void addFrequency(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(root.get("runFrequency").in(list)));
		}
	}

	private void addFinalDispositions(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(
					criteriaBuilder.isTrue(root.get(SmtConstant.ASSESSMENT_RISK_STATUS.getDescription()).in(list)));
		}

	}

	private void addAssessmentTaskStatus(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(
					criteriaBuilder.isTrue(root.get(SmtConstant.ASSESSMENT_TASK_STATUS.getDescription()).in(list)));
		}

	}

	private void addRiskPlanActionStatus(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> list = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(root.get("riskTaskStatus").in(list)));
		}

	}

	private void addDueDate(Object value, CriteriaBuilder criteriaBuilder, Root<E> root, List<Predicate> predicates,
			String type) {
		Set<String> emptyDates = prepareFieldValuesSet(value);
		String dateType;
		switch (type) {
		case RISK:
			dateType = "riskDueDate";
			break;
		case ASSESSMENT:
			dateType = "assessmentDueDate";
			break;
		default:
			dateType = "dueDate";
			break;
		}

		if (!"".equalsIgnoreCase(emptyDates.iterator().next())) {
			getDatePredicates(value, criteriaBuilder, root, predicates, dateType);
		}
	}

	protected void getDatePredicates(Object value, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates, String dateType) {
		List<?> l = (ArrayList<?>) value;
		for (int i = 0; i < l.size(); i++) {
			Object obj = l.get(i);
			if (i == 0 && !"".equalsIgnoreCase(obj.toString())) {
				Date date1 = SignalUtil.convertStringToDate(obj.toString());
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(dateType), date1));
			}
			if (i == 1 && !"".equalsIgnoreCase(obj.toString())) {
				Date date2 = SignalUtil.convertStringToDate(obj.toString());
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(dateType), date2));
			}

		}
	}

	private void addCreatedDate(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> emptyDates = prepareFieldValuesSet(statusValue);
		String dateType = SmtConstant.CREATED_DATE.getDescription();
		if (!"".equalsIgnoreCase(emptyDates.iterator().next())) {
			getDatePredicates(statusValue, criteriaBuilder, root, predicates, dateType);
		}
	}

	private void addSignalConfirmations(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			predicates.add(criteriaBuilder.isTrue(root.get("signalConfirmation").in(statusValue)));
		}

	}

	private void addSourceNames(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates) {
		Set<String> statusList = prepareFieldValuesSet(statusValue);
		if (!CollectionUtils.isEmpty(statusList)) {
			predicates.add(criteriaBuilder.isTrue(root.get("sourceName").in(statusList)));
		}

	}

	private void addStatuses(Object statusValue, CriteriaBuilder criteriaBuilder, Root<E> root,
			List<Predicate> predicates, String type) {
		Set<String> statusList = prepareFieldValuesSet(statusValue);

		if (!CollectionUtils.isEmpty(statusList)) {

			switch (type) {
			case RISK:
				predicates.add(criteriaBuilder.isTrue(root.get("status").in(statusList)));
				break;
			case ASSESSMENT:
				predicates.add(criteriaBuilder.isTrue(root.get("assessmentPlanStatus").in(statusList)));
				break;
			default:
				predicates.add(criteriaBuilder.isTrue(root.get("signalStatus").in(statusList)));
				break;
			}
		}

	}

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

			LOGGER.info("ownerSet........" + ownerSet);
			LOGGER.info("userKey  >>>>>>>>>>>>>>>>>>" + userSet1);
			LOGGER.info("user_group_key >>>>>>>>>>>>>>>>>" + groupSet1);

			if (!CollectionUtils.isEmpty(userSet1)) {
				user = criteriaBuilder.isTrue(joinAssignees.get(SmtConstant.USER_KEY.getDescription()).in(userSet1));
			}
			if (!CollectionUtils.isEmpty(groupSet1)) {
				group = criteriaBuilder
						.isTrue(joinAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(groupSet1));
			}

			buildOwnerUserGroupPredicate(criteriaBuilder, predicates, owner, user, group);

		} catch (Exception e) {
			LOGGER.error(e);
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
