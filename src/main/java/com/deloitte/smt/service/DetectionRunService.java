package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.DetectionRunRepository;

/**
 * Created by rajesh on 05-05-2017.
 */
@Service
public class DetectionRunService {

    @Autowired
    DetectionRunRepository detectionRunRepository;

    public DetectionRun insert(DetectionRun detectionRun) {
    	detectionRun.setCreatedDate(new Date());
    	detectionRun.setRunDate(new Date());
    	detectionRun.setLastModifiedDate(new Date());
        return detectionRunRepository.save(detectionRun);
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
}