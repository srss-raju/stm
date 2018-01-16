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

import com.deloitte.smt.entity.Task;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.TaskService;

/**
 * Created by RKB on 13-01-2018.
 */
@RestController
@RequestMapping("/camunda/api/signal/{type}/task")
public class TaskController    {
	
    @Autowired
    TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) throws ApplicationException {
    	if (task.getTemplateId() != 0) {
    		return taskService.createTemplateTask(task);
		} else {
			return taskService.createTask(task);
		}
    }

    @PutMapping
    public Task updateTask(@RequestBody Task task) throws ApplicationException {
    	return taskService.updateAssessmentAction(task);
    }
    
    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long taskId) throws ApplicationException {
    	taskService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
    @GetMapping(value = "/all/{id}")
    public List<Task> findAll(@PathVariable String type, @PathVariable Long id) {
        return taskService.findAll(type, id);
    }
	
	@GetMapping(value = "/{taskId}")
    public Task findByTemplateId(@PathVariable Long taskId) throws ApplicationException {
        return taskService.findById(taskId);
    }
	
}
