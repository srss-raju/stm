package com.deloitte.smt.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.service.SignalService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class NonSignalController {

	private static final Logger LOG = Logger.getLogger(NonSignalController.class);
	@Autowired
	private SignalService signalService;

	@PostMapping(value = "/nonsignal")
	public NonSignal createNonSignal(@RequestParam(value = "data") String nonSignalStr){
		NonSignal nonSignal=null;
			try {
				nonSignal = new ObjectMapper().readValue(nonSignalStr,NonSignal.class);
			} catch (IOException e) {
				LOG.error(e);
			}
			return signalService.createOrupdateNonSignal(nonSignal);
	}
	
	
	@PostMapping(value="/nonsignal/update")
    public NonSignal updateNonSignal(@RequestParam(value = "data") String nonSignalStr) {
		NonSignal nonSignal=null;
		try {
			nonSignal = new ObjectMapper().readValue(nonSignalStr,NonSignal.class);
		} catch (IOException e) {
			LOG.error(e);
		}
		return signalService.createOrupdateNonSignal(nonSignal);
    }

}
