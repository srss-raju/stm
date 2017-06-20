package com.deloitte.smt.controllertest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.controller.AppDataController;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(AppDataController.class)
public class AppDataControllerTest {
	
	@Mock
    private HttpServletRequest context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testGetAppData() throws Exception {
		
		mockMvc.perform(post("/camunda/api/appData/data").content(TestUtil.convertObjectToJsonBytes("")).contentType(MediaType.APPLICATION_JSON_VALUE));
		
	}
}