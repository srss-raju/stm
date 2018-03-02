package com.deloitte.smt.servicetest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.service.FiltersService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class FiltersServiceTest {
	
	@Autowired
	FiltersService filtersService;

	
	@Test
	public void testGetSignalFilters() {
		try{
			filtersService.getFiltersByType("Signal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateShowCodes() {
		try{
			filtersService.getSignalDataByFilter("", null);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}
