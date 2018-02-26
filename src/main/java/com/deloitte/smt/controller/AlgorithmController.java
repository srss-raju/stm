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

import com.deloitte.smt.entity.Algorithm;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AlgorithmService;

/**
 * Created by RajeshKumar on 26-02-2018.
 */
@RestController
@RequestMapping("/camunda/api/signal/algorithm")
public class AlgorithmController {

    @Autowired
    AlgorithmService algorithmService;

    @PostMapping
    public List<Algorithm> createAlgorithm(@RequestBody List<Algorithm> algorithm) {
        return algorithmService.create(algorithm);
    }

    @PutMapping
    public Algorithm updateAlgorithm(@RequestBody Algorithm algorithm) throws ApplicationException {
        return algorithmService.update(algorithm);
    }

    @DeleteMapping(value = "/{algorithmId}")
    public ResponseEntity<Void> deleteAlgorithm(@PathVariable Long algorithmId) throws ApplicationException {
    	algorithmService.delete(algorithmId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{algorithmId}")
    public Algorithm findFinalDispositionsById(@PathVariable Long algorithmId) throws ApplicationException {
        return algorithmService.findById(algorithmId);
    }

    @GetMapping
    public List<Algorithm> findAll() {
        return algorithmService.findAll();
    }
}
