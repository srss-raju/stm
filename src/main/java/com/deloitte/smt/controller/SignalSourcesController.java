package com.deloitte.smt.controller;

import com.deloitte.smt.entity.SignalSources;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SignalSourcesService;
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
@RequestMapping("/camunda/api/signal/sources")
public class SignalSourcesController {

    @Autowired
    SignalSourcesService signalSourcesService;

    @PostMapping
    public List<SignalSources> createNewAssessmentActionType(@RequestBody List<SignalSources> signalSources) {
        return signalSourcesService.insert(signalSources);
    }

    @PutMapping
    public SignalSources updateAssessmentActionType(@RequestBody SignalSources signalSource) throws UpdateFailedException {
        signalSource = signalSourcesService.update(signalSource);
        return signalSource;
    }

    @DeleteMapping(value = "/{signalSourceId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long signalSourceId) throws EntityNotFoundException {
        signalSourcesService.delete(signalSourceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{signalSourceId}")
    public SignalSources findAssessmentActionTypeById(@PathVariable Long signalSourceId) throws EntityNotFoundException {
        return signalSourcesService.findById(signalSourceId);
    }

    @GetMapping
    public List<SignalSources> findAll() {
        return signalSourcesService.findAll();
    }
}
