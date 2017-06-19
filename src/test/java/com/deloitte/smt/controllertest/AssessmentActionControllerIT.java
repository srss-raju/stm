package com.deloitte.smt.controllertest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.controller.AssessmentActionController;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.service.AssessmentActionService;
import com.deloitte.smt.util.TestUtil;

public class AssessmentActionControllerIT extends AbstractControllerTest {
	
	@Mock
    private AssessmentActionService assessmentActionService;
	
	@Mock
    private HttpServletRequest context;
	
	public Object getController() {
        return new AssessmentActionController();
    }
	
	private static final Logger LOG = Logger.getLogger(AssessmentActionControllerIT.class);

	@Test
	public void testCreateAssessmentAction() throws Exception{
		
		SignalAction action = TestUtil.buildSignalAction();
		MultipartFile[] attachments = null;
		
		given(this.assessmentActionService.createAssessmentAction(action, attachments)).willReturn(action);
		mockMvc.perform(post("/camunda/api/signal/createAssessmentAction").param("data", "{  \"actionName\": \"Task100\",  \"actionDescription\": \"Task100 descriptions\",  \"actionNotes\": \"\",  \"createdBy\": \"Shilpa\",  \"actionType\": \"Meeting Task\",  \"actionStatus\": \"New\",  \"assessmentId\": 1446,  \"inDays\": 0,  \"templateId\": 0,  \"caseInstanceId\": \"28878\",  \"recipients\": null,  \"deletedAttachmentIds\": [],  \"id\": 1447,  \"fileMetadata\": {},\"assignTo\": \"Shilpa\",\"owner\": \"Shilpa\" }").content(TestUtil.convertObjectToJsonBytes(action)).contentType(MediaType.APPLICATION_JSON_VALUE));
		LOG.info("completed");
		
	}
	
}