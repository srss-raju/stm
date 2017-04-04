package com.deloitte.smt.controller;

import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/signal")
public class SignalController {

	@Autowired
	SignalService signalService;

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
	
	@GetMapping(value = "/getCounts")
	public String getCounts() {
		return SignalUtil.getCounts(signalService.getValidateAndPrioritizeCount(),signalService.getAssesmentCount(),signalService.getRiskCount());
	}
}
