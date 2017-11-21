package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
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

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.TaskTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/camunda/api/signal/template")
public class TaskTemplateController {
	private static final Logger LOG = Logger.getLogger(TaskTemplateController.class);
	
	@Autowired
	private TaskTemplateService taskTemplateService;
	
	@PostMapping(value = "/createTaskTemplate")
	public TaskTemplate createTaskTemplate(@RequestParam("data") String taskTemplateString, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
		
		TaskTemplate taskTemplate = null;
		try {
			taskTemplate = new ObjectMapper().readValue(taskTemplateString, TaskTemplate.class);
		} catch (IOException e) {
			LOG.info("Exception occured while creating "+e);
		}
		return taskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	@PostMapping(value = "/updateTaskTemplate")
	public TaskTemplate updateTaskTemplate(@RequestParam("data") String taskTemplateString, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
		TaskTemplate taskTemplate = new ObjectMapper().readValue(taskTemplateString, TaskTemplate.class);
		return taskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@DeleteMapping(value = "/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long taskId) throws ApplicationException {
		taskTemplateService.delete(taskId);
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
    public TaskTemplate findByTemplateId(@PathVariable Long templateId) throws ApplicationException {
        return taskTemplateService.findById(templateId);
    }
}
