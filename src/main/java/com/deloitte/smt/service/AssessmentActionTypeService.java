package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentActionType;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentActionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Service
public class AssessmentActionTypeService {

    @Autowired
    AssessmentActionTypeRepository assessmentActionTypeRepository;

    public AssessmentActionType insert(AssessmentActionType assessmentActionType) {
        assessmentActionType.setCreatedDate(new Date());
        assessmentActionType.setLastModifiedDate(new Date());
        assessmentActionType = assessmentActionTypeRepository.save(assessmentActionType);
        return assessmentActionType;
    }

    public AssessmentActionType update(AssessmentActionType assessmentActionType) throws UpdateFailedException {
        if(assessmentActionType.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        assessmentActionType.setLastModifiedDate(new Date());
        assessmentActionType = assessmentActionTypeRepository.save(assessmentActionType);
        return assessmentActionType;
    }

    public void delete(Long assessmentActionTypeId) throws EntityNotFoundException {
        AssessmentActionType assessmentActionType = assessmentActionTypeRepository.findOne(assessmentActionTypeId);
        if(assessmentActionType == null) {
            throw new EntityNotFoundException("Assessment Action Type not found with the given Id : "+assessmentActionTypeId);
        }
        assessmentActionTypeRepository.delete(assessmentActionType);
    }

    public AssessmentActionType findById(Long assessmentActionTypeId) throws EntityNotFoundException {
        AssessmentActionType assessmentActionType = assessmentActionTypeRepository.findOne(assessmentActionTypeId);
        if(assessmentActionType == null) {
            throw new EntityNotFoundException("Assessment Action Type not found with the given Id : "+assessmentActionTypeId);
        }
        return assessmentActionType;
    }

    public List<AssessmentActionType> findAll() {
        return assessmentActionTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
