package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.ConditionResponse;
import com.deloitte.smt.dto.Versions;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.util.Levels;

@Service
public class ConditonService {
	
	@Autowired
	ConditionLevelRepository conditionLevelRepository;
	
	public ConditionResponse getAllConditionLevels(){
		ConditionResponse response=new ConditionResponse();
		List<ConditionLevels> conditionLevelList=conditionLevelRepository.findAll();
		List<String> conditionVersions=conditionLevelRepository.findByVersions();
		
		List<Levels> levelsList=new ArrayList<>();
		if(!CollectionUtils.isEmpty(conditionLevelList)){
			for(ConditionLevels conditionlevel:conditionLevelList){
				Levels level=new Levels();
				level.setKey(conditionlevel.getKey());
				level.setValue(conditionlevel.getValue());
				levelsList.add(level);
			}
		}
		
		Versions versions=new Versions();
		versions.setVersionNumber(conditionVersions);
		response.setVersions(versions);
		response.setLevels(levelsList);
		
		return response;
	}
	

}
