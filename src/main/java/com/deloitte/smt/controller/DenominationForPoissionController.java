package com.deloitte.smt.controller;

import java.util.List;

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

import com.deloitte.smt.entity.DenominationForPoission;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.DenominationForPoissionService;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/denomination")
public class DenominationForPoissionController {

    @Autowired
    DenominationForPoissionService denominationForPoissionService;

    @PostMapping
    public List<DenominationForPoission> createNewAssessmentActionType(@RequestBody List<DenominationForPoission> denominationForPoissions) {
    	denominationForPoissions = denominationForPoissionService.insert(denominationForPoissions);
        return denominationForPoissions;
    }

    @PutMapping
    public DenominationForPoission updateAssessmentActionType(@RequestBody DenominationForPoission denominationForPoission) throws UpdateFailedException {
    	denominationForPoission = denominationForPoissionService.update(denominationForPoission);
        return denominationForPoission;
    }

    @DeleteMapping(value = "/{denominationForPoissionId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long denominationForPoissionId) throws EntityNotFoundException {
    	denominationForPoissionService.delete(denominationForPoissionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{denominationForPoissionId}")
    public DenominationForPoission findAssessmentActionTypeById(@PathVariable Long denominationForPoissionId) throws EntityNotFoundException {
        return denominationForPoissionService.findById(denominationForPoissionId);
    }

    @GetMapping
    public List<DenominationForPoission> findAll() {
        return denominationForPoissionService.findAll();
    }
}
