package com.deloitte.smt.controller;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.AssessmentActionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class AssessmentActionController {

    @Autowired
    AssessmentActionService assessmentActionService;

    @PostMapping(value = "/createAssessmentAction")
    public ResponseEntity<Void> createAssessmentAction(@RequestBody SignalAction signalAction) {
        assessmentActionService.createAssessmentAction(signalAction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/updateAssessmentAction")
    public ResponseEntity<Void> updateAssessmentAction(@RequestBody SignalAction signalAction) throws UpdateFailedException {
        assessmentActionService.updateAssessmentAction(signalAction);
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

    @DeleteMapping(value = "/assessmentAction/{assessmentActionId}//{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long assessmentActionId, @PathVariable String taskId) throws DeleteFailedException {
        assessmentActionService.delete(assessmentActionId, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
