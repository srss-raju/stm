package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.TaskTemplateService;

@RestController
@RequestMapping("/camunda/api/signal/{type}/template")
public class TaskTemplateController {
	
	@Autowired
	private TaskTemplateService taskTemplateService;
	
	@PostMapping
	public TaskTemplate createTaskTemplate(@RequestBody TaskTemplate taskTemplate) throws ApplicationException {
		return taskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	@PutMapping
	public TaskTemplate updateTaskTemplate(@RequestBody TaskTemplate taskTemplate) throws ApplicationException {
		return taskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@DeleteMapping(value = "/{templateId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long templateId) throws ApplicationException {
		taskTemplateService.delete(templateId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping
    public List<TaskTemplate> findAll() {
        return taskTemplateService.findAll();
    }
	
	@GetMapping(value = "/{templateId}")
    public TaskTemplate findByTemplateId(@PathVariable Long templateId) throws ApplicationException {
        return taskTemplateService.findById(templateId);
    }
}
