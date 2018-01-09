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
import com.deloitte.smt.entity.ExternalDatasets;
import com.deloitte.smt.repository.ExternalDatasetsRepository;
import com.deloitte.smt.service.ExternalDatasetsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ExternalDatasetsServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ExternalDatasetsService externalDatasetsService;
	
	@MockBean
	ExternalDatasetsRepository externalDatasetsRepository;
	
	@Test
	public void testInsert() throws Exception{
		List<ExternalDatasets> externalDatasets = new ArrayList<>();
		ExternalDatasets externalDataset = new ExternalDatasets();
		externalDatasets.add(externalDataset);
		externalDatasetsService.insert(externalDatasets);
	}
	
	@Test
	public void testUpdate() throws Exception{
		ExternalDatasets externalDataset = new ExternalDatasets();
		externalDataset.setId(1l);
		externalDatasetsService.update(externalDataset);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			ExternalDatasets externalDataset = new ExternalDatasets();
			externalDatasetsService.update(externalDataset);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		ExternalDatasets externalDataset = new ExternalDatasets();
		given(this.externalDatasetsRepository.findOne(1l)).willReturn(externalDataset);
		externalDatasetsService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			externalDatasetsService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		ExternalDatasets externalDataset = new ExternalDatasets();
		given(this.externalDatasetsRepository.findOne(1l)).willReturn(externalDataset);
		externalDatasetsService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			externalDatasetsService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		externalDatasetsService.findAll();
	}
	
}
