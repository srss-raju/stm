package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.RiskPlanService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/risk")
public class RiskController {

    @Autowired
    RiskPlanService riskPlanService;

    @PostMapping()
    public ResponseEntity<Void> createRiskPlan(@RequestParam("data") String riskPlanString,
                                               @RequestParam("assessmentId") Long assessmentId,
                                               @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException, EntityNotFoundException {
        RiskPlan riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
        riskPlanService.insert(riskPlan, attachments, assessmentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping(value = "/allRiskPlans")
    public List<RiskPlan> getAllRiskPlans(@RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "createdDate", required = false) Date createdDate){
        return riskPlanService.findAllRiskPlansForSearch(status, createdDate);
    }
    
    @PostMapping(value = "/task/create")
    public ResponseEntity<Void> createRiskAction(@RequestParam("data") String riskPlanActionString,
                                                 @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
        RiskTask riskTask = new ObjectMapper().readValue(riskPlanActionString, RiskTask.class);
    	riskPlanService.createRiskTask(riskTask, attachments);
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
    public ResponseEntity<Void> deleteById(@PathVariable Long riskTaskId, @PathVariable String taskId) throws DeleteFailedException {
    	riskPlanService.delete(riskTaskId, taskId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /*@PutMapping(value = "/task/updateRiskTask")
    public ResponseEntity<Void> updateRiskTask(@RequestBody RiskTask riskTask) throws UpdateFailedException {
    	riskPlanService.updateRiskTask(riskTask);
    	return new ResponseEntity<>(HttpStatus.OK);
    }*/
    
    @PostMapping(value = "/task/updateRiskTask")
    public ResponseEntity<Void> updateRiskTask(@RequestParam("data") String riskTaskString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws UpdateFailedException, IOException {
    	RiskTask riskTask = new ObjectMapper().readValue(riskTaskString, RiskTask.class);
        riskPlanService.updateRiskTask(riskTask, attachments);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/{id}")
    public RiskPlan findByRiskId(@PathVariable Long id) throws EntityNotFoundException {
        return riskPlanService.findByRiskId(id);
    }
    
    @PostMapping(value = "/updateRiskPlan")
    public ResponseEntity<Void> updateRiskPlan(@RequestParam("data") String riskPlanString,
                                  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws UpdateFailedException, IOException {
        RiskPlan riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
        riskPlanService.updateRiskPlan(riskPlan, attachments);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/riskPlanSummary")
    public ResponseEntity<Void> riskPlanSummary(@RequestParam("data") String riskPlanString,
                                  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws UpdateFailedException, IOException {
        RiskPlan riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
        riskPlanService.riskPlanSummary(riskPlan, attachments);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{riskId}/assessmentPlan")
    public AssessmentPlan getAssessmentPlanByRiskId(@PathVariable Long riskId) throws EntityNotFoundException {
        return riskPlanService.findByRiskId(riskId).getAssessmentPlan();
    }
}
