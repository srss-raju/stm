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

import com.deloitte.smt.dto.ConditionResponse;
import com.deloitte.smt.dto.LevelsDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConditonService {
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	ConditionLevelRepository conditionLevelRepository;
	
	public ConditionResponse getAllConditionLevels(){
		
		String strVersions = null;
		ConditionResponse response = null;
		List<LevelsDTO> levelsList=null;
		try {
			response=new ConditionResponse();
			Map<String,String> versions=new HashMap<>();
			List<ConditionLevels> conditionLevelList=conditionLevelRepository.findAllByOrderByIdAsc();
			ObjectMapper mapper = new ObjectMapper();
			levelsList =new ArrayList<>();
			if (!CollectionUtils.isEmpty(conditionLevelList)) {
				for (ConditionLevels condLevel : conditionLevelList) {
					LevelsDTO level=new LevelsDTO();
					level.setKey(condLevel.getKey());
					level.setValue(condLevel.getValue());
					levelsList.add(level);
				}
				ConditionLevels cLevel = conditionLevelList.get(0);
				versions.put("versionNumber", cLevel.getVersions());
				response.setShowCodes(cLevel.isShowCodes());
			}
			strVersions = mapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		response.setLevels(levelsList);
		response.setVersions(Arrays.asList(strVersions));
		return response;
		
		/*
		ConditionResponse response=new ConditionResponse();
		List<ConditionLevels> conditionLevelList=conditionLevelRepository.findAllByOrderByIdAsc();
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
		
		List<Versions> versionsList=new ArrayList<>();
		Versions version=new Versions();
		
		for(String versionNum:conditionVersions){
			version.setVersionNumber(versionNum);
		}
		versionsList.add(version);
		response.setVersions(versionsList);
		response.setLevels(levelsList);
		
		return response;
		*/
	}

	public void updateShowCodes(ConditionResponse conditionResponse) {
		try{
				ConditionLevels prl= conditionLevelRepository.findOne(1L);
				prl.setShowCodes(conditionResponse.isShowCodes());
				conditionLevelRepository.save(prl);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
	
	

}
