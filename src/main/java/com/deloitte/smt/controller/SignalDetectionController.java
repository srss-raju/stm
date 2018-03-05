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

import com.deloitte.smt.dto.PtDTO;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.dto.SmqDTO;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.SignalDetectionService;
import com.deloitte.smt.service.SmqService;

@RestController
@RequestMapping(value = "/camunda/api/signal/detect")
public class SignalDetectionController {
	
	@Autowired
	SignalDetectionService signalDetectionService;
	
	@Autowired
	SmqService smqService;

	@PostMapping(value = "/createSignalDetection")
	public SignalDetection  createSignalDetection(@RequestBody SignalDetection signalDetection) throws ApplicationException {
		return signalDetectionService.createOrUpdateSignalDetection(signalDetection);
	}
	
	
	@GetMapping(value = "/{signalDetectionId}")
    public SignalDetection getSignalDetectionById(@PathVariable Long signalDetectionId) throws ApplicationException {
        return signalDetectionService.findById(signalDetectionId);
    }
	
	@PutMapping(value = "/updateSignalDetection")
	public SignalDetection updateTopic(@RequestBody SignalDetection signalDetection) throws ApplicationException {
		return signalDetectionService.createOrUpdateSignalDetection(signalDetection);
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
	
}
