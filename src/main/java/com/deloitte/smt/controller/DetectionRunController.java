package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.service.DetectionRunService;

@RestController
@RequestMapping("/camunda/api/signal/detectionrun")
public class DetectionRunController {
	@Autowired
	DetectionRunService detectionRunService;

    @PostMapping
    public DetectionRun createDetectionRun(@RequestBody DetectionRun detectionRun) {
        return detectionRunService.insert(detectionRun);
    }

    @GetMapping(value = "/{detectionRunId}")
    public DetectionRun findDetectionRunById(@PathVariable Long detectionRunId) throws EntityNotFoundException {
        return detectionRunService.findById(detectionRunId);
    }

    @GetMapping
    public List<DetectionRun> findAll() {
        return detectionRunService.findAll();
    }
}
