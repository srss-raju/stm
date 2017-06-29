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
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.ExternalDatasetsService;

/**
 * Created by RajeshKumar on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/ae")
public class ExternalDatasetsController {

    @Autowired
    ExternalDatasetsService externalDatasetsService;

    @PostMapping
    public List<ExternalDatasets> createExternalDatasets(@RequestBody List<ExternalDatasets> externalDatasets) {
    	return externalDatasetsService.insert(externalDatasets);
    }

    @PutMapping
    public ExternalDatasets updateExternalDatasets(@RequestBody ExternalDatasets externalDatasets) throws ApplicationException {
    	return externalDatasetsService.update(externalDatasets);
    }

    @DeleteMapping(value = "/{externalDatasetsId}")
    public ResponseEntity<Void> deleteExternalDatasets(@PathVariable Long externalDatasetsId) throws ApplicationException {
    	externalDatasetsService.delete(externalDatasetsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{includeAEId}")
    public ExternalDatasets findExternalDatasetsById(@PathVariable Long includeAEId) throws ApplicationException {
        return externalDatasetsService.findById(includeAEId);
    }

    @GetMapping
    public List<ExternalDatasets> findAll() {
        return externalDatasetsService.findAll();
    }
}
