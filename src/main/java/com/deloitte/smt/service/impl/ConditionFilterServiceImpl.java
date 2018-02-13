package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.LevelsDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConditionValuesRepository;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.ProductUtil;

@Service
public class ConditionFilterServiceImpl {
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ConditionLevelRepository conditionLevelRepository;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private AssignmentConditionValuesRepository assignmentConditionValuesRepository;

	@Autowired
	private SignalDetectionRepository signalDetectionRepository;

	@Autowired
	private RiskPlanRepository riskPlanRepository;

	@Autowired
	private AssessmentPlanRepository assessmentPlanRepository;

	public void conditionLevelFilter(List<FilterDTO> filterList, String type) {
		List<Map<String, List<LevelsDTO>>> conlevels = null;
		List<ConditionLevels> conditionLevels = null;
		try {
			conlevels = conditionLevelValuesCodes(type);
			conditionLevels = conditionLevelRepository.findAllByOrderByIdAsc();
			logger.info("CONDITION LEVEL MAP ===" + conlevels);
			logger.info("CONDITION REPO MAP ===" + conditionLevels);
			if(!CollectionUtils.isEmpty(conditionLevels)){
				constructConditionFilter(filterList, conlevels, conditionLevels);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void constructConditionFilter(List<FilterDTO> filterList, List<Map<String, List<LevelsDTO>>> conlevels, List<ConditionLevels> levels) {
			for (ConditionLevels level : levels) {
				setFilters(filterList, conlevels, level);
			}
	}

	private void setFilters(List<FilterDTO> filterList,
			List<Map<String, List<LevelsDTO>>> conlevels, ConditionLevels level) {
		FilterDTO dto;
		dto = new FilterDTO();
		dto.setFilterKey(level.getValue().replace(" ", ""));
		dto.setFilterName(level.getValue());
		dto.setFilterType("condition");
		List<?> existList = null;
		boolean exists = false;
		
		if(!CollectionUtils.isEmpty(conlevels))
		{
			for (Map<String, List<LevelsDTO>> levelMap : conlevels) {
				if (levelMap.containsKey(level.getKey())) {
					existList = levelMap.get(level.getKey());
					exists = true;
					break;
				} else{
					exists = false;
				}
			}
		}
		logger.info("Exists...?" + level.getKey() + "----" + exists);
		setFilterValues(dto, existList, exists);
		filterList.add(dto);
	}

	private void setFilterValues(FilterDTO dto, List<?> existList, boolean exists) {
		if (exists)
			dto.setFilterValues(existList);
		else
			dto.setFilterValues(new ArrayList<>());
	}

	private List<Map<String, List<LevelsDTO>>> conditionLevelValuesCodes(String type) {
		List<Map<String, List<LevelsDTO>>> levelMapList = null;
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
		logger.info("TYPE-------> " + type + " ############>>>>>" + conFillVals);
		if (!CollectionUtils.isEmpty(conFillVals)) {
			levelMapList = new ArrayList<>();
			ProductUtil productUtil = new ProductUtil();
			Map<Integer, Set<String>> itemMap = productUtil.splitRecordValues(conFillVals);
			logger.info("MAP #############>>>>>" + itemMap);
			Set<Entry<Integer, Set<String>>> st = itemMap.entrySet();
			for (Entry<Integer, Set<String>> me : st) {
				Set<String> recValues = me.getValue();
				List<Object[]> assignmentProductList = getCategoryCodes(recValues);
				
				productUtil.createProductValues(levelMapList, assignmentProductList);
			}
			logger.info("assignmentProductList #############>>>>>" + levelMapList);
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
		logger.info("LIST OF SIGNAL IDS...." + signalIds);

		if (!CollectionUtils.isEmpty(signalIds))
			prds = topicRepository.getListOfConditionRecordKeys(signalIds);
		logger.info("LIST OF SIGNAL KEYS IDS...." + prds);
		return prds;
	}

	private List<Object[]> getCategoryCodes(Set<String> recValues) {
		return assignmentConditionValuesRepository.findDistinctByCategoryCodeIn(recValues);
	}

	public void constructConditionPredicate(Set<String> conditionSet, StringBuilder queryBuilder, String type,
			Map<String, Object> parameterMap) {
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
		
		logger.info("FINAL CONDITION LIST....."+conRecKeys);
		if(!CollectionUtils.isEmpty(conRecKeys))
		{
			switch (type) {
				case "signal":
					queryBuilder.append(" and condition.topicId=root.id ");
					break;
				case "detection":
					queryBuilder.append(" and condition.detectionId=root.id ");
					break;
				case "risk":
				case "assessment":
					queryBuilder.append(" and condition.topicId=topic.id ");
					break;
				default:
					break;
			}
			
			queryBuilder.append(" and condition.recordKey in :recordKey");
			parameterMap.put("recordKey", conRecKeys);
		}
		
	}

}
