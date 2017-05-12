package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.DenominationForPoission;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.DenominationForPoissionRepository;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Service
public class DenominationForPoissionService {

    @Autowired
    DenominationForPoissionRepository denominationForPoissionRespository;

    public List<DenominationForPoission> insert(List<DenominationForPoission> denominationForPoissions) {
    	denominationForPoissionRespository.deleteAll();
    	for(DenominationForPoission denominationForPoission:denominationForPoissions){
    		denominationForPoission.setCreatedDate(new Date());
    		denominationForPoission.setLastModifiedDate(new Date());
    	}
        return denominationForPoissionRespository.save(denominationForPoissions);
    }

    public DenominationForPoission update(DenominationForPoission denominationForPoission) throws UpdateFailedException {
        if(denominationForPoission.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        denominationForPoission.setLastModifiedDate(new Date());
        denominationForPoission = denominationForPoissionRespository.save(denominationForPoission);
        return denominationForPoission;
    }

    public void delete(Long denominationForPoissionId) throws EntityNotFoundException {
    	DenominationForPoission denominationForPoission = denominationForPoissionRespository.findOne(denominationForPoissionId);
        if(denominationForPoission == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+denominationForPoission);
        }
        denominationForPoissionRespository.delete(denominationForPoission);
    }

    public DenominationForPoission findById(Long denominationForPoissionId) throws EntityNotFoundException {
    	DenominationForPoission denominationForPoission = denominationForPoissionRespository.findOne(denominationForPoissionId);
        if(denominationForPoission == null) {
            throw new EntityNotFoundException("Signal Confirmation not found with the given Id : "+denominationForPoissionId);
        }
        return denominationForPoission;
    }

    public List<DenominationForPoission> findAll() {
        return denominationForPoissionRespository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
