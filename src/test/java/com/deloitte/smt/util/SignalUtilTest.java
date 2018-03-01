package com.deloitte.smt.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.ProductHierarchyDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalUtilTest {

	@Test
	public void testGetCounts() {
		try{
			SignalUtil.getCounts(1l, 2l, 3l);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testConvertStringToDate() {
		try{
			String date="31/12/1998";  
			SignalUtil.convertStringToDate(date);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testConverToJson() {
		try{
			ProductHierarchyDto dto = new ProductHierarchyDto();
			SignalUtil.converToJson(dto);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testConverToJsonNull() {
		try{
			SignalUtil.converToJson(null);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
