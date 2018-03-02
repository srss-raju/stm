package com.deloitte.smt.daotest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dao.impl.PtDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class PtDAOImplTest {
	
	@Autowired
	PtDAOImpl ptDAOImpl ;
	
	@Test
	public void testFindPtsBySmqId() {
		try{
			List<Integer> ids = new ArrayList<>();
			ids.add(1);
			ptDAOImpl.findPtsBySmqId(ids);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}
