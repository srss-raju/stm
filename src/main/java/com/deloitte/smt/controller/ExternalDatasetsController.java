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

import com.deloitte.smt.entity.ExternalDatasets;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.ExternalDatasetsService;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/ae")
public class ExternalDatasetsController {

    @Autowired
    ExternalDatasetsService externalDatasetsService;

    @PostMapping
    public List<ExternalDatasets> createNewAssessmentActionType(@RequestBody List<ExternalDatasets> externalDatasets) {
    	externalDatasets = externalDatasetsService.insert(externalDatasets);
        return externalDatasets;
    }

    @PutMapping
    public ExternalDatasets updateAssessmentActionType(@RequestBody ExternalDatasets externalDatasets) throws UpdateFailedException {
    	externalDatasets = externalDatasetsService.update(externalDatasets);
        return externalDatasets;
    }

    @DeleteMapping(value = "/{externalDatasetsId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long externalDatasetsId) throws EntityNotFoundException {
    	externalDatasetsService.delete(externalDatasetsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{includeAEId}")
    public ExternalDatasets findAssessmentActionTypeById(@PathVariable Long externalDatasetsId) throws EntityNotFoundException {
        return externalDatasetsService.findById(externalDatasetsId);
    }

    @GetMapping
    public List<ExternalDatasets> findAll() {
        return externalDatasetsService.findAll();
    }
}
