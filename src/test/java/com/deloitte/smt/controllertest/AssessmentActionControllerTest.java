package com.deloitte.smt.controllertest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.service.TaskService;


/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentActionControllerTest {
	
	@MockBean
    private TaskService assessmentActionService;
	
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Test
	public void testCreateAssessmentAction() throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("data", "{  \"actionName\": \"Task100\",  \"actionDescription\": \"Task100 descriptions\",  \"actionNotes\": \"\",  \"createdBy\": \"Shilpa\",  \"actionType\": \"Meeting Task\",  \"actionStatus\": \"New\",  \"assessmentId\": 1446,  \"inDays\": 0,  \"templateId\": 0,  \"caseInstanceId\": \"28878\",  \"recipients\": null,  \"deletedAttachmentIds\": [],  \"id\": 1447,  \"fileMetadata\": {},\"assignTo\": \"Shilpa\",\"owner\": \"Shilpa\" }");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity( createURLWithPort("/camunda/api/signal/createAssessmentAction"), request , String.class );
		logger.info(response);
		
	}
	
	@Test
	public void testCreateAssessmentActionWithOrphan() throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("data", "{  \"actionName\": \"Task100\",  \"actionDescription\": \"Task100 descriptions\",  \"actionNotes\": \"\",  \"createdBy\": \"Shilpa\",  \"actionType\": \"Meeting Task\",  \"actionStatus\": \"New\",  \"assessmentId\": 1446,  \"inDays\": 0,  \"templateId\": 110,  \"caseInstanceId\": \"28878\",  \"recipients\": null,  \"deletedAttachmentIds\": [],  \"id\": 1447,  \"fileMetadata\": {},\"assignTo\": \"Shilpa\",\"owner\": \"Shilpa\" }");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity( createURLWithPort("/camunda/api/signal/createAssessmentAction"), request , String.class );
		logger.info(response);
		
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}