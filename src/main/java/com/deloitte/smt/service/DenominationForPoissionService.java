package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.DenominatorForPoisson;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.DenominatorForPoissonRepository;

/**
 * Created by RajeshKumar on 02-05-2017.
 */
@Transactional
@Service
public class DenominationForPoissionService {

    @Autowired
    DenominatorForPoissonRepository denominationForPoissionRespository;

    public List<DenominatorForPoisson> insert(List<DenominatorForPoisson> denominationForPoissions) {
    	denominationForPoissionRespository.deleteByDetectionIdIsNull();
    	for(DenominatorForPoisson denominationForPoission:denominationForPoissions){
    		denominationForPoission.setCreatedDate(new Date());
    		denominationForPoission.setLastModifiedDate(new Date());
    	}
        return denominationForPoissionRespository.save(denominationForPoissions);
    }

    public DenominatorForPoisson update(DenominatorForPoisson denominationForPoission) throws ApplicationException {
        if(denominationForPoission.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        denominationForPoission.setLastModifiedDate(new Date());
        return denominationForPoissionRespository.save(denominationForPoission);
    }

    public void delete(Long denominationForPoissionId) throws ApplicationException {
    	DenominatorForPoisson denominationForPoission = denominationForPoissionRespository.findOne(denominationForPoissionId);
        if(denominationForPoission == null) {
            throw new ApplicationException("Risk Plan Action Type not found with the given Id : "+denominationForPoission);
        }
        denominationForPoissionRespository.delete(denominationForPoission);
    }

    public DenominatorForPoisson findById(Long denominationForPoissionId) throws ApplicationException {
    	DenominatorForPoisson denominationForPoission = denominationForPoissionRespository.findOne(denominationForPoissionId);
        if(denominationForPoission == null) {
            throw new ApplicationException("Signal Confirmation not found with the given Id : "+denominationForPoissionId);
        }
        return denominationForPoission;
    }

    public List<DenominatorForPoisson> findAll() {
        return denominationForPoissionRespository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
    
    public List<DenominatorForPoisson> findByDetectionIdIsNull() {
        return denominationForPoissionRespository.findByDetectionIdIsNullOrderByName();
    }
}
