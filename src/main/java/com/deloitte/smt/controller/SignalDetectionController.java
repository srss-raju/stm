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

import com.deloitte.smt.dto.DetectionTopicDTO;
import com.deloitte.smt.dto.PtDTO;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.dto.SmqDTO;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.SignalAdditionalService;
import com.deloitte.smt.service.SignalDetectionService;
import com.deloitte.smt.service.SmqService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/camunda/api/signal/detect")
public class SignalDetectionController {
	
	private static final Logger LOG = Logger.getLogger(SignalDetectionController.class);
	
	@Autowired
	SignalDetectionService signalDetectionService;
	
	@Autowired
	SignalAdditionalService signalAdditionalService;
	
	@Autowired
	SmqService smqService;

	@PostMapping(value = "/createSignalDetection")
	public SignalDetection  createSignalDetection(@RequestParam(value = "data") String topicString) throws ApplicationException {
		SignalDetection signalDetection = null;
		try {
			signalDetection = new ObjectMapper().readValue(topicString, SignalDetection.class);
		} catch (IOException e) {
			try {
				throw e;
			} catch (IOException e1) {
				LOG.info("Exception occured while creating "+e1);
			}
		}
		return signalDetectionService.createOrUpdateSignalDetection(signalDetection);
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
	
	@DeleteMapping(value = "/detectionassignee/{assigneeId}")
    public ResponseEntity<Void> deleteByAssigneeId(@PathVariable Long assigneeId) throws ApplicationException {
		signalDetectionService.deleteByAssigneeId(assigneeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(value = "/allsmqs")
    public List<SmqDTO> findAllSmqs() {
        return smqService.findAllSmqs();
    }
	
	@PostMapping(value = "/allptsbysmqids")
    public List<PtDTO> findPtsBySmqId(@RequestBody(required=false) SearchDto searchDto) {
        return smqService.findPtsBySmqId(searchDto.getSmqIds());
    }
	
	@PostMapping(value = "/createTopic")
	public void createSignalDetection(@RequestBody List<DetectionTopicDTO> detectionTopicDTOs) throws ApplicationException {
		signalAdditionalService.saveSignalsAndNonSignals(detectionTopicDTOs);
	}
	
}
