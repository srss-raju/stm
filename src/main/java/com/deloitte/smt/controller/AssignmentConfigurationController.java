package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.exception.EntityAlreadyExistsException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
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
    public AssignmentConfiguration createNewAssignmentConfiguration(@RequestBody AssignmentConfiguration assignmentConfiguration) throws EntityAlreadyExistsException {
        return assignmentConfigurationService.insert(assignmentConfiguration);
    }

    @PutMapping
    public AssignmentConfiguration updateAssignmentConfiguration(@RequestBody AssignmentConfiguration assignmentConfiguration) throws UpdateFailedException {
        assignmentConfiguration = assignmentConfigurationService.update(assignmentConfiguration);
        return assignmentConfiguration;
    }

    @DeleteMapping(value = "/{assignmentConfigurationId}")
    public ResponseEntity<Void> deleteAssignmentConfiguration(@PathVariable Long assignmentConfigurationId) throws EntityNotFoundException {
        assignmentConfigurationService.delete(assignmentConfigurationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{assignmentConfigurationId}")
    public AssignmentConfiguration findAssignmentConfigurationById(@PathVariable Long assignmentConfigurationId) throws EntityNotFoundException {
        return assignmentConfigurationService.findById(assignmentConfigurationId);
    }

    @GetMapping
    public List<AssignmentConfiguration> findAll() {
        return assignmentConfigurationService.findAll();
    }
}
