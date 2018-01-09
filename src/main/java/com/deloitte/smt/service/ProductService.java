package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.ProductResponse;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.util.Levels;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductService {
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	ProductLevelRepository productLevelRepository;
	
	public ProductResponse getAllProductsLevel(){
		String strVersions = null;
		ProductResponse response = null;
		List<Levels> levelsList=null;
		try {
			response=new ProductResponse();
			Map<String,String> versions=new HashMap<>();
			List<ProductLevels> productLevelList=productLevelRepository.findAllByOrderByIdAsc();
			ObjectMapper mapper = new ObjectMapper();
			levelsList =new ArrayList<>();
			if (!CollectionUtils.isEmpty(productLevelList)) {
				for (ProductLevels productlevel : productLevelList) {
					Levels level=new Levels();
					level.setKey(productlevel.getKey());
					level.setValue(productlevel.getValue());
					levelsList.add(level);
				}
				ProductLevels plevel = productLevelList.get(0);
				versions.put("versionNumber", plevel.getVersions());
				response.setShowCodes(plevel.isShowCodes());
			}
			strVersions = mapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		response.setLevels(levelsList);
		response.setVersions(Arrays.asList(strVersions));
		
		return response;
	}

	public void updateShowCodes(ProductResponse productResponse) {
		try {
			ProductLevels prl= productLevelRepository.findOne(1L);
			prl.setShowCodes(productResponse.isShowCodes());
			productLevelRepository.save(prl);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	

}
