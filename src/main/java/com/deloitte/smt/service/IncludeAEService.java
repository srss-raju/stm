package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.IncludeAERepository;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Service
public class IncludeAEService {

    @Autowired
    IncludeAERepository includeAERespository;

    public List<IncludeAE> insert(List<IncludeAE> includeAEs) {
    	includeAERespository.deleteAll();
    	for(IncludeAE includeAE:includeAEs){
    		includeAE.setCreatedDate(new Date());
    		includeAE.setLastModifiedDate(new Date());
    	}
        return includeAERespository.save(includeAEs);
    }

    public IncludeAE update(IncludeAE includeAE) throws UpdateFailedException {
        if(includeAE.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        includeAE = includeAERespository.save(includeAE);
        return includeAE;
    }

    public void delete(Long includeAEId) throws EntityNotFoundException {
    	IncludeAE includeAE = includeAERespository.findOne(includeAEId);
        if(includeAE == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+includeAE);
        }
        includeAERespository.delete(includeAE);
    }

    public IncludeAE findById(Long includeAEId) throws EntityNotFoundException {
    	IncludeAE includeAE = includeAERespository.findOne(includeAEId);
        if(includeAE == null) {
            throw new EntityNotFoundException("Signal Confirmation not found with the given Id : "+includeAEId);
        }
        return includeAE;
    }

    public List<IncludeAE> findAll() {
        return includeAERespository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
