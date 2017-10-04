package com.deloitte.smt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.TaskTemplateService;

@RestController
@RequestMapping("/camunda/api/signal/template")
public class TaskTemplateNameUpdateController {
	
	@Autowired
	TaskTemplateService taskTemplateService;

	@GetMapping(value = "/{taskId}/updateTaskTemplateName/{name}")
	public ResponseEntity<Void> updateTaskTemplateName(@PathVariable Long taskId,@PathVariable String name ) throws ApplicationException{
		
		taskTemplateService.updateTaskTemplateName(taskId, name);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
}
