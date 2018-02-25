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
import com.deloitte.smt.dto.ProductSearchDTO;
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
    		medraBrowserDto.setSearchValue("A");
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
	public void testResponseMapper() {
    	try{
    		List<ProductHierarchyDto> productHierarchyList = new ArrayList<>();
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
    		
    		productHierarchyList.add(productHierarchyDto);
    		
    		List<ProductLevels> prl = new ArrayList<>();
    		ProductLevels productLevel1 = new ProductLevels();
    		productLevel1.setKey("ATC_LVL_1");
    		productLevel1.setValue("A");
    		prl.add(productLevel1);
    		
    		ProductLevels productLevel2 = new ProductLevels();
    		productLevel2.setKey("ATC_LVL_2");
    		productLevel2.setValue("A");
    		prl.add(productLevel2);
    		
    		ProductLevels productLevel3 = new ProductLevels();
    		productLevel3.setKey("ATC_LVL_3");
    		productLevel3.setValue("A");
    		prl.add(productLevel3);
    		
    		ProductLevels productLevel4 = new ProductLevels();
    		productLevel4.setKey("ATC_LVL_4");
    		productLevel4.setValue("A");
    		prl.add(productLevel4);
    		
    		ProductLevels productLevel5 = new ProductLevels();
    		productLevel5.setKey("ATC_LVL_5");
    		productLevel5.setValue("A");
    		prl.add(productLevel5);
    		
    		ProductLevels rxNorm = new ProductLevels();
    		rxNorm.setKey("RXNORM");
    		rxNorm.setValue("A");
    		prl.add(rxNorm);
    		given(this.productLevelRepository.findAll()).willReturn(prl);
    		productHierarchyService.responseMapper(productHierarchyList);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextAll() {
    	try{
    		List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
    		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
    		productSearchDTOList.add(productSearchDTO);
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ALL");
    		
    		ProductLevels prl= new ProductLevels();
    		prl.setKey("ALL");
    		given(this.productMedraHierarchyDAO.findByActLvelOne(medraBrowserDto.getSearchValue(),0,10)).willReturn(productSearchDTOList);
    		given(this.productLevelRepository.findByKey("ALL")).willReturn(prl);
    		
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	
	@Test
	public void testGetDetailsBySearchTextL1() {
    	try{
    		List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
    		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
    		productSearchDTOList.add(productSearchDTO);
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_1");
    		
    		ProductLevels prl= new ProductLevels();
    		prl.setKey("ATC_LVL_1");
    		given(this.productMedraHierarchyDAO.findByActLvelOne(medraBrowserDto.getSearchValue(),0,10)).willReturn(productSearchDTOList);
    		given(this.productLevelRepository.findByKey("ATC_LVL_1")).willReturn(prl);
    		
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextL2() {
    	try{
    		List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
    		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
    		productSearchDTOList.add(productSearchDTO);
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_2");
    		
    		ProductLevels prl= new ProductLevels();
    		prl.setKey("ATC_LVL_2");
    		given(this.productMedraHierarchyDAO.findByActLvelTwo(medraBrowserDto.getSearchValue(),0,10)).willReturn(productSearchDTOList);
    		given(this.productLevelRepository.findByKey("ATC_LVL_2")).willReturn(prl);
    		
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextL3() {
    	try{
    		List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
    		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
    		productSearchDTOList.add(productSearchDTO);
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_3");
    		
    		ProductLevels prl= new ProductLevels();
    		prl.setKey("ATC_LVL_3");
    		given(this.productMedraHierarchyDAO.findByActLvelThree(medraBrowserDto.getSearchValue(),0,10)).willReturn(productSearchDTOList);
    		given(this.productLevelRepository.findByKey("ATC_LVL_3")).willReturn(prl);
    		
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextL4() {
    	try{
    		List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
    		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
    		productSearchDTOList.add(productSearchDTO);
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_4");
    		
    		ProductLevels prl= new ProductLevels();
    		prl.setKey("ATC_LVL_4");
    		given(this.productMedraHierarchyDAO.findByActLvelFour(medraBrowserDto.getSearchValue(),0,10)).willReturn(productSearchDTOList);
    		given(this.productLevelRepository.findByKey("ATC_LVL_4")).willReturn(prl);
    		
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testGetDetailsBySearchTextL5() {
    	try{
    		List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
    		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
    		productSearchDTOList.add(productSearchDTO);
    		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
    		medraBrowserDto.setSearchLevel("ATC_LVL_5");
    		
    		ProductLevels prl= new ProductLevels();
    		prl.setKey("ATC_LVL_5");
    		given(this.productMedraHierarchyDAO.findByActLvelFive(medraBrowserDto.getSearchValue(),0,10)).willReturn(productSearchDTOList);
    		given(this.productLevelRepository.findByKey("ATC_LVL_5")).willReturn(prl);
    		
    		productHierarchyService.getDetailsBySearchText(medraBrowserDto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

}
