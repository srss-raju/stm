package com.deloitte.smt.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.LevelsDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ProductUtilTest {

	@MockBean
	ResultSet rs;
	
	@Test
	public void testCreateProductValues() {
		ProductUtil productUtil = new ProductUtil();
		try{
			List<Map<String, List<LevelsDTO>>> levelMapList = new ArrayList<>(); 
			List<Object[]> assignmentProductList = new ArrayList<>();
			String myArray[] = { "one", "two", "three" };
			assignmentProductList.add(myArray);
			productUtil.createProductValues(levelMapList, assignmentProductList);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void testSplitRecordValues() {
		ProductUtil productUtil = new ProductUtil();
		try{
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			productUtil.splitRecordValues(prodFillVals);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
