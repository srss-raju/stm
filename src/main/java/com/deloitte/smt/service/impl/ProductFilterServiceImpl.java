package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.Join;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.AssignmentUtil;
import com.deloitte.smt.util.Levels;

@Service
public class ProductFilterServiceImpl {
	private static final Logger LOGGER = Logger.getLogger(ProductFilterServiceImpl.class);
	@Autowired
	private ProductLevelRepository productLevelRepository;
	
	@Autowired
	private AssignmentProductRepository assignmentProductRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private SignalDetectionRepository signalDetectionRepository;
	
	@Autowired
	private RiskPlanRepository riskPlanRepository;
	
	@Autowired
	private AssessmentPlanRepository assessmentPlanRepository;
	public void productLevelFilter(List<FilterDTO> filterList, String type) {
		List<Map<String, List<Levels>>> prlevels=null;
		List<ProductLevels> productLevels=null;
		try {
			prlevels = productLevelValuesCodes(type);
			productLevels = productLevelRepository.findAllByOrderByIdAsc();
			if(!AssignmentUtil.isNullOrEmpty(prlevels) && !AssignmentUtil.isNullOrEmpty(productLevels))
			{
				LOGGER.info("PRODUCT LEVEL MAP ==="+prlevels);
				LOGGER.info("PRODUCT REPO MAP ==="+productLevels);
				constructProductFilter(filterList, prlevels, productLevels);
			}
			
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private void constructProductFilter(List<FilterDTO> filterList, List<Map<String, List<Levels>>> prlevels,
			List<ProductLevels> productLevels) {
		FilterDTO dto;
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

	private List<Map<String, List<Levels>>> productLevelValuesCodes(String type) {
		List<Map<String, List<Levels>>> levelMapList = null;
		List<String> prodFillVals =null;
		switch (type) {
		case "signal":
			prodFillVals = topicRepository.getProductFilterValues();
			break;
		case "detection":
			prodFillVals = signalDetectionRepository.getProductFilterValues();
			break;
		case "risk":
		case "assessment":	
			prodFillVals = getRiskProductFilterValues(type);
			break;
		default:
			break;
		}
		LOGGER.info("TYPE-------> "+type+" ############>>>>>" + prodFillVals);
		if (!CollectionUtils.isEmpty(prodFillVals)) {
			levelMapList = new ArrayList<>();
			Map<Integer, Set<String>> itemMap = splitRecordValues(prodFillVals);
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

	private List<String> getRiskProductFilterValues(String type) {
		List<String>  prds=null;
		Set<Long> signalIds;
		if("risk".equalsIgnoreCase(type))
			signalIds = riskPlanRepository.findAllSignalIds();
		else
			signalIds = assessmentPlanRepository.findAllSignalIds();
		LOGGER.info("LIST OF SIGNAL IDS...."+signalIds);
		
		if(!CollectionUtils.isEmpty(signalIds))
			prds = topicRepository.getListOfProductRecordKeys(signalIds) ; 
		
		LOGGER.info("LIST OF SIGNAL KEYS IDS...."+prds);
		return prds;
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

	private List<Object[]> getCategoryCodes(Set<String> recValues) {
		return assignmentProductRepository.findDistinctByCategoryCodeIn(recValues);
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

	public <E> void getProductPredicate(Set<String> productSet, Join<E, E> joinProducts, String type) {
		List<String> prodFillVals = topicRepository.getProductFilterValues();
		
		if(!CollectionUtils.isEmpty(prodFillVals))
		{
			for (String setString : productSet) {
				for (String product : prodFillVals) {
					
				}
			}
				
				
				
				
				
			}
		}
			
				
		
		
		
	
}
