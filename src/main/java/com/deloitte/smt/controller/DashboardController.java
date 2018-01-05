package com.deloitte.smt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SignalDetectDTO;
import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.dto.ValidationOutComesDTO;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.DashboardService;

@RestController
@RequestMapping(value = "/camunda/api/dashboard")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	
	@GetMapping(value = "/getSmtComplianceDetails")
	public Map<String, List<SmtComplianceDto>> getSmtComplianceDetails() {
		return dashboardService.getSmtComplianceDetails();
	}
	
	@GetMapping(value="/validationoutcomes")
	public List<ValidationOutComesDTO> getValidationOutcomes(){
		return dashboardService.generateDataForValidateOutcomesChart();
	}
	
	@GetMapping(value = "/getDetectedSignalDetails")
	public List<SignalDetectDTO> getDetectedSignalDetails() {
		return dashboardService.getDetectedSignalDetails();
	}

	
	@GetMapping(value = "/signalStrength")
	public Map<String, Object> getSignalStrength(@RequestParam("topicIds") List<Long> topicIds) throws ApplicationException {
		return dashboardService.getSignalStrength(topicIds);
	}

	
}
