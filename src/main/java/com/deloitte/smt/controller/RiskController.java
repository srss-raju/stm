package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.RiskPlanService;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/risk")
public class RiskController {

    @Autowired
    RiskPlanService riskPlanService;

    @PostMapping()
    public String createRiskPlan(@RequestBody RiskPlan riskPlan) {
        riskPlanService.insert(riskPlan);
        return "Successfully Created";
    }
    
    @GetMapping(value = "/allRiskPlans")
    public List<RiskPlan> getAllRiskPlans(@RequestParam(value = "status", required = false) String status){
        return riskPlanService.findAllRiskPlansByStatus(status);
    }
    
    @PostMapping(value = "/task/create")
    public String createRiskAction(@RequestBody RiskTask riskTask) {
    	riskPlanService.createRiskTask(riskTask);
        return "Saved Successfully";
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
    public String deleteById(@PathVariable Long riskTaskId, @PathVariable String taskId) throws DeleteFailedException {
    	riskPlanService.delete(riskTaskId, taskId);
        return "Successfully Deleted";
    }
    
    @PutMapping(value = "/task/updateRiskTask")
    public String updateRiskTask(@RequestBody RiskTask riskTask) throws UpdateFailedException {
    	riskPlanService.updateRiskTask(riskTask);
        return "Successfully Updated";
    }
}
