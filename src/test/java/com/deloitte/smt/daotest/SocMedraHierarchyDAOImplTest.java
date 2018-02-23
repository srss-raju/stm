package com.deloitte.smt.daotest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dao.impl.SocMedraHierarchyDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SocMedraHierarchyDAOImplTest {
	
	@Autowired
	SocMedraHierarchyDAOImpl socMedraHierarchyDAOImpl ;
	
	@Test
	public void testFindAllByConditionName() {
		try{
			socMedraHierarchyDAOImpl.findAllByConditionName("A","ATC_LVL_1", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByMatchingSocName() {
		try{
			socMedraHierarchyDAOImpl.findByMatchingSocName("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByHlgtName() {
		try{
			socMedraHierarchyDAOImpl.findByHlgtName("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByPtName() {
		try{
			socMedraHierarchyDAOImpl.findByPtName("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByLltName() {
		try{
			socMedraHierarchyDAOImpl.findByLltName("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}
