package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssessmentActionType;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.AssessmentActionTypeService;
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
@RequestMapping("/camunda/api/signal/assessmentActionType")
public class AssessmentActionTypeController {

    @Autowired
    AssessmentActionTypeService assessmentActionTypeService;

    @PostMapping
    public List<AssessmentActionType> createNewAssessmentActionType(@RequestBody List<AssessmentActionType> assessmentActionTypes) {
        return assessmentActionTypeService.insert(assessmentActionTypes);
    }

    @PutMapping
    public AssessmentActionType updateAssessmentActionType(@RequestBody AssessmentActionType assessmentActionType) throws UpdateFailedException {
        assessmentActionType = assessmentActionTypeService.update(assessmentActionType);
        return assessmentActionType;
    }

    @DeleteMapping(value = "/{assessmentActionTypeId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long assessmentActionTypeId) throws EntityNotFoundException {
        assessmentActionTypeService.delete(assessmentActionTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{assessmentActionTypeId}")
    public AssessmentActionType findAssessmentActionTypeById(@PathVariable Long assessmentActionTypeId) throws EntityNotFoundException {
        return assessmentActionTypeService.findById(assessmentActionTypeId);
    }

    @GetMapping
    public List<AssessmentActionType> findAll() {
        return assessmentActionTypeService.findAll();
    }
}
