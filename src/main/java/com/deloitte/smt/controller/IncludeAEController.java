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

import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.IncludeAEService;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/ae")
public class IncludeAEController {

    @Autowired
    IncludeAEService includeAEService;

    @PostMapping
    public List<IncludeAE> createNewAssessmentActionType(@RequestBody List<IncludeAE> includeAEs) {
    	includeAEs = includeAEService.insert(includeAEs);
        return includeAEs;
    }

    @PutMapping
    public IncludeAE updateAssessmentActionType(@RequestBody IncludeAE includeAE) throws UpdateFailedException {
    	includeAE = includeAEService.update(includeAE);
        return includeAE;
    }

    @DeleteMapping(value = "/{includeAEId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long includeAEId) throws EntityNotFoundException {
    	includeAEService.delete(includeAEId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{includeAEId}")
    public IncludeAE findAssessmentActionTypeById(@PathVariable Long includeAEId) throws EntityNotFoundException {
        return includeAEService.findById(includeAEId);
    }

    @GetMapping
    public List<IncludeAE> findAll() {
        return includeAEService.findAll();
    }
}
