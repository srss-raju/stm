package com.deloitte.smt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.RiskPlanService;

@RestController
@RequestMapping("/camunda/api/signal")
public class RiskNameUpdateController {
	@Autowired
	RiskPlanService riskPlanService;
	
	@GetMapping(value = "/{id}/updateRiskName/{riskName}")
	public ResponseEntity<Void> updateRiskName(@PathVariable Long id,
			@PathVariable String riskName) throws ApplicationException {

		riskPlanService.updateRiskName(id, riskName);

		return new ResponseEntity<>(HttpStatus.OK);

	}

}
