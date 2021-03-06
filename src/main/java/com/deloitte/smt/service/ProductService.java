package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
	private static final Logger LOGGER = Logger.getLogger(ProductService.class);
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
			LOGGER.error(e);
		}
		response.setLevels(levelsList);
		response.setVersions(Arrays.asList(strVersions));
		
		/*List<String> productVersions=productLevelRepository.findByVersions();
		ProductLevels  prlevel = productLevelRepository.findOne(1L);
		
		List<Levels> levelsList=new ArrayList<>();
		if(!CollectionUtils.isEmpty(productLevelList)){
			for(ProductLevels productlevel:productLevelList){
				Levels level=new Levels();
				level.setKey(productlevel.getKey());
				level.setValue(productlevel.getValue());
				levelsList.add(level);
			}
		}
		
		List<Versions> versionsList=new ArrayList<>();
		Versions version=new Versions();
		
		for(String versionNum:productVersions){
			version.setVersionNumber(versionNum);
		}
		versionsList.add(version);
		response.setVersions(versionsList);
		response.setLevels(levelsList);*/
		
		return response;
	}

	public void updateShowCodes(ProductResponse productResponse) {
		try {
			ProductLevels prl= productLevelRepository.findOne(1L);
			prl.setShowCodes(productResponse.isShowCodes());
			productLevelRepository.save(prl);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	

}
