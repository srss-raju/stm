package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AssignmentConfigurationService;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/assignmentConfiguration")
public class AssignmentConfigurationController {

    @Autowired
    AssignmentConfigurationService assignmentConfigurationService;

    @PostMapping
    public ResponseEntity<Void> createNewAssignmentConfiguration(@RequestBody List<AssignmentConfiguration> assignmentConfiguration) throws ApplicationException {
         assignmentConfigurationService.insert(assignmentConfiguration);
         return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updateAssignmentConfiguration(@RequestBody List<AssignmentConfiguration> assignmentConfiguration) throws ApplicationException {
         assignmentConfigurationService.update(assignmentConfiguration);
         return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{assignmentConfigurationId}")
    public ResponseEntity<Void> deleteAssignmentConfiguration(@PathVariable Long assignmentConfigurationId) throws ApplicationException {
        assignmentConfigurationService.delete(assignmentConfigurationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{assignmentConfigurationId}")
    public AssignmentConfiguration findAssignmentConfigurationById(@PathVariable Long assignmentConfigurationId) throws ApplicationException {
        return assignmentConfigurationService.findById(assignmentConfigurationId);
    }

    @GetMapping
    public List<AssignmentConfiguration> findAll() {
        return assignmentConfigurationService.findAll();
    }
}
