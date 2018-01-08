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

import com.deloitte.smt.entity.TaskType;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.TaskTypeService;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/assessmentActionType")
public class TaskTypeController {

    @Autowired
    TaskTypeService assessmentActionTypeService;

    @PostMapping
    public List<TaskType> createNewTaskType(@RequestBody List<TaskType> assessmentActionTypes) {
        return assessmentActionTypeService.insert(assessmentActionTypes);
    }

    @PutMapping
    public TaskType updateTaskType(@RequestBody TaskType assessmentActionType) throws ApplicationException {
    	return assessmentActionTypeService.update(assessmentActionType);
    }

    @DeleteMapping(value = "/{assessmentActionTypeId}")
    public ResponseEntity<Void> deleteTaskType(@PathVariable Long assessmentActionTypeId) throws ApplicationException {
        assessmentActionTypeService.delete(assessmentActionTypeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{assessmentActionTypeId}")
    public TaskType findTaskTypeById(@PathVariable Long assessmentActionTypeId) throws ApplicationException {
        return assessmentActionTypeService.findById(assessmentActionTypeId);
    }

    @GetMapping
    public List<TaskType> findAll() {
        return assessmentActionTypeService.findAll();
    }
}
