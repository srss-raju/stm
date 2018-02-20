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
import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.service.ProductHierarchyService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ProductHierarchyServiceTest {
	
	@Autowired
	ProductHierarchyService productHierarchyService;
	
	@MockBean
	ProductMedraHierarchyDAO productMedraHierarchyDAO;

	@MockBean
	private ProductLevelRepository productLevelRepository;
	
	@Test
	public void testGetHierarchyByCode() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		List<ProductHierarchyDto> socHierarchyList = new ArrayList<>();
    		ProductHierarchyDto productHierarchyDto=new ProductHierarchyDto();
    		productHierarchyDto.setActLevelOneCode("ATC_LVL_1");
    		productHierarchyDto.setActLevelOneDesc("ATC_LVL_1_DESC");
    		
    		productHierarchyDto.setActLevelTwoCode("ATC_LVL_2");
    		productHierarchyDto.setActLevelTwoDesc("ATC_LVL_2_DEC");
    		
    		productHierarchyDto.setActLevelThreeCode("ATC_LVL_3");
    		productHierarchyDto.setActLevelThreeDesc("ATC_LVL_3_DESC");
    		
    		productHierarchyDto.setActLevelFourCode("ATC_LVL_4");
    		productHierarchyDto.setActLevelFourDesc("ATC_LVL_4_DESC");
    		
    		productHierarchyDto.setActLevelFiveCode("ATC_LVL_5");
    		productHierarchyDto.setActLevelFiveDesc("ATC_LVL_5_DESC");
    		
    		socHierarchyList.add(productHierarchyDto);
    		medraBrowserDto.setScrollCount(10);
    		medraBrowserDto.setScrollOffset(0);
    		medraBrowserDto.setSearchValue("10022891");
    		medraBrowserDto.setSelectLevel("SOC_CODE");
    		List<ProductLevels> levels = new ArrayList<>();
    		ProductLevels l1 = new ProductLevels();
    		l1.setKey("ATC_LVL_1");
    		l1.setValue("A");
    		ProductLevels l2 = new ProductLevels();
    		l2.setKey("ATC_LVL_2");
    		l2.setValue("AA");
    		ProductLevels l3 = new ProductLevels();
    		l3.setKey("ATC_LVL_3");
    		l3.setValue("AAA");
    		ProductLevels l4 = new ProductLevels();
    		l4.setKey("ATC_LVL_4");
    		l4.setValue("AAAA");
    		ProductLevels l5 = new ProductLevels();
    		l5.setKey("ATC_LVL_5");
    		l5.setValue("AAAAA");
    		levels.add(l1);
    		levels.add(l2);
    		levels.add(l3);
    		levels.add(l4);
    		levels.add(l5);
    		
    		given(this.productLevelRepository.findAll()).willReturn(levels);
    		given(this.productMedraHierarchyDAO.findAllByActLvel("A","ATC_LVL_1",0,10)).willReturn(socHierarchyList);
    		productHierarchyService.getHierarchyByCode(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextAll() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ALL");
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextSoc() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_1");
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextHlgt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_2");
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextHlt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_3");
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextPt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_4");
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextllt() {
    	try{
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_5");
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

}
