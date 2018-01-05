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

import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.RiskTaskTemplate;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.RiskTaskTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/camunda/api/signal/risktemplate")
public class RiskTaskTemplateController {
	
	private static final Logger LOG = Logger.getLogger(RiskTaskTemplateController.class);
	
	@Autowired
	private RiskTaskTemplateService riskTaskTemplateService;
	
	@PostMapping(value = "/createTaskTemplate")
	public RiskTaskTemplate createTaskTemplate(@RequestParam("data") String taskTemplateString, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
		
		RiskTaskTemplate taskTemplate = null;
		try {
			taskTemplate = new ObjectMapper().readValue(taskTemplateString, RiskTaskTemplate.class);
		} catch (IOException e) {
			LOG.info("Exception occured while updating "+e);
		}
		return riskTaskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	@PostMapping(value = "/updateTaskTemplate")
	public RiskTaskTemplate updateTaskTemplate(@RequestParam("data") String taskTemplateString, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
		
		RiskTaskTemplate taskTemplate = null;
		try {
			taskTemplate = new ObjectMapper().readValue(taskTemplateString, RiskTaskTemplate.class);
		} catch (IOException e) {
			LOG.info("Exception occured while updating "+e);
		}
		return riskTaskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@DeleteMapping(value = "/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long taskId) throws ApplicationException {
		riskTaskTemplateService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(value = "/all")
    public List<RiskTaskTemplate> findAll() {
        return riskTaskTemplateService.findAll();
    }
	
	@GetMapping(value = "/risk/{templateId}")
    public List<RiskTask> findAllByTemplateId(@PathVariable Long templateId) {
        return riskTaskTemplateService.findAllByTemplateId(templateId);
    }
	
	@GetMapping(value = "/{templateId}")
    public RiskTaskTemplate findByTemplateId(@PathVariable Long templateId) throws ApplicationException {
        return riskTaskTemplateService.findById(templateId);
    }
	
}
