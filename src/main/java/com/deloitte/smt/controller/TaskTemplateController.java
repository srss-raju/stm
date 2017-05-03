package com.deloitte.smt.controller;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.service.TaskTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/camunda/api/signal/template")
public class TaskTemplateController {
	
	@Autowired
	private TaskTemplateService taskTemplateService;
	
	@PostMapping(value = "/createTaskTemplate")
	public List<TaskTemplate> createTaskTemplate(@RequestParam("data") String taskTemplateString, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
		TaskTemplate taskTemplate = new ObjectMapper().readValue(taskTemplateString, TaskTemplate.class);
		return taskTemplateService.createTaskTemplate(taskTemplate, attachments);
	}
	
	@PostMapping(value = "/updateTaskTemplate")
	public List<TaskTemplate> updateTaskTemplate(@RequestParam("data") String taskTemplateString, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
		TaskTemplate taskTemplate = new ObjectMapper().readValue(taskTemplateString, TaskTemplate.class);
		return taskTemplateService.updateTaskTemplate(taskTemplate, attachments);
	}
	
	@DeleteMapping(value = "/{templateName}")
    public ResponseEntity<Void> deleteById(@PathVariable String templateName) throws DeleteFailedException {
		taskTemplateService.delete(templateName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(value = "/all")
    public List<TaskTemplate> findAll() {
        return taskTemplateService.findAll();
    }
	
	@GetMapping(value = "/signalAction/{templateId}")
    public List<SignalAction> findAllByTemplateId(@PathVariable Long templateId) {
        return taskTemplateService.findAllByTemplateId(templateId);
    }
	
	@GetMapping(value = "/{templateId}")
    public TaskTemplate findByTemplateId(@PathVariable Long templateId) throws EntityNotFoundException {
        return taskTemplateService.findById(templateId);
    }
}
