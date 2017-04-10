package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.service.AssessmentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class AssessmentController {

    @Autowired
    AssessmentPlanService assessmentPlanService;


    @GetMapping(value = "/allAssessmentPlans")
    public List<AssessmentPlan> getAllAssessmentPlans(@RequestParam(value = "status", defaultValue = "ActionPlan") String status){
        return assessmentPlanService.findAllAssessmentPlansByStatus(status);
    }

    @GetMapping(value = "/assessmentPlan/{id}")
    public AssessmentPlan getAssessmentPlanById(@PathVariable Long id) {
        return assessmentPlanService.findById(id);
    }
}
