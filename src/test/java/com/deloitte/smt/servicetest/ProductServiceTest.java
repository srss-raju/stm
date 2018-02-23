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
import com.deloitte.smt.dto.ProductResponse;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ProductServiceTest {
	
	@Autowired
	ProductService productService;
	
	@MockBean
	ProductLevelRepository productLevelRepository;

	
	@Test
	public void testGetSignalFilters() {
		try{
			List<ProductLevels> productLevelslList = new ArrayList<>();
			ProductLevels productLevel = new ProductLevels();
			productLevel.setVersions("1.2");
			productLevelslList.add(productLevel);
			given(this.productLevelRepository.findAllByOrderByIdAsc()).willReturn(productLevelslList);
			productService.getAllProductsLevel();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateShowCodes() {
		try{
			ProductLevels productLevel = new ProductLevels();
			productLevel.setVersions("1.2");
			ProductResponse productResponse = new ProductResponse();
			given(this.productLevelRepository.findOne(1l)).willReturn(productLevel);
			productService.updateShowCodes(productResponse);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateShowCodesNull() {
		try{
			ProductResponse productResponse = null;
			ProductLevels productLevel = null;
			given(this.productLevelRepository.findOne(1l)).willReturn(productLevel);
			productService.updateShowCodes(productResponse);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
