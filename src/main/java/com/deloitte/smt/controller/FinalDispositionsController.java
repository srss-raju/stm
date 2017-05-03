package com.deloitte.smt.controller;

import com.deloitte.smt.entity.FinalDispositions;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.FinalDispositionService;
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
@RequestMapping("/camunda/api/signal/finalDispositions")
public class FinalDispositionsController {

    @Autowired
    FinalDispositionService finalDispositionService;

    @PostMapping
    public List<FinalDispositions> createNewAssessmentActionType(@RequestBody List<FinalDispositions> finalDispositions) {
        finalDispositions = finalDispositionService.insert(finalDispositions);
        return finalDispositions;
    }

    @PutMapping
    public FinalDispositions updateAssessmentActionType(@RequestBody FinalDispositions finalDisposition) throws UpdateFailedException {
        finalDisposition = finalDispositionService.update(finalDisposition);
        return finalDisposition;
    }

    @DeleteMapping(value = "/{finalDispositionId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long finalDispositionId) throws EntityNotFoundException {
        finalDispositionService.delete(finalDispositionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{finalDispositionId}")
    public FinalDispositions findAssessmentActionTypeById(@PathVariable Long finalDispositionId) throws EntityNotFoundException {
        return finalDispositionService.findById(finalDispositionId);
    }

    @GetMapping
    public List<FinalDispositions> findAll() {
        return finalDispositionService.findAll();
    }
}
