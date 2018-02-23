package com.deloitte.smt.servicetest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.service.DashboardCountService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DashboardCountServiceTest {

	@Autowired
	DashboardCountService dashboardCountService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public void testGetValidateAndPrioritizeCount() {
		try{
			String owner = "R";
			List<Long> userKeys = new ArrayList<>();
			userKeys.add(1l);
			List<Long> userGroupKeys = new ArrayList<>();
			userGroupKeys.add(1l);
			dashboardCountService.getValidateAndPrioritizeCount(owner, userKeys, userGroupKeys);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetAssessmentCount() {
		try{
			String owner = "R";
			List<Long> userKeys = new ArrayList<>();
			userKeys.add(1l);
			List<Long> userGroupKeys = new ArrayList<>();
			userGroupKeys.add(1l);
			dashboardCountService.getAssessmentCount(owner, userKeys, userGroupKeys);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetRiskCount() {
		try{
			String owner = "R";
			List<Long> userKeys = new ArrayList<>();
			userKeys.add(1l);
			List<Long> userGroupKeys = new ArrayList<>();
			userGroupKeys.add(1l);
			dashboardCountService.getRiskCount(owner, userKeys, userGroupKeys);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
