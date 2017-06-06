package com.deloitte.smt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.service.ChartsService;

@RestController
@RequestMapping(value = "/camunda/api/signal/charts")
public class ChartsController {
	
	@Autowired
	private ChartsService chartsService;
	
	@GetMapping(value = "/getSmtComplianceDetails")
	public Map<String, List<SmtComplianceDto>> getSmtComplianceDetails() {
		return chartsService.getSmtComplianceDetails();
	}

}
