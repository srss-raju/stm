package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.SignalDetectionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/camunda/api/signal/detect")
public class SignalDetectionController {
	
	private static final Logger LOG = Logger.getLogger(SignalDetectionController.class);
	
	@Autowired
	SignalDetectionService signalDetectionService;

	@PostMapping(value = "/createSignalDetection")
	public SignalDetection  createSignalDetection(@RequestParam(value = "data") String topicString) {
		try {
			SignalDetection signalDetection = new ObjectMapper().readValue(topicString, SignalDetection.class);
			return signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		} catch (ApplicationException | IOException e) {
			LOG.info("Exception occured while creating "+e);
		}
		return null;
	}
	
	@GetMapping(value = "/{signalDetectionId}")
    public SignalDetection getSignalDetectionById(@PathVariable Long signalDetectionId) throws ApplicationException {
        return signalDetectionService.findById(signalDetectionId);
    }
	
	@PostMapping(value = "/updateSignalDetection")
	public ResponseEntity<Void> updateTopic(@RequestParam(value = "data") String topicString) {
		try {
			SignalDetection signalDetection = new ObjectMapper().readValue(topicString, SignalDetection.class);
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		} catch (ApplicationException | IOException e) {
			LOG.info("Exception occured while updating "+e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{signalDetectionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long signalDetectionId) throws ApplicationException {
		signalDetectionService.delete(signalDetectionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(value = "/all")
	public List<SignalDetection> getAllByStatus(@RequestBody(required=false) SearchDto searchDto) {
		List<SignalDetection> detectionList;

		if(null!=searchDto && searchDto.isGantt()) {
			detectionList = signalDetectionService.ganttDetections(signalDetectionService.findAllForSearch(searchDto));
		}else{
			detectionList = signalDetectionService.findAllForSearch(searchDto);
		}
		return detectionList;
	}
	
}
