package com.deloitte.smt.service;

import com.deloitte.smt.entity.RiskPlanActionTaskType;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.RiskPlanActionTaskTypeRepository;
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
public class RiskPlanActionTypeService {

    @Autowired
    RiskPlanActionTaskTypeRepository riskPlanActionTaskTypeRepository;

    public List<RiskPlanActionTaskType> insert(List<RiskPlanActionTaskType> riskPlanActionTaskTypes) {
    	riskPlanActionTaskTypeRepository.deleteAll(); 	
    	for(RiskPlanActionTaskType riskPlanActionTaskType:riskPlanActionTaskTypes){
    		riskPlanActionTaskType.setCreatedDate(new Date());
            riskPlanActionTaskType.setLastModifiedDate(new Date());
    	}
        return riskPlanActionTaskTypeRepository.save(riskPlanActionTaskTypes);
    }

    public RiskPlanActionTaskType update(RiskPlanActionTaskType riskPlanActionTaskType) throws ApplicationException {
        if(riskPlanActionTaskType.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        riskPlanActionTaskType.setLastModifiedDate(new Date());
        return riskPlanActionTaskTypeRepository.save(riskPlanActionTaskType);
    }

    public void delete(Long riskPlanActionTypeId) throws ApplicationException {
        RiskPlanActionTaskType riskPlanActionTaskType = riskPlanActionTaskTypeRepository.findOne(riskPlanActionTypeId);
        if(riskPlanActionTaskType == null) {
            throw new ApplicationException("Risk Plan Action Type not found with the given Id : "+riskPlanActionTypeId);
        }
        riskPlanActionTaskTypeRepository.delete(riskPlanActionTaskType);
    }

    public RiskPlanActionTaskType findById(Long riskPlanActionTypeId) throws ApplicationException {
        RiskPlanActionTaskType riskPlanActionTaskType = riskPlanActionTaskTypeRepository.findOne(riskPlanActionTypeId);
        if(riskPlanActionTaskType == null) {
            throw new ApplicationException("Risk Plan Action Type not found with the given Id : "+riskPlanActionTypeId);
        }
        return riskPlanActionTaskType;
    }

    public List<RiskPlanActionTaskType> findAll() {
        return riskPlanActionTaskTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
