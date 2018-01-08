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

import com.deloitte.smt.entity.Task;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class TaskController    {
	
	private static final Logger LOG = Logger.getLogger(TaskController.class);

    @Autowired
    TaskService assessmentActionService;

    @PostMapping(value = "/createAssessmentAction")
    public Task createAssessmentAction(@RequestParam("data") String signalActionString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
    	Task signalAction = null;
		try {
			signalAction = new ObjectMapper().readValue(signalActionString, Task.class);
			if (signalAction.getTemplateId() != 0) {
				signalAction = assessmentActionService.createOrphanAssessmentAction(signalAction, attachments);
			} else {
				signalAction = assessmentActionService.createAssessmentAction(signalAction, attachments);
			}
		} catch (IOException e) {
			LOG.info("Exception occured while updating " + e);
		}
        return signalAction;
    }

    @PostMapping(value = "/updateAssessmentAction")
    public ResponseEntity<Void> updateAssessmentAction(@RequestParam("data") String signalActionString,
                                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
        Task signalAction = null;
        try {
        	signalAction = new ObjectMapper().readValue(signalActionString, Task.class);
			assessmentActionService.updateAssessmentAction(signalAction, attachments);
		} catch (IOException e) {
			LOG.info("Exception occured while updating "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/assessmentAction/{assessmentActionId}")
    public Task getAssessmentActionById(@PathVariable Long assessmentActionId){
        return assessmentActionService.findById(assessmentActionId);
    }

    @GetMapping(value = "/{assessmentId}/allAssessmentActions")
    public List<Task> getAllByAssessmentId(@PathVariable String assessmentId,
                                                   @RequestParam(value = "status", required = false) String actionStatus) {
        return assessmentActionService.findAllByAssessmentId(assessmentId, actionStatus);
    }

    @DeleteMapping(value = "/assessmentAction/{assessmentActionId}/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long assessmentActionId, @PathVariable String taskId) throws ApplicationException {
        assessmentActionService.delete(assessmentActionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
