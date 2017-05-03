package com.deloitte.smt.controller;

import com.deloitte.smt.entity.RiskPlanActionTaskType;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.RiskPlanActionTypeService;
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
@RequestMapping("/camunda/api/signal/riskPlanActionType")
public class RiskPlanActionTypeController {

    @Autowired
    RiskPlanActionTypeService riskPlanActionTypeService;

    @PostMapping
    public List<RiskPlanActionTaskType> createNewAssessmentActionType(@RequestBody List<RiskPlanActionTaskType> riskPlanActionTaskTypes) {
    	riskPlanActionTaskTypes = riskPlanActionTypeService.insert(riskPlanActionTaskTypes);
        return riskPlanActionTaskTypes;
    }

    @PutMapping
    public RiskPlanActionTaskType updateAssessmentActionType(@RequestBody RiskPlanActionTaskType riskPlanActionTaskType) throws UpdateFailedException {
        riskPlanActionTaskType = riskPlanActionTypeService.update(riskPlanActionTaskType);
        return riskPlanActionTaskType;
    }

    @DeleteMapping(value = "/{riskPlanActionTypeId}")
    public ResponseEntity<Void> deleteAssessmentActionType(@PathVariable Long riskPlanActionTypeId) throws EntityNotFoundException {
        riskPlanActionTypeService.delete(riskPlanActionTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{riskPlanActionTypeId}")
    public RiskPlanActionTaskType findAssessmentActionTypeById(@PathVariable Long riskPlanActionTypeId) throws EntityNotFoundException {
        return riskPlanActionTypeService.findById(riskPlanActionTypeId);
    }

    @GetMapping
    public List<RiskPlanActionTaskType> findAll() {
        return riskPlanActionTypeService.findAll();
    }
}
