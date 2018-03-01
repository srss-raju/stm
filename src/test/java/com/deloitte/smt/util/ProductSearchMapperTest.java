package com.deloitte.smt.util;

import java.sql.ResultSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.mapper.ProductSearchMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ProductSearchMapperTest {
	
	@MockBean
	ResultSet rs;
	
	@Test
	public void testProductHierarchyMapper() {
		ProductSearchMapper productSearchMapper = new ProductSearchMapper();
		try{
			productSearchMapper.mapRow(rs,1);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

}
