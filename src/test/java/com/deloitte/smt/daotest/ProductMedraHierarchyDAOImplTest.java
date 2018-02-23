package com.deloitte.smt.daotest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dao.impl.ProductMedraHierarchyDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ProductMedraHierarchyDAOImplTest {
	
	@Autowired
	ProductMedraHierarchyDAOImpl productMedraHierarchyDAOImpl;
	
	@MockBean
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Test
	public void testFindAllByActLvel() {
		try{
			productMedraHierarchyDAOImpl.findAllByActLvel("A","ATC_LVL_1", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByActLvelOne() {
		try{
			productMedraHierarchyDAOImpl.findByActLvelOne("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByActLvelTwo() {
		try{
			productMedraHierarchyDAOImpl.findByActLvelTwo("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByActLvelThree() {
		try{
			productMedraHierarchyDAOImpl.findByActLvelThree("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByActLvelFour() {
		try{
			productMedraHierarchyDAOImpl.findByActLvelFour("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByActLvelFive() {
		try{
			productMedraHierarchyDAOImpl.findByActLvelFive("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindByRxNorm() {
		try{
			productMedraHierarchyDAOImpl.findByRxNorm("A", 0, 10);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
