package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.Levels;

@Service
public class ConditionFilterServiceImpl {
	private static final Logger LOGGER = Logger.getLogger(ConditionFilterServiceImpl.class);
	@Autowired
	private ConditionLevelRepository conditionLevelRepository;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private AssignmentConditionRepository assignmentConditionRepository;

	public void conditionLevelFilter(List<FilterDTO> filterList, String type) {
		try {
			FilterDTO dto;
			List<Map<String, List<Levels>>> prlevels = conditionLevelValuesCodes(type);
			LOGGER.info("CONDITION LEVEL MAP ===" + prlevels.size());
			List<ConditionLevels> conditionLevelList = conditionLevelRepository.findAllByOrderByIdAsc();
			if (!CollectionUtils.isEmpty(conditionLevelList) && !CollectionUtils.isEmpty(prlevels)) {
				for (ConditionLevels condLevel : conditionLevelList) {
					dto = new FilterDTO();
					dto.setFilterKey(condLevel.getValue().replace(" ", ""));
					dto.setFilterName(condLevel.getValue());
					dto.setFilterType("condition");
					boolean exists = false;
					List<?> existList = null;
					for (Map<String, List<Levels>> prlevelMap : prlevels) {
						if (prlevelMap.containsKey(condLevel.getKey())) {
							existList = prlevelMap.get(condLevel.getKey());
							exists = true;
							break;
						} else
							exists = false;
					}
					LOGGER.info("Exists...?" + condLevel.getKey() + "----" + exists);
					if (exists)
						dto.setFilterValues(existList);
					else
						dto.setFilterValues(new ArrayList<>());
					filterList.add(dto);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private List<Map<String, List<Levels>>> conditionLevelValuesCodes(String type) {
		List<Map<String, List<Levels>>> levelMapList = null;
		List<String> prodFillVals = topicRepository.getConditionFilterValues();
		LOGGER.info("#############>>>>>" + prodFillVals);
		if (!CollectionUtils.isEmpty(prodFillVals)) {
			levelMapList = new ArrayList<>();
			Map<Integer, Set<String>> itemMap = new LinkedHashMap<>();
			for (String value : prodFillVals) {
				String[] splitArr = value.split("@#\\$");
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
			LOGGER.info("MAP #############>>>>>" + itemMap);
			if (itemMap != null) {
				Set<Entry<Integer, Set<String>>> st = itemMap.entrySet();
				for (Entry<Integer, Set<String>> me : st) {
					Set<String> recValues = me.getValue();
					List<Object[]>  assignmentConditionList = assignmentConditionRepository
							.findDistinctByCategoryCodeIn(recValues);
					LOGGER.info("ASSIGNMENT CONDITION LIST ======> "+assignmentConditionList);
					
					if (!CollectionUtils.isEmpty(assignmentConditionList) && assignmentConditionList != null) {
						Map<String, List<Levels>> levelMap = new LinkedHashMap<>();
						List<Levels> levels = new ArrayList<>();
						for (Object[] assignmentProduct : assignmentConditionList) {
							Levels level = new Levels();
							level.setKey(assignmentProduct[0].toString());
							level.setValue(assignmentProduct[1].toString());
							levels.add(level);
						}
						levelMap.put(assignmentConditionList.get(0)[2].toString(), levels);
						levelMapList.add(levelMap);
					}
				}
				LOGGER.info("assignmentProductList #############>>>>>" + levelMapList);
			}

		}
		return levelMapList;
	}
}
