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
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AssessmentActionService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class AssessmentActionController    {
	
	private static final Logger LOG = Logger.getLogger(AssessmentActionController.class);

    @Autowired
    AssessmentActionService assessmentActionService;

    @PostMapping(value = "/createAssessmentAction")
    public SignalAction createAssessmentAction(@RequestParam("data") String signalActionString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException, ApplicationException {
        SignalAction signalAction = new ObjectMapper().readValue(signalActionString, SignalAction.class);
        if(signalAction.getTemplateId() != 0){
        	signalAction = assessmentActionService.createOrphanAssessmentAction(signalAction, attachments);
        }else{
        	signalAction = assessmentActionService.createAssessmentAction(signalAction, attachments);
        }
        return signalAction;
    }

    @PostMapping(value = "/updateAssessmentAction")
    public ResponseEntity<Void> updateAssessmentAction(@RequestParam("data") String signalActionString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        SignalAction signalAction = null;
        try {
        	signalAction = new ObjectMapper().readValue(signalActionString, SignalAction.class);
			assessmentActionService.updateAssessmentAction(signalAction, attachments);
		} catch (ApplicationException | IOException e) {
			LOG.info("Exception occured while updating "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/assessmentAction/{assessmentActionId}")
    public SignalAction getAssessmentActionById(@PathVariable Long assessmentActionId){
        return assessmentActionService.findById(assessmentActionId);
    }

    @GetMapping(value = "/{assessmentId}/allAssessmentActions")
    public List<SignalAction> getAllByAssessmentId(@PathVariable String assessmentId,
                                                   @RequestParam(value = "status", required = false) String actionStatus) {
        return assessmentActionService.findAllByAssessmentId(assessmentId, actionStatus);
    }

    @DeleteMapping(value = "/assessmentAction/{assessmentActionId}/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long assessmentActionId, @PathVariable String taskId) throws ApplicationException {
        assessmentActionService.delete(assessmentActionId, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
