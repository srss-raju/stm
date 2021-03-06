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

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AssignmentConfigurationService;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/assignmentConfiguration")
public class AssignmentConfigurationController {

    @Autowired
    AssignmentConfigurationService assignmentConfigurationService;

    @PostMapping
    public AssignmentConfiguration createNewAssignmentConfiguration(@RequestBody AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        return assignmentConfigurationService.insert(assignmentConfiguration);
    }

    @PutMapping
    public AssignmentConfiguration updateAssignmentConfiguration(@RequestBody AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        return assignmentConfigurationService.update(assignmentConfiguration);
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
    public List<AssignmentConfiguration> findAll() throws ApplicationException {
        return assignmentConfigurationService.findAll();
    }
    
    @DeleteMapping(value = "/signalValidationAssignmentAssignee/{assigneeId}")
    public ResponseEntity<Void> deleteSignalValidationAssignmentAssignee(@PathVariable Long assigneeId) throws ApplicationException {
        assignmentConfigurationService.deleteSignalValidationAssignmentAssignee(assigneeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/assessmentAssignmentAssignee/{assigneeId}")
    public ResponseEntity<Void> deleteAssessmentAssignmentAssignee(@PathVariable Long assigneeId) throws ApplicationException {
        assignmentConfigurationService.deleteAssessmentAssignmentAssignee(assigneeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/riskPlanAssignmentAssignee/{assigneeId}")
    public ResponseEntity<Void> deleteRiskPlanAssignmentAssignee(@PathVariable Long assigneeId) throws ApplicationException {
        assignmentConfigurationService.deleteRiskPlanAssignmentAssignee(assigneeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/soc/{socAssignmentConfigurationId}")
    public ResponseEntity<Void> deleteSocAssignmentConfiguration(@PathVariable Long socAssignmentConfigurationId) throws ApplicationException {
        assignmentConfigurationService.deleteSocAssignmentConfiguration(socAssignmentConfigurationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/product/{productAssignmentConfigurationId}")
    public ResponseEntity<Void> deleteProductAssignmentConfiguration(@PathVariable Long productAssignmentConfigurationId) throws ApplicationException {
        assignmentConfigurationService.deleteProductAssignmentConfiguration(productAssignmentConfigurationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
