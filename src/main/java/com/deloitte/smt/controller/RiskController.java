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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.RiskPlanService;
import com.deloitte.smt.util.SmtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/risk")
public class RiskController {
	
	private static final Logger LOG = Logger.getLogger(RiskController.class);

    @Autowired
    RiskPlanService riskPlanService;

    @PostMapping()
    public RiskPlan createRiskPlan(@RequestParam("data") String riskPlanString,
                                               @RequestParam(value = "assessmentId", required = false) Long assessmentId,
                                               @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
    	RiskPlan riskPlan = null;
    	try {
        	riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
			riskPlan = riskPlanService.insert(riskPlan, attachments, assessmentId);
		} catch (IOException | ApplicationException e) {
			LOG.info("Exception occured while creating "+e);
			throw new ApplicationException(e.getMessage());
		}
        return riskPlan;
    }
    
    
    @PostMapping(value = "/search")
    public SmtResponse getAllRiskPlans(@RequestBody(required=false) SearchDto searchDto){
        return riskPlanService.findAllRiskPlansForSearch(searchDto);
    }
    
    @PostMapping(value = "/task/create")
    public ResponseEntity<Void> createRiskAction(@RequestParam("data") String riskPlanActionString,
                                                 @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
        RiskTask riskTask = null;
		try {
			riskTask = new ObjectMapper().readValue(riskPlanActionString, RiskTask.class);
			riskPlanService.createRiskTask(riskTask, attachments);
		} catch (IOException e) {
			LOG.info("Exception occured while creating "+e);
			throw new ApplicationException(e.getMessage());
		}
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping(value = "/task/{riskTaskId}")
    public RiskTask getAssessmentActionById(@PathVariable Long riskTaskId){
        return riskPlanService.findById(riskTaskId);
    }

    @GetMapping(value = "/task/{riskId}/allRiskTasks")
    public List<RiskTask> getAllByRiskId(@PathVariable String riskId,
                                                   @RequestParam(value = "status", required = false) String status) {
        return riskPlanService.findAllByRiskId(riskId, status);
    }

    @DeleteMapping(value = "/task/{riskTaskId}/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long riskTaskId, @PathVariable String taskId) throws ApplicationException {
    	riskPlanService.delete(riskTaskId, taskId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @PostMapping(value = "/task/updateRiskTask")
    public ResponseEntity<Void> updateRiskTask(@RequestParam("data") String riskTaskString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        try {
        	RiskTask riskTask = new ObjectMapper().readValue(riskTaskString, RiskTask.class);
			riskPlanService.updateRiskTask(riskTask, attachments);
		} catch (ApplicationException | IOException e) {
			LOG.info("Exception occured while updating "+e);
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
			LOG.info("Exception occured while updating "+e);
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
			LOG.info("Exception occured in riskPlanSummary "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{riskId}/assessmentPlan")
    public AssessmentPlan getAssessmentPlanByRiskId(@PathVariable Long riskId) throws ApplicationException {
        return riskPlanService.findByRiskId(riskId).getAssessmentPlan();
    }
    
    @PostMapping(value = "/associateRiskTemplateTasks")
    public List<RiskTask> associateRiskTemplateTasks(@RequestBody RiskPlan riskPlan) {
        return riskPlanService.associateRiskTemplateTasks(riskPlan);
    }
}
