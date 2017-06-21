package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.SignalService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class NonSignalController {

	private static final Logger LOG = Logger.getLogger(NonSignalController.class);
	@Autowired
	private SignalService signalService;

	@PostMapping(value = "/nonsignal")
	public Topic createNonSignal(@RequestParam(value = "data") String nonSignalStr) throws ApplicationException{
		Topic nonSignal=null;
			try {
				nonSignal = new ObjectMapper().readValue(nonSignalStr,Topic.class);
				nonSignal.setSignal(false);
			} catch (IOException e) {
				LOG.error(e);
			}
			return signalService.createNonSignal(nonSignal);
	}
	
	@GetMapping(value = "/nonsignal/run/{runInstanceId}")
    public List<Topic> findTopicsByRunInstanceId(@PathVariable Long runInstanceId) {
        return signalService.findTopicsByRunInstanceId(runInstanceId);
    }

}
