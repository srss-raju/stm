package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.ConditionLevelRepository;

@Service
public class ConditionFilterServiceImpl {
	@Autowired
	private ConditionLevelRepository conditionLevelRepository;

	public void conditionLevelFilter(List<FilterDTO> filterList, String type) {

		FilterDTO dto;
		List<ConditionLevels> conditionLevelList = conditionLevelRepository.findAllByOrderByIdAsc();
		if (!CollectionUtils.isEmpty(conditionLevelList)) {
			for (ConditionLevels condLevel : conditionLevelList) {
				dto = new FilterDTO();
				dto.setFilterKey(condLevel.getValue().replace(" ", ""));
				dto.setFilterName(condLevel.getValue());
				dto.setFilterValues(conditionLevelValuesCodes(type));
				dto.setFilterType("condition");
				filterList.add(dto);
			}
		}
	}

	private List<?> conditionLevelValuesCodes(String type) {
		return new ArrayList<>();
	}

}
