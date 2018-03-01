package com.deloitte.smt.servicetest;

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
import com.deloitte.smt.dao.PtDAO;
import com.deloitte.smt.dao.SmqDAO;
import com.deloitte.smt.service.SmqService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SmqServiceTest {
	
	@Autowired
	SmqService smqService;

	@MockBean
	SmqDAO smqDAO;
	
	@MockBean
	PtDAO ptDAO;
	
	@Test
	public void testFindAllSmqs() {
		try{
			smqService.findAllSmqs();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindPtsBySmqId() {
		try{
			List<Integer> ids = new ArrayList<>();
			ptDAO.findPtsBySmqId(ids);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
