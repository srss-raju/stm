package com.deloitte.smt.service;

import com.deloitte.smt.entity.RiskPlanActionTaskType;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.RiskPlanActionTaskTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Service
public class RiskPlanActionTypeService {

    @Autowired
    RiskPlanActionTaskTypeRepository riskPlanActionTaskTypeRepository;

    public RiskPlanActionTaskType insert(RiskPlanActionTaskType riskPlanActionTaskType) {
        riskPlanActionTaskType.setCreatedDate(new Date());
        riskPlanActionTaskType.setLastModifiedDate(new Date());
        riskPlanActionTaskType = riskPlanActionTaskTypeRepository.save(riskPlanActionTaskType);
        return riskPlanActionTaskType;
    }

    public RiskPlanActionTaskType update(RiskPlanActionTaskType riskPlanActionTaskType) throws UpdateFailedException {
        if(riskPlanActionTaskType.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        riskPlanActionTaskType.setLastModifiedDate(new Date());
        riskPlanActionTaskType = riskPlanActionTaskTypeRepository.save(riskPlanActionTaskType);
        return riskPlanActionTaskType;
    }

    public void delete(Long riskPlanActionTypeId) throws EntityNotFoundException {
        RiskPlanActionTaskType riskPlanActionTaskType = riskPlanActionTaskTypeRepository.findOne(riskPlanActionTypeId);
        if(riskPlanActionTaskType == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+riskPlanActionTypeId);
        }
        riskPlanActionTaskTypeRepository.delete(riskPlanActionTaskType);
    }

    public RiskPlanActionTaskType findById(Long riskPlanActionTypeId) throws EntityNotFoundException {
        RiskPlanActionTaskType riskPlanActionTaskType = riskPlanActionTaskTypeRepository.findOne(riskPlanActionTypeId);
        if(riskPlanActionTaskType == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+riskPlanActionTypeId);
        }
        return riskPlanActionTaskType;
    }

    public List<RiskPlanActionTaskType> findAll() {
        return riskPlanActionTaskTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
