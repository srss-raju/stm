package com.deloitte.smt;

import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.service.SignalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignalManagmentProcessTests {
	
	@Autowired
	SignalService signalService;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testCounts(){
		Long validateCount = signalService.getValidateAndPrioritizeCount();
		Long assesmentCount = signalService.getAssessmentCount();
		Long riskCount = signalService.getRiskCount();
		System.out.println("validateCount  -->> "+validateCount);
		System.out.println("assesmentCount  -->> "+assesmentCount);
		System.out.println("riskCount  -->> "+riskCount);
	}
	
	@Test
	public void testGetAllByStatus(){
		List<Topic> list = signalService.findAllByStatus("",null, null);
		System.out.println("validateCount  -->> "+list);
	}

}
