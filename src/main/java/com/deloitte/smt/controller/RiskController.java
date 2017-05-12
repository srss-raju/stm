package com.deloitte.smt.controller;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.RiskPlanService;
import com.deloitte.smt.util.StringConverterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public RiskPlan createRiskPlan(@RequestParam("data") String riskPlanString,
                                               @RequestParam(value = "assessmentId", required = false) Long assessmentId,
                                               @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException, EntityNotFoundException {
        RiskPlan riskPlan = new ObjectMapper().readValue(riskPlanString, RiskPlan.class);
        riskPlan = riskPlanService.insert(riskPlan, attachments, assessmentId);
        return riskPlan;
    }
    
    @GetMapping(value = "/allRiskPlans")
    public List<RiskPlan> getAllRiskPlans(@RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "product", required = false) String product,
                                          @RequestParam(value = "license", required = false) String license,
                                          @RequestParam(value = "ingredient", required = false) String ingredient,
                                          @RequestParam(name = "assignees", required = false) String assignees,
                                          @RequestParam(value = "createdDate", required = false) Date createdDate,
    									  @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
    									  @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
    									  @RequestParam(name = "isDueDate", required = false) boolean isDueDate){
        SearchDto searchDto = new SearchDto();
        if(StringUtils.isNotBlank(status)) {
            searchDto.setStatuses(StringConverterUtil.convertStringToList(status));
        }
        if(StringUtils.isNotBlank(product)) {
            searchDto.setProducts(StringConverterUtil.convertStringToList(product));
        }
        if(StringUtils.isNotBlank(license)) {
            searchDto.setLicenses(StringConverterUtil.convertStringToList(license));
        }
        if(StringUtils.isNotBlank(ingredient)) {
            searchDto.setIngredients(StringConverterUtil.convertStringToList(ingredient));
        }
        if(StringUtils.isNotBlank(assignees)) {
        	searchDto.setAssignees(StringConverterUtil.convertStringToList(assignees));
		}
        if(null != startDate){
        	searchDto.setStartDate(startDate);
        	searchDto.setEndDate(endDate);
        	searchDto.setDueDate(isDueDate);
		}
        return riskPlanService.findAllRiskPlansForSearch(searchDto);
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
