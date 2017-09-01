package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.exception.ApplicationException;
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
    
    public DetectionRun update(DetectionRun detectionRun) throws ApplicationException {
        if(detectionRun.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        return detectionRunRepository.save(detectionRun);
    }

    public DetectionRun findById(Long detectionRunId) throws ApplicationException {
    	DetectionRun detectionRun = detectionRunRepository.findOne(detectionRunId);
        if(detectionRun == null) {
            throw new ApplicationException("Detection Run not found with the given Id : "+detectionRunId);
        }
        return detectionRun;
    }

    public List<DetectionRun> findAll() {
        return detectionRunRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }

    public List<DetectionRun> findByDetectionId(Long detectionId) {
        return detectionRunRepository.findByDetectionId(detectionId, new Sort(Sort.Direction.DESC, "createdDate"));
    }

	public void delete(Long detectionRunId) throws ApplicationException {
		DetectionRun detectionRun = detectionRunRepository.findOne(detectionRunId);
        if(detectionRun == null) {
            throw new ApplicationException("Detection Run not found with the given Id : "+detectionRunId);
        }else{
        	detectionRunRepository.delete(detectionRun);
        }
		
	}
}
