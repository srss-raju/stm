package com.deloitte.smt.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.LevelsDTO;

public class ProductUtil {
	
	public void createProductValues(List<Map<String, List<LevelsDTO>>> levelMapList, List<Object[]> assignmentProductList) {
		if (!CollectionUtils.isEmpty(assignmentProductList)) {
			Map<String, List<LevelsDTO>> levelMap = new LinkedHashMap<>();
			List<LevelsDTO> levels = new ArrayList<>();
			for (Object[] assignmentProduct : assignmentProductList) {
				LevelsDTO level = new LevelsDTO();
				level.setKey(assignmentProduct[0].toString());
				level.setValue(assignmentProduct[1].toString());
				levels.add(level);
			}
			levelMap.put(assignmentProductList.get(0)[2].toString(), levels);
			levelMapList.add(levelMap);
		}
	}
	
	public Map<Integer, Set<String>> splitRecordValues(List<String> prodFillVals) {
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

}
