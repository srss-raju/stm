package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.AssignmentUtil;
import com.deloitte.smt.util.Levels;

@Service
public class ConditionFilterServiceImpl<E> {
	private static final Logger LOGGER = Logger.getLogger(ConditionFilterServiceImpl.class);
	@Autowired
	private ConditionLevelRepository conditionLevelRepository;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private AssignmentConditionRepository assignmentConditionRepository;

	@Autowired
	private SignalDetectionRepository signalDetectionRepository;

	@Autowired
	private RiskPlanRepository riskPlanRepository;

	@Autowired
	private AssessmentPlanRepository assessmentPlanRepository;

	public void conditionLevelFilter(List<FilterDTO> filterList, String type) {
		List<Map<String, List<Levels>>> conlevels = null;
		List<ConditionLevels> conditionLevels = null;
		try {
			conlevels = conditionLevelValuesCodes(type);
			conditionLevels = conditionLevelRepository.findAllByOrderByIdAsc();
			if (!AssignmentUtil.isNullOrEmpty(conlevels) && !AssignmentUtil.isNullOrEmpty(conditionLevels)) {
				LOGGER.info("CONDITION LEVEL MAP ===" + conlevels);
				LOGGER.info("CONDITION REPO MAP ===" + conditionLevels);
				constructConditionFilter(filterList, conlevels, conditionLevels);
			}

		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private void constructConditionFilter(List<FilterDTO> filterList, List<Map<String, List<Levels>>> conlevels,
			List<ConditionLevels> levels) {
		FilterDTO dto;
		for (ConditionLevels level : levels) {
			dto = new FilterDTO();
			dto.setFilterKey(level.getValue().replace(" ", ""));
			dto.setFilterName(level.getValue());
			dto.setFilterType("condition");
			boolean exists = false;
			List<?> existList = null;
			for (Map<String, List<Levels>> levelMap : conlevels) {
				if (levelMap.containsKey(level.getKey())) {
					existList = levelMap.get(level.getKey());
					exists = true;
					break;
				} else
					exists = false;
			}
			LOGGER.info("Exists...?" + level.getKey() + "----" + exists);
			if (exists)
				dto.setFilterValues(existList);
			else
				dto.setFilterValues(new ArrayList<>());
			filterList.add(dto);
		}
	}

	private List<Map<String, List<Levels>>> conditionLevelValuesCodes(String type) {
		List<Map<String, List<Levels>>> levelMapList = null;
		List<String> conFillVals = null;
		switch (type) {
		case "signal":
			conFillVals = topicRepository.getConditionFilterValues();
			break;
		case "detection":
			conFillVals = signalDetectionRepository.getConditionFilterValues();
			break;
		case "risk":
		case "assessment":
			conFillVals = getRiskConditionFilterValues(type);

			break;
		default:
			break;
		}
		LOGGER.info("TYPE-------> " + type + " ############>>>>>" + conFillVals);
		if (!CollectionUtils.isEmpty(conFillVals)) {
			levelMapList = new ArrayList<>();
			Map<Integer, Set<String>> itemMap = splitRecordValues(conFillVals);
			LOGGER.info("MAP #############>>>>>" + itemMap);
			Set<Entry<Integer, Set<String>>> st = itemMap.entrySet();
			for (Entry<Integer, Set<String>> me : st) {
				Set<String> recValues = me.getValue();
				List<Object[]> assignmentProductList = getCategoryCodes(recValues);
				createProductValues(levelMapList, assignmentProductList);
			}
			LOGGER.info("assignmentProductList #############>>>>>" + levelMapList);
		}
		return levelMapList;
	}

	private List<String> getRiskConditionFilterValues(String type) {
		List<String> prds = null;
		Set<Long> signalIds;
		if ("risk".equalsIgnoreCase(type))
			signalIds = riskPlanRepository.findAllSignalIds();
		else
			signalIds = assessmentPlanRepository.findAllSignalIds();
		LOGGER.info("LIST OF SIGNAL IDS...." + signalIds);

		if (!CollectionUtils.isEmpty(signalIds))
			prds = topicRepository.getListOfConditionRecordKeys(signalIds);
		LOGGER.info("LIST OF SIGNAL KEYS IDS...." + prds);
		return prds;
	}

	private List<Object[]> getCategoryCodes(Set<String> recValues) {
		return assignmentConditionRepository.findDistinctByCategoryCodeIn(recValues);
	}

	private void createProductValues(List<Map<String, List<Levels>>> levelMapList,
			List<Object[]> assignmentProductList) {
		if (!AssignmentUtil.isNullOrEmpty(assignmentProductList)) {
			Map<String, List<Levels>> levelMap = new LinkedHashMap<>();
			List<Levels> levels = new ArrayList<>();
			for (Object[] assignmentProduct : assignmentProductList) {
				Levels level = new Levels();
				level.setKey(assignmentProduct[0].toString());
				level.setValue(assignmentProduct[1].toString());
				levels.add(level);
			}
			levelMap.put(assignmentProductList.get(0)[2].toString(), levels);
			levelMapList.add(levelMap);
		}
	}

	private Map<Integer, Set<String>> splitRecordValues(List<String> prodFillVals) {
		Map<Integer, Set<String>> itemMap = new LinkedHashMap<>();
		for (String value : prodFillVals) {
			String[] splitArr = value.split("@#");
			for (int i = 0; i < splitArr.length; i++) {
				if (itemMap.containsKey(i)) {
					itemMap.get(i).add(splitArr[i]);
				} else {
					Set<String> set = new HashSet<>();
					set.add(splitArr[i]);
					itemMap.put(i, set);
				}
			}
		}
		return itemMap;
	}

	public void constructProductPredicate(Set<String> conditionSet, CriteriaBuilder criteriaBuilder,
			Join<E, E> conditionJoin, List<Predicate> predicates, String type) {
		Set<String> conRecKeys = new HashSet<>();
		List<String> condFillVals = topicRepository.getConditionFilterValues();
		for (String recKeys : condFillVals) {
			for (String proSet : conditionSet) {
				List<String> list = Arrays.asList(recKeys.split("@#"));
				if (list.contains(proSet)) {
					conRecKeys.add(recKeys);
				}
			}
		}
		
		LOGGER.info("FINAL CONDITION LIST....."+conRecKeys);
		if(!AssignmentUtil.isNullOrEmpty(conRecKeys))
		{
			predicates.add(criteriaBuilder.isTrue(conditionJoin.get("recordKey").in(conRecKeys)));
		}
	}

}
