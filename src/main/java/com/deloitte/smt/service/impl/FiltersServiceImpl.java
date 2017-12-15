package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
				createQueryByFilterDetails(filters, criteriaBuilder, rootTopic, joinAssignees, predicates);
				Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				query.select(rootTopic).where(andPredicate)
						.orderBy(criteriaBuilder.desc(rootTopic.get(SmtConstant.CREATED_DATE.getDescription())))
						.distinct(true);
			}
			FilterResponse smtResponse=new FilterResponse();
			query.multiselect(rootTopic.get("id"), rootTopic.get("name"),rootTopic.get("signalStatus"),
					rootTopic.get("description"),rootTopic.get("signalConfirmation"),rootTopic.get("sourceName"),rootTopic.get("createdDate"));
			List<Topic> q = entityManager.createQuery(query).setFirstResult(searchCriteria.getStartIndex()).setMaxResults(50).getResultList();
			if (!CollectionUtils.isEmpty(q)) {
				smtResponse.setTotalRecords(q.size());
				List<FilterDataObject> fres= new ArrayList<>();
				for (Topic topic : q) {
					FilterDataObject res = new FilterDataObject();
					res.setSignalName(topic.getName());
					res.setTopicId(topic.getId());
					res.setDescription(topic.getDescription());
					res.setSignalConfirmation(topic.getSignalConfirmation());
					res.setSignalStatus(topic.getSignalStatus());
					res.setCreatedDate(topic.getCreatedDate());
					res.setSourceName(topic.getSourceName());
					fres.add(res);
				}
				smtResponse.setResult(fres);
			}
			smtResponse.setStartIndex(searchCriteria.getStartIndex());
			ServerResponseObject s = new ServerResponseObject();
			s.setResponse(smtResponse);
			return s;
		}catch (Exception e) {
			LOGGER.error(e);
		}
		
		return null;
	}

	private void createQueryByFilterDetails(List<FilterDTO> filters, CriteriaBuilder criteriaBuilder,
			Root<Topic> rootTopic, Join<Topic, TopicSignalValidationAssignmentAssignees> joinAssignees,
			List<Predicate> predicates) {
		for (FilterDTO filterDTO : filters) {
			String name = filterDTO.getFilterName();
			switch (name) {
				case "Status":
					addStatuses(filterDTO.getFilterValues(), criteriaBuilder, rootTopic, predicates);
					break;
				case "Owner":
						addOwners(filterDTO.getFilterValues(), criteriaBuilder,rootTopic,predicates);
					break;
				case "Assigned To":	
						addAssignedTo(joinAssignees,filterDTO.getFilterValues(), criteriaBuilder,predicates);
					break;
				default: break;	
			}
		}
	}
	



	/**
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addStatuses(List<?> list, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalStatus").in(list)));
		}
	}

	private void addOwners(List<?> list, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(list)) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.isTrue(rootTopic.get(SmtConstant.OWNER.getDescription()).in(list)),
					criteriaBuilder.isTrue(rootTopic.get(SmtConstant.OWNER.getDescription()).isNull())));
		}
	}

	private void addAssignedTo(Join<Topic, TopicSignalValidationAssignmentAssignees> joinAssignees, List<?> list,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		try {
			if (!CollectionUtils.isEmpty(list)) {
				ObjectMapper oMapper = new ObjectMapper();
				Set<String> userSet = new HashSet<>();
				Set<String> groupSet = new HashSet<>();
				for (Object assignObj : list) {
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
				LOGGER.info("userKey  >>>>>>>>>>>>>>>>>>" + userSet);
				LOGGER.info("user_group_key >>>>>>>>>>>>>>>>>" + groupSet);
				createAssignedToPredicate(joinAssignees, criteriaBuilder, predicates, userSet, groupSet);
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

	private void createAssignedToPredicate(Join<Topic, TopicSignalValidationAssignmentAssignees> joinAssignees,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Set<String> userSet, Set<String> groupSet) {
		if (!CollectionUtils.isEmpty(userSet) && (!CollectionUtils.isEmpty(groupSet))) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder
							.isTrue(joinAssignees.get(SmtConstant.USER_KEY.getDescription()).in(userSet)),
					criteriaBuilder.isTrue(
							joinAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(groupSet))));

		} else {
			if (!CollectionUtils.isEmpty(userSet)) {
				predicates.add(criteriaBuilder.or(criteriaBuilder
						.isTrue(joinAssignees.get(SmtConstant.USER_KEY.getDescription()).in(userSet))));
			} else {
				predicates.add(criteriaBuilder.or(criteriaBuilder
						.isTrue(joinAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(groupSet))));

			}
		}
	}
}
