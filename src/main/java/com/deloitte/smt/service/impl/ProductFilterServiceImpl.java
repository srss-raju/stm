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
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.Levels;

@Service
public class ProductFilterServiceImpl {
	private static final Logger LOGGER = Logger.getLogger(FiltersServiceImpl.class);
	@Autowired
	private ProductLevelRepository productLevelRepository;
	
	@Autowired
	private AssignmentProductRepository assignmentProductRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	public void productLevelFilter(List<FilterDTO> filterList, String type) {
		try {
			FilterDTO dto;
			List<Map<String, List<Levels>>> prlevels = productLevelValuesCodes(type);
			LOGGER.info("PRODUCT LEVEL MAP ==="+prlevels.size());
			List<ProductLevels> productLevels = productLevelRepository.findAllByOrderByIdAsc();
			LOGGER.info("PRODUCT LEVEL MAP ==="+productLevels.size());
			if (!CollectionUtils.isEmpty(productLevels) && !CollectionUtils.isEmpty(prlevels)) {
				for (ProductLevels productLevel : productLevels) {
					dto = new FilterDTO();
					dto.setFilterKey(productLevel.getValue().replace(" ", ""));
					dto.setFilterName(productLevel.getValue());
					dto.setFilterType("product");
					boolean exists=false;
					List<?>	existList=null;
					for (Map<String, List<Levels>> prlevelMap : prlevels) {
						if(prlevelMap.containsKey(productLevel.getKey()))
						{
							existList = prlevelMap.get(productLevel.getKey());
							exists=true;
							break;
						}
						else
							exists=false;
					}
					LOGGER.info("Exists...?"+productLevel.getKey()+"----"+exists);
					if(exists)
						dto.setFilterValues(existList);
					else
						dto.setFilterValues(new ArrayList<>());
					filterList.add(dto);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private List<Map<String, List<Levels>>> productLevelValuesCodes(String type) {
		List<Map<String, List<Levels>>> levelMapList = null;
		List<String> prodFillVals = topicRepository.getProductFilterValues();
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
			if(itemMap!=null)
			{
				Set<Entry<Integer, Set<String>>> st = itemMap.entrySet();
				for (Entry<Integer, Set<String>> me : st) {
					Set<String> recValues = me.getValue();
					List<Object[]> assignmentProductList = assignmentProductRepository
							.findDistinctByCategoryCodeIn(recValues);
					if (!CollectionUtils.isEmpty(assignmentProductList) && assignmentProductList != null) {
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
				LOGGER.info("assignmentProductList #############>>>>>" + levelMapList);
			}

		}
		return levelMapList;
	}

}
