package com.deloitte.smt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.service.SignalService;

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
		Long assesmentCount = signalService.getAssesmentCount();
		Long riskCount = signalService.getRiskCount();
		System.out.println("validateCount  -->> "+validateCount);
		System.out.println("assesmentCount  -->> "+assesmentCount);
		System.out.println("riskCount  -->> "+riskCount);
	}

}
