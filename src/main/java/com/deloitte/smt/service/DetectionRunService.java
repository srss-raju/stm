package com.deloitte.smt.service;

import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.DetectionRunRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by rajesh on 05-05-2017.
 */
@Service
public class DetectionRunService {

    @Autowired
    DetectionRunRepository detectionRunRepository;
    
    @Autowired
	private SignalDetectionRepository signalDetectionRepository;

    public DetectionRun insert(DetectionRun detectionRun) {
    	detectionRun.setCreatedDate(new Date());
    	detectionRun.setRunDate(new Date());
    	detectionRun.setLastModifiedDate(new Date());
    	detectionRun = detectionRunRepository.save(detectionRun);
    	signalDetectionRepository.updateLastRunDate(detectionRun.getCreatedDate(), detectionRun.getDetectionId());
        return detectionRun;
    }

    public DetectionRun findById(Long detectionRunId) throws EntityNotFoundException {
    	DetectionRun detectionRun = detectionRunRepository.findOne(detectionRunId);
        if(detectionRun == null) {
            throw new EntityNotFoundException("Detection Run not found with the given Id : "+detectionRunId);
        }
        return detectionRun;
    }

    public List<DetectionRun> findAll() {
        return detectionRunRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }

    public List<DetectionRun> findByDetectionId(Long detectionId) {
        return detectionRunRepository.findByDetectionId(detectionId, new Sort(Sort.Direction.DESC, "createdDate"));
    }
}
