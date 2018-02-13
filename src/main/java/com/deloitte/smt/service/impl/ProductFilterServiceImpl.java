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
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentProductValuesRepository;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.ProductUtil;

@Service
public class ProductFilterServiceImpl {
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ProductLevelRepository productLevelRepository;
	
	@Autowired
	private AssignmentProductValuesRepository assignmentProductValuesRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private SignalDetectionRepository signalDetectionRepository;
	
	@Autowired
	private RiskPlanRepository riskPlanRepository;
	
	@Autowired
	private AssessmentPlanRepository assessmentPlanRepository;
	public void productLevelFilter(List<FilterDTO> filterList, String type) {
		List<Map<String, List<LevelsDTO>>> prlevels=null;
		List<ProductLevels> productLevels=null;
		try {
			prlevels = productLevelValuesCodes(type);
			productLevels = productLevelRepository.findAllByOrderByIdAsc();
			logger.info("PRODUCT LEVEL MAP ==="+prlevels);
			logger.info("PRODUCT REPO MAP ==="+productLevels);
			if(!CollectionUtils.isEmpty(productLevels))
			{
			constructProductFilter(filterList, prlevels, productLevels);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void constructProductFilter(List<FilterDTO> filterList, List<Map<String, List<LevelsDTO>>> prlevels, List<ProductLevels> productLevels) {
			for (ProductLevels productLevel : productLevels) {
				setFilters(filterList, prlevels, productLevel);
			}
	}

	private void setFilters(List<FilterDTO> filterList,
			List<Map<String, List<LevelsDTO>>> prlevels,
			ProductLevels productLevel) {
		FilterDTO dto;
		dto = new FilterDTO();
		dto.setFilterKey(productLevel.getValue().replace(" ", ""));
		dto.setFilterName(productLevel.getValue());
		dto.setFilterType("product");
		boolean exists=false;
		List<?>	existList=null;
		if(!CollectionUtils.isEmpty(prlevels))
		{
			for (Map<String, List<LevelsDTO>> prlevelMap : prlevels) {
				if(prlevelMap.containsKey(productLevel.getKey()))
				{
					existList = prlevelMap.get(productLevel.getKey());
					exists=true;
					break;
				}
				else
					exists=false;
			}
		}
		logger.info("Exists...?"+productLevel.getKey()+"----"+exists);
		setFilterValues(dto, existList, exists);
		filterList.add(dto);
	}
	
	private void setFilterValues(FilterDTO dto, List<?> existList, boolean exists) {
		if (exists)
			dto.setFilterValues(existList);
		else
			dto.setFilterValues(new ArrayList<>());
	}

	private List<Map<String, List<LevelsDTO>>> productLevelValuesCodes(String type) {
		List<Map<String, List<LevelsDTO>>> levelMapList = null;
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
		logger.info("TYPE-------> "+type+" ############>>>>>" + prodFillVals);
		if (!CollectionUtils.isEmpty(prodFillVals)) {
			levelMapList = new ArrayList<>();
			ProductUtil productUtil = new ProductUtil();
			Map<Integer, Set<String>> itemMap = productUtil.splitRecordValues(prodFillVals);
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

	private List<String> getRiskProductFilterValues(String type) {
		List<String>  prds=null;
		Set<Long> signalIds;
		if("risk".equalsIgnoreCase(type))
			signalIds = riskPlanRepository.findAllSignalIds();
		else
			signalIds = assessmentPlanRepository.findAllSignalIds();
		logger.info("LIST OF SIGNAL IDS...."+signalIds);
		
		if(!CollectionUtils.isEmpty(signalIds))
			prds = topicRepository.getListOfProductRecordKeys(signalIds) ; 
		
		logger.info("LIST OF SIGNAL KEYS IDS...."+prds);
		return prds;
	}

	

	private List<Object[]> getCategoryCodes(Set<String> recValues) {
		return assignmentProductValuesRepository.findDistinctByCategoryCodeIn(recValues);
	}

	public void constructProductPredicate(Set<String> productSet, StringBuilder queryBuilder, String type, Map<String, Object> parameterMap) {
		Set<String> productRecKeys = new HashSet<>();
		List<String> prodFillVals = topicRepository.getProductFilterValues();
		for (String recKeys : prodFillVals) {
			for (String proSet : productSet) {
				List<String> list = Arrays.asList(recKeys.split("@#"));
				if (list.contains(proSet)) {
					productRecKeys.add(recKeys);
				}
			}
		}
		
		logger.info("FINAL LIST....."+productRecKeys);
		if(!CollectionUtils.isEmpty(productRecKeys))
		{
			switch (type) 
			{
				case "signal":
					queryBuilder.append(" and product.topicId=root.id ");
					break;
				case "detection":
					queryBuilder.append(" and product.detectionId=root.id ");
					break;
				case "risk":
				case "assessment":
					queryBuilder.append(" and product.topicId=topic.id ");
					break;
				default:
					break;
			}
			queryBuilder.append(" and product.recordKey in :prRecordKey");
			parameterMap.put("prRecordKey", productRecKeys);
		}
		
	}			
		
		
		
	
}
