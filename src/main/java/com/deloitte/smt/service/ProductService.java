package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.ProductResponse;
import com.deloitte.smt.dto.Versions;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.util.Levels;

@Service
public class ProductService {
	
	@Autowired
	ProductLevelRepository productLevelRepository;
	
	public ProductResponse getAllProductsLevel(){
		ProductResponse response=new ProductResponse();
		List<ProductLevels> productLevelList=productLevelRepository.findAllByOrderByIdAsc();
		List<String> productVersions=productLevelRepository.findByVersions();
		
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
		response.setLevels(levelsList);
		
		return response;
	}
	

}
