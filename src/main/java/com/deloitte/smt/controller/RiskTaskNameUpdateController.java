package com.deloitte.smt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.RiskTaskTemplateService;

@RestController
@RequestMapping("/camunda/api/signal/risktemplate")
public class RiskTaskNameUpdateController {
	
	@Autowired
	RiskTaskTemplateService riskTaskTemplateService;
	
	@GetMapping(value = "/{taskId}/updateRiskTaskName/{name}")
	public ResponseEntity<Void> updateRiskTaskName(@PathVariable Long taskId,@PathVariable String name ) throws ApplicationException{
		
		riskTaskTemplateService.updateRiskTaskName(taskId, name);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}

}
