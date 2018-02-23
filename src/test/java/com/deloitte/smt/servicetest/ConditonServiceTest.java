package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.ConditionResponse;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.service.ConditonService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ConditonServiceTest {
	
	@Autowired
	ConditonService conditonService;

	@MockBean
	ConditionLevelRepository conditionLevelRepository;
	
	@Test
	public void testGetSignalFilters() {
		try{
			List<ConditionLevels> conditionLevelList = new ArrayList<>();
			ConditionLevels conditionLevel = new ConditionLevels();
			conditionLevel.setVersions("1.2");
			conditionLevelList.add(conditionLevel);
			given(this.conditionLevelRepository.findAllByOrderByIdAsc()).willReturn(conditionLevelList);
			conditonService.getAllConditionLevels();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateShowCodes() {
		try{
			ConditionLevels conditionLevel = new ConditionLevels();
			conditionLevel.setVersions("1.2");
			ConditionResponse conditionResponse = new ConditionResponse();
			given(this.conditionLevelRepository.findOne(1l)).willReturn(conditionLevel);
			conditonService.updateShowCodes(conditionResponse);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateShowCodesNull() {
		try{
			ConditionResponse conditionResponse = null;
			ConditionLevels conditionLevel = null;
			given(this.conditionLevelRepository.findOne(1l)).willReturn(conditionLevel);
			conditonService.updateShowCodes(conditionResponse);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
