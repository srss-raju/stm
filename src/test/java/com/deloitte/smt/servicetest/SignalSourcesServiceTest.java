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
import com.deloitte.smt.entity.SignalSources;
import com.deloitte.smt.repository.SignalSourcesRepository;
import com.deloitte.smt.service.SignalSourcesService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalSourcesServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SignalSourcesService signalSourcesService;
	
	@MockBean
	SignalSourcesRepository signalSourcesRepository;
    
	@Test
	public void testInsert() throws Exception{
		List<SignalSources> signalSources = new ArrayList<>();
		SignalSources signalSource = new SignalSources();
		signalSources.add(signalSource);
		signalSourcesService.insert(signalSources);
	}
	
	@Test
	public void testUpdate() throws Exception{
		SignalSources signalSource = new SignalSources();
		signalSource.setId(1l);
		signalSourcesService.update(signalSource);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			SignalSources signalSource = new SignalSources();
			signalSourcesService.update(signalSource);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
			SignalSources signalSource = new SignalSources();
			given(this.signalSourcesRepository.findOne(1l)).willReturn(signalSource);
			signalSourcesService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			signalSourcesService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		SignalSources signalSource = new SignalSources();
		given(this.signalSourcesRepository.findOne(1l)).willReturn(signalSource);
		signalSourcesService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			signalSourcesService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		signalSourcesService.findAll();
	}
	
}
