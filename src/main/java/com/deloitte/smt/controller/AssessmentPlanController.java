package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AssessmentPlanService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by RajeshKumar on 10-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class AssessmentPlanController {
	
	private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    AssessmentPlanService assessmentPlanService;

    @GetMapping(value = "/assessmentPlan/{id}")
    public AssessmentPlan getAssessmentPlanById(@PathVariable Long id) throws ApplicationException {
        return assessmentPlanService.findById(id);
    }

    @GetMapping(value = "/{assessmentId}/allTopics")
    public Set<Topic> getAllSignalsByAssessmentId(@PathVariable Long assessmentId) throws ApplicationException {
        return assessmentPlanService.findById(assessmentId).getTopics();
    }

    @PutMapping(value = "/unlink/{assessmentId}/{topicId}")
    public ResponseEntity<Void> unlinkSignal(@PathVariable Long assessmentId, @PathVariable Long topicId) {
        assessmentPlanService.unlinkSignalToAssessment(assessmentId, topicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/updateAssessment")
    public ResponseEntity<Void> updateAssessment(@RequestParam("data") String assessmentPlanString,
                                   @RequestParam(value = "attachments", required = false) MultipartFile[] attachments)  {
        AssessmentPlan assessmentPlan = null;
        try {
        	assessmentPlan = new ObjectMapper().readValue(assessmentPlanString, AssessmentPlan.class);
			assessmentPlanService.updateAssessment(assessmentPlan, attachments);
		} catch (ApplicationException | IOException e) {
			logger.info("Exception occured while updating "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/finalAssessment")
    public ResponseEntity<Void> finalAssessment(@RequestParam("data") String assessmentPlanString,
                                  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        AssessmentPlan assessmentPlan = null;
        try {
        	assessmentPlan = new ObjectMapper().readValue(assessmentPlanString, AssessmentPlan.class);
			assessmentPlanService.finalAssessment(assessmentPlan, attachments);
		} catch (ApplicationException | IOException e) {
			logger.info("Exception occured in finalAssessment "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping(value = "/assessmentPlan/getTemplates/{assessmentId}")
	public List<TaskTemplate> getTaskTamplatesOfAssessmentProducts(@PathVariable Long assessmentId) throws ApplicationException{
		return assessmentPlanService.getTaskTamplatesOfAssessmentProducts(assessmentId);
	}
    
    @GetMapping(value = "/{assessmentId}/updateAssessmentName/{assessmentName}")
	public ResponseEntity<Void> updateAssessmentName(@PathVariable Long assessmentId,@PathVariable String assessmentName ) throws ApplicationException{
		
		assessmentPlanService.updateAssessmentName(assessmentId, assessmentName);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
    
}
