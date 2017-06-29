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

import com.deloitte.smt.entity.DenominatorForPoisson;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.DenominationForPoissionService;

/**
 * Created by RajeshKumar on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/denomination")
public class DenominationForPoissionController {

    @Autowired
    DenominationForPoissionService denominationForPoissionService;

    @PostMapping
    public List<DenominatorForPoisson> createDenominationForPoission(@RequestBody List<DenominatorForPoisson> denominationForPoissions) {
    	return denominationForPoissionService.insert(denominationForPoissions);
    }

    @PutMapping
    public DenominatorForPoisson updateDenominationForPoission(@RequestBody DenominatorForPoisson denominationForPoission) throws ApplicationException {
    	return denominationForPoissionService.update(denominationForPoission);
    }

    @DeleteMapping(value = "/{denominationForPoissionId}")
    public ResponseEntity<Void> deleteDenominationForPoission(@PathVariable Long denominationForPoissionId) throws ApplicationException {
    	denominationForPoissionService.delete(denominationForPoissionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{denominationForPoissionId}")
    public DenominatorForPoisson findDenominationForPoissionById(@PathVariable Long denominationForPoissionId) throws ApplicationException {
        return denominationForPoissionService.findById(denominationForPoissionId);
    }

    @GetMapping
    public List<DenominatorForPoisson> findAll() {
        return denominationForPoissionService.findByDetectionIdIsNull();
    }
    
    @GetMapping(value = "/all")
    public List<DenominatorForPoisson> findByDetectionIdIsNullOrderByName() {
        return denominationForPoissionService.findByDetectionIdIsNull();
    }
}
