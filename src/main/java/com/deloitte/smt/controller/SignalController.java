package com.deloitte.smt.controller;

import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/signal")
public class SignalController {

	@Autowired
	SignalService signalService;

	@Autowired
	TaskInstRepository taskInstRepository;

	@GetMapping(value = "/createTopic")
	public String createTopic() {
		return signalService.createTopic();
	}
	
	@GetMapping(value = "/{processInstanceId}/validate")
	public String validateTopic(@PathVariable String processInstanceId) throws TaskNotFoundException {
		signalService.validateTopic(processInstanceId);
		return "Validation is finished";
	}
	
	@GetMapping(value = "/{processInstanceId}/prioritize")
	public String prioritizeTopic(@PathVariable String processInstanceId) throws TaskNotFoundException {
		signalService.prioritizeTopic(processInstanceId);
		return "finished";
	}

	@GetMapping(value = "/all")
	public List<Topic> getAllByStatus(@RequestParam(name = "status", required = false) String statuses,
									  @RequestParam(name = "deleteReason", required = false, defaultValue = "completed") String deleteReason){
		return signalService.findAllByStatus(statuses, deleteReason);
	}
}
