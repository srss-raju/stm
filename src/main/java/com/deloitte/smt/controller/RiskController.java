package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.RiskPlanService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/risk")
public class RiskController {
	
	private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    RiskPlanService riskPlanService;

    @PostMapping()
    public RiskPlan createRiskPlan(@RequestParam("data") String riskPlanString,
                                               @RequestParam(value = "assessmentId", required = false) Long assessmentId,
                                               @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
    	RiskPlan riskPlan = null;
    	try {
        	riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
			riskPlan = riskPlanService.insert(riskPlan, attachments, assessmentId);
		} catch (Exception e) {
			logger.info("Exception occured while creating Risk Plan"+e);
		}
        return riskPlan;
    }
    
    @PostMapping(value = "/task/create")
    public ResponseEntity<Void> createRiskAction(@RequestParam("data") String riskPlanActionString,
                                                 @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException  {
        
    	try {
    		Task riskTask = new ObjectMapper().readValue(riskPlanActionString, Task.class);
    		if(riskTask.getTemplateId() != null){
    			riskPlanService.createRiskTemplateTask(riskTask, attachments);
    		}else{
    			riskPlanService.createRiskTask(riskTask, attachments);
    		}
        	
		} catch (IOException e) {
			logger.info("Exception occured while creating Risk Action "+e);
		}
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping(value = "/task/{riskTaskId}")
    public Task getAssessmentActionById(@PathVariable Long riskTaskId){
        return riskPlanService.findById(riskTaskId);
    }

    @GetMapping(value = "/task/{riskId}/allRiskTasks")
    public List<Task> getAllByRiskId(@PathVariable String riskId,
                                                   @RequestParam(value = "status", required = false) String status) {
        return riskPlanService.findAllByRiskId(riskId, status);
    }

    @DeleteMapping(value = "/task/{riskTaskId}/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long riskTaskId, @PathVariable String taskId) throws ApplicationException {
    	riskPlanService.delete(riskTaskId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @PostMapping(value = "/task/updateRiskTask")
    public ResponseEntity<Void> updateRiskTask(@RequestParam("data") String riskTaskString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
        try {
        	Task riskTask = new ObjectMapper().readValue(riskTaskString, Task.class);
			riskPlanService.updateRiskTask(riskTask, attachments);
		} catch (IOException e) {
			logger.info("Exception occured while updating "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/{id}")
    public RiskPlan findByRiskId(@PathVariable Long id) throws ApplicationException {
        return riskPlanService.findByRiskId(id);
    }
    
    @PostMapping(value = "/updateRiskPlan")
    public ResponseEntity<Void> updateRiskPlan(@RequestParam("data") String riskPlanString,
                                  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        try {
        	RiskPlan riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
			riskPlanService.updateRiskPlan(riskPlan, attachments);
		} catch (ApplicationException | IOException e) {
			logger.info("Exception occured while updating "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/riskPlanSummary")
    public ResponseEntity<Void> riskPlanSummary(@RequestParam("data") String riskPlanString,
                                  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        try {
        	RiskPlan riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
			riskPlanService.riskPlanSummary(riskPlan, attachments);
		} catch (ApplicationException | IOException e) {
			logger.info("Exception occured in riskPlanSummary "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{riskId}/assessmentPlan")
    public AssessmentPlan getAssessmentPlanByRiskId(@PathVariable Long riskId) throws ApplicationException {
        return riskPlanService.findByRiskId(riskId).getAssessmentPlan();
    }
    
    @PostMapping(value = "/associateRiskTemplateTasks")
    public List<Task> associateRiskTemplateTasks(@RequestBody RiskPlan riskPlan) {
        return riskPlanService.associateRiskTemplateTasks(riskPlan);
    }
    
    @GetMapping(value = "/getTemplates/{riskPlanId}")
   	public List<TaskTemplate> getTaskTamplatesOfRiskProducts(@PathVariable Long riskPlanId) throws ApplicationException{
   		return riskPlanService.getTaskTamplatesOfRiskProducts(riskPlanId);
   	}
    
}
