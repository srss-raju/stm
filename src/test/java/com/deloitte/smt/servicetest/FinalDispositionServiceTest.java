package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.FinalDispositions;
import com.deloitte.smt.repository.FinalDispositionRepository;
import com.deloitte.smt.service.FinalDispositionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class FinalDispositionServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private FinalDispositionService finalDispositionService;
	
	@MockBean
	FinalDispositionRepository finalDispositionRepository;
	
	@Test
	public void testInsert() throws Exception{
		List<FinalDispositions> finalDispositions = new ArrayList<>();
		FinalDispositions finalDisposition = new FinalDispositions();
		finalDispositions.add(finalDisposition);
		finalDispositionService.insert(finalDispositions);
	}
	
	@Test
	public void testUpdate() throws Exception{
		FinalDispositions finalDisposition = new FinalDispositions();
		finalDisposition.setId(1l);
		finalDispositionService.update(finalDisposition);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			FinalDispositions finalDisposition = new FinalDispositions();
			finalDispositionService.update(finalDisposition);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		FinalDispositions finalDisposition = new FinalDispositions();
		given(this.finalDispositionRepository.findOne(1l)).willReturn(finalDisposition);
		finalDispositionService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			finalDispositionService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		FinalDispositions finalDisposition = new FinalDispositions();
		given(this.finalDispositionRepository.findOne(1l)).willReturn(finalDisposition);
		finalDispositionService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			finalDispositionService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		finalDispositionService.findAll();
	}
	
}
