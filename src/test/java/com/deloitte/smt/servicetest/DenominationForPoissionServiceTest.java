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
import com.deloitte.smt.entity.DenominatorForPoission;
import com.deloitte.smt.repository.DenominatorForPoissionRepository;
import com.deloitte.smt.service.DenominationForPoissionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DenominationForPoissionServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private DenominationForPoissionService denominationForPoissionService;
	
	@MockBean
	DenominatorForPoissionRepository denominationForPoissionRespository;
	
	@Test
	public void testInsert() throws Exception{
		List<DenominatorForPoission> denominatorForPoissions = new ArrayList<>();
		DenominatorForPoission denominatorForPoisson = new DenominatorForPoission();
		denominatorForPoissions.add(denominatorForPoisson);
		denominationForPoissionService.insert(denominatorForPoissions);
	}
	
	@Test
	public void testUpdate() throws Exception{
		DenominatorForPoission denominatorForPoisson = new DenominatorForPoission();
		denominatorForPoisson.setId(1l);
		denominationForPoissionService.update(denominatorForPoisson);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			DenominatorForPoission denominatorForPoisson = new DenominatorForPoission();
			denominationForPoissionService.update(denominatorForPoisson);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		DenominatorForPoission denominatorForPoisson = new DenominatorForPoission();
		given(this.denominationForPoissionRespository.findOne(1l)).willReturn(denominatorForPoisson);
		denominationForPoissionService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			denominationForPoissionService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		DenominatorForPoission denominatorForPoisson = new DenominatorForPoission();
		given(this.denominationForPoissionRespository.findOne(1l)).willReturn(denominatorForPoisson);
		denominationForPoissionService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			denominationForPoissionService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		denominationForPoissionService.findAll();
	}
	
}
