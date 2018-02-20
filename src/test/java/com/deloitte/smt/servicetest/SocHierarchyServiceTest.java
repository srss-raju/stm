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
import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.service.SocHierarchyService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SocHierarchyServiceTest {
	
	@Autowired
	SocHierarchyService socHierarchyService;

	@MockBean
	SocMedraHierarchyDAO socMedraHierarchyDAO;

	@MockBean
	private ConditionLevelRepository conditionLevelRepository;
	
	@Test
	public void testGetHierarchyByCode() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		List<SocHierarchyDto> socHierarchyList = new ArrayList<>();
    		SocHierarchyDto socHierarchyDto=new SocHierarchyDto();
    		socHierarchyDto.setSoc_code("SOC");
    		socHierarchyDto.setSoc_desc("SOC DESC");
    		
    		socHierarchyDto.setHlgt_code("HLGT");
    		socHierarchyDto.setHlgt_desc("HLGT DESC");
    		
    		socHierarchyDto.setHlt_code("HLT");
    		socHierarchyDto.setHlt_desc("HLT DESC");
    		
    		socHierarchyDto.setLlt_code("LLT");
    		socHierarchyDto.setLlt_desc("LLT DESC");
    		
    		socHierarchyDto.setPt_code("PT");
    		socHierarchyDto.setPt_desc("PT DESC");
    		
    		socHierarchyDto.setMedra_version_number("1.2J");
    		socHierarchyDto.setIntl_soc_order(1l);
    		socHierarchyDto.setPrimary_soc_flag("Y");
    		socHierarchyList.add(socHierarchyDto);
    		medraBrowserDto.setScrollCount(10);
    		medraBrowserDto.setScrollOffset(0);
    		medraBrowserDto.setSearchValue("10022891");
    		medraBrowserDto.setSelectLevel("SOC_CODE");
    		List<ConditionLevels> levels = new ArrayList<>();
    		ConditionLevels soc = new ConditionLevels();
    		soc.setKey("SOC_CODE");
    		soc.setValue("SOC");
    		ConditionLevels hlgt = new ConditionLevels();
    		hlgt.setKey("HLGT_CODE");
    		hlgt.setValue("HLGT");
    		ConditionLevels hlt = new ConditionLevels();
    		hlt.setKey("HLT_CODE");
    		hlt.setValue("HLT");
    		ConditionLevels pt = new ConditionLevels();
    		pt.setKey("PT_CODE");
    		pt.setValue("PT");
    		ConditionLevels llt = new ConditionLevels();
    		llt.setKey("LLT_CODE");
    		llt.setValue("LLT");
    		levels.add(soc);
    		levels.add(hlgt);
    		levels.add(hlt);
    		levels.add(pt);
    		levels.add(llt);
    		
    		given(this.conditionLevelRepository.findAll()).willReturn(levels);
    		given(this.socMedraHierarchyDAO.findAllByConditionName("10022891","SOC_CODE",0,10)).willReturn(socHierarchyList);
    		socHierarchyService.getHierarchyByCode(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextAll() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ALL");
    		socHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextSoc() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("SOC_CODE");
    		socHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextHlgt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("HLGT_CODE");
    		socHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextHlt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("HLT_CODE");
    		socHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextPt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("PT_CODE");
    		socHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextllt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("LLT_CODE");
    		socHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
