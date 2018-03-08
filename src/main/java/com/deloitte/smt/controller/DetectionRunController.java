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

import com.deloitte.smt.dto.DetectionRunDTO;
import com.deloitte.smt.dto.DetectionRunResponseDTO;
import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.DetectionRunService;

@RestController
@RequestMapping("/camunda/api/signal/detectionrun")
public class DetectionRunController {
	@Autowired
	DetectionRunService detectionRunService;

    @PostMapping
    public DetectionRunResponseDTO createDetectionRun(@RequestBody DetectionRun detectionRun) {
        return detectionRunService.insert(detectionRun);
    }
    
    @PutMapping
    public DetectionRun updateDetectionRun(@RequestBody DetectionRun detectionRun) throws ApplicationException {
        return detectionRunService.update(detectionRun);
    }

    @GetMapping(value = "/{detectionRunId}")
    public DetectionRun findDetectionRunById(@PathVariable Long detectionRunId) throws ApplicationException {
        return detectionRunService.findById(detectionRunId);
    }

    @GetMapping
    public List<DetectionRun> findAll() {
        return detectionRunService.findAll();
    }

    @GetMapping(value = "/detection/{detectionId}")
    public List<DetectionRun> findByDetectionId(@PathVariable Long detectionId) {
        return detectionRunService.findByDetectionId(detectionId);
    }
    
    @DeleteMapping(value = "/{detectionRunId}")
    public ResponseEntity<Void> deleteDetectionRun(@PathVariable Long detectionRunId) throws ApplicationException {
    	detectionRunService.delete(detectionRunId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/getSignal")
    public boolean findSignal(@RequestBody DetectionRunDTO dto) {
        return detectionRunService.isSignalExist(dto);
    }
}
