package com.deloitte.smt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AssessmentPlanService;

@RestController
@RequestMapping("/camunda/api/signal")
public class AssessmentNameUpdateController {
	
	@Autowired
	AssessmentPlanService assessmentPlanService;
	
	@GetMapping(value = "/{assessmentId}/updateAssessmentName/{assessmentName}")
	public ResponseEntity<Void> updateAssessmentName(@PathVariable Long assessmentId,@PathVariable String assessmentName ) throws ApplicationException{
		
		assessmentPlanService.updateAssessmentName(assessmentId, assessmentName);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}

}
