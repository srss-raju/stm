package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.SignalAlgorithmDTO;
import com.deloitte.smt.service.ExportExcelService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ExportToExcelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ExportExcelService exportExcelServiceMock;
	
	ExportExcelService exportExcelServiceMock1;
	
	@Before
	public void setUp() {
		exportExcelServiceMock1 = mock(ExportExcelService.class);
	}
	
	@Test
	public void testCreateTask() throws Exception{
		try{
			List<SignalAlgorithmDTO> signalAlgorithmDtoList = new ArrayList<>();
			HSSFWorkbook workbook = new HSSFWorkbook();
			when(exportExcelServiceMock.writeExcel(signalAlgorithmDtoList,"ExportDetectionDetails.xls")).thenReturn(workbook);

			this.mockMvc
					.perform(post("/camunda/api/signal/exportExcel")
							.param("data",TestUtil.convertObjectToJsonString(signalAlgorithmDtoList))
							.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andExpect(status().isOk());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	}
