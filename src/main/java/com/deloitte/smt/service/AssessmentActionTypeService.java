package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentActionType;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentActionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Transactional
@Service
public class AssessmentActionTypeService {

    @Autowired
    AssessmentActionTypeRepository assessmentActionTypeRepository;

    public List<AssessmentActionType> insert(List<AssessmentActionType> assessmentActionTypes) {
    	assessmentActionTypeRepository.deleteAll();
    	for(AssessmentActionType assessmentActionType:assessmentActionTypes){
    		assessmentActionType.setCreatedDate(new Date());
            assessmentActionType.setLastModifiedDate(new Date());
    	}
        return assessmentActionTypeRepository.save(assessmentActionTypes);
    }

    public AssessmentActionType update(AssessmentActionType assessmentActionType) throws ApplicationException {
        if(assessmentActionType.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assessmentActionType.setLastModifiedDate(new Date());
        return assessmentActionTypeRepository.save(assessmentActionType);
    }

    public void delete(Long assessmentActionTypeId) throws ApplicationException {
        AssessmentActionType assessmentActionType = assessmentActionTypeRepository.findOne(assessmentActionTypeId);
        if(assessmentActionType == null) {
            throw new ApplicationException("Assessment Action Type not found with the given Id : "+assessmentActionTypeId);
        }
        assessmentActionTypeRepository.delete(assessmentActionType);
    }

    public AssessmentActionType findById(Long assessmentActionTypeId) throws ApplicationException {
        AssessmentActionType assessmentActionType = assessmentActionTypeRepository.findOne(assessmentActionTypeId);
        if(assessmentActionType == null) {
            throw new ApplicationException("Assessment Action Type not found with the given Id : "+assessmentActionTypeId);
        }
        return assessmentActionType;
    }

    public List<AssessmentActionType> findAll() {
        return assessmentActionTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
