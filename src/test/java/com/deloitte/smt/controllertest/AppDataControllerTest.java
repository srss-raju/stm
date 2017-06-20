package com.deloitte.smt.controllertest;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.AppDataDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppDataControllerTest {
	
	private static final Logger LOG = Logger.getLogger(AppDataControllerTest.class);

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testGetAppData() {

		HttpEntity<AppDataDTO> entity = new HttpEntity<AppDataDTO>(null, headers);

		ResponseEntity<AppDataDTO> response = 
				restTemplate.exchange(createURLWithPort("/camunda/api/appData/data"),HttpMethod.POST, entity, AppDataDTO.class);
		LOG.info(response);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}