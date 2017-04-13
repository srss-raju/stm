package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.AssessmentPlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public List<AssessmentPlan> getAllAssessmentPlans(@RequestParam(value = "status", required = false) String status){
        return assessmentPlanService.findAllAssessmentPlansByStatus(status);
    }

    @GetMapping(value = "/assessmentPlan/{id}")
    public AssessmentPlan getAssessmentPlanById(@PathVariable Long id) {
        return assessmentPlanService.findById(id);
    }

    @GetMapping(value = "/{assessmentId}/allTopics")
    public List<Topic> getAllSignalsByAssessmentId(@PathVariable Long assessmentId) {
        return assessmentPlanService.findById(assessmentId).getTopics();
    }


    @PostMapping(value = "/updateAssessment")
    public ResponseEntity<Void> updateAssessment(@RequestParam("data") String assessmentPlanString,
                                   @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws UpdateFailedException, IOException {
        AssessmentPlan assessmentPlan = new ObjectMapper().readValue(assessmentPlanString, AssessmentPlan.class);
        assessmentPlanService.updateAssessment(assessmentPlan, attachments);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/finalAssessment")
    public String finalAssessment(@RequestParam("data") String assessmentPlanString,
                                  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws UpdateFailedException, IOException {
        AssessmentPlan assessmentPlan = new ObjectMapper().readValue(assessmentPlanString, AssessmentPlan.class);
        assessmentPlanService.finalAssessment(assessmentPlan, attachments);
        return "Successfully Updated";
    }
}
