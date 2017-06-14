package com.deloitte.smt.controller;

import com.deloitte.smt.entity.SignalConfirmations;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SignalConfirmationsService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/confirmations")
public class SignalConfirmationsController {

    @Autowired
    SignalConfirmationsService signalConfirmationsService;

    @PostMapping
    public List<SignalConfirmations> createNewAssessmentActionType(@RequestBody List<SignalConfirmations> signalConfirmations) {
    	return signalConfirmationsService.insert(signalConfirmations);
    }

    @PutMapping
    public SignalConfirmations updateAssessmentActionType(@RequestBody SignalConfirmations signalConfirmation) throws UpdateFailedException {
        return signalConfirmationsService.update(signalConfirmation);
    }

    @DeleteMapping(value = "/{signalConfirmationId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long signalConfirmationId) throws EntityNotFoundException {
        signalConfirmationsService.delete(signalConfirmationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{signalConfirmationId}")
    public SignalConfirmations findAssessmentActionTypeById(@PathVariable Long signalConfirmationId) throws EntityNotFoundException {
        return signalConfirmationsService.findById(signalConfirmationId);
    }

    @GetMapping
    public List<SignalConfirmations> findAll() {
        return signalConfirmationsService.findAll();
    }
}
