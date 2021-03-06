package com.deloitte.smt.controllertest;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.junit.AfterClass;
import org.junit.Rule;
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
import com.deloitte.smt.service.AssessmentActionService;


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
    private AssessmentActionService assessmentActionService;
	
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();
	
	private static final Logger LOG = Logger.getLogger(AssessmentActionControllerTest.class);
	
	private static final ProcessEngineConfiguration processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration() {
	    {
	      jobExecutorActivate = false;
	      expressionManager = new MockExpressionManager();
	      databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
	    }
	  };
	  
	  private static final ProcessEngine PROCESS_ENGINE_NEEDS_CLOSE = processEngineConfiguration.buildProcessEngine();
	  
	  @Rule
	  public final ProcessEngineRule processEngine = new ProcessEngineRule(PROCESS_ENGINE_NEEDS_CLOSE);

	  @AfterClass
	  public static void shutdown() {
	    PROCESS_ENGINE_NEEDS_CLOSE.close();
	  }

	  

	@Test
	public void testCreateAssessmentAction() throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("data", "{  \"actionName\": \"Task100\",  \"actionDescription\": \"Task100 descriptions\",  \"actionNotes\": \"\",  \"createdBy\": \"Shilpa\",  \"actionType\": \"Meeting Task\",  \"actionStatus\": \"New\",  \"assessmentId\": 1446,  \"inDays\": 0,  \"templateId\": 0,  \"caseInstanceId\": \"28878\",  \"recipients\": null,  \"deletedAttachmentIds\": [],  \"id\": 1447,  \"fileMetadata\": {},\"assignTo\": \"Shilpa\",\"owner\": \"Shilpa\" }");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity( createURLWithPort("/camunda/api/signal/createAssessmentAction"), request , String.class );
		LOG.info(response);
		
	}
	
	@Test
	public void testCreateAssessmentActionWithOrphan() throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("data", "{  \"actionName\": \"Task100\",  \"actionDescription\": \"Task100 descriptions\",  \"actionNotes\": \"\",  \"createdBy\": \"Shilpa\",  \"actionType\": \"Meeting Task\",  \"actionStatus\": \"New\",  \"assessmentId\": 1446,  \"inDays\": 0,  \"templateId\": 110,  \"caseInstanceId\": \"28878\",  \"recipients\": null,  \"deletedAttachmentIds\": [],  \"id\": 1447,  \"fileMetadata\": {},\"assignTo\": \"Shilpa\",\"owner\": \"Shilpa\" }");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity( createURLWithPort("/camunda/api/signal/createAssessmentAction"), request , String.class );
		LOG.info(response);
		
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}