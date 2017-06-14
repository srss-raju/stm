package com.deloitte.smt.service;

import com.deloitte.smt.entity.FinalDispositions;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.FinalDispositionRepository;
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
public class FinalDispositionService {

    @Autowired
    FinalDispositionRepository finalDispositionRepository;

    public List<FinalDispositions> insert(List<FinalDispositions> finalDispositions) {
    	finalDispositionRepository.deleteAll();
    	for(FinalDispositions finalDisposition:finalDispositions){
    		finalDisposition.setCreatedDate(new Date());
            finalDisposition.setLastModifiedDate(new Date());
    	}
        return finalDispositionRepository.save(finalDispositions);
    }

    public FinalDispositions update(FinalDispositions finalDisposition) throws ApplicationException {
        if(finalDisposition.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        finalDisposition.setLastModifiedDate(new Date());
        return finalDispositionRepository.save(finalDisposition);
    }

    public void delete(Long finalDispositionId) throws ApplicationException {
        FinalDispositions finalDisposition = finalDispositionRepository.findOne(finalDispositionId);
        if(finalDisposition == null) {
            throw new ApplicationException("Final Disposition not found with the given Id : "+finalDispositionId);
        }
        finalDispositionRepository.delete(finalDisposition);
    }

    public FinalDispositions findById(Long finalDispositionId) throws ApplicationException {
        FinalDispositions finalDisposition = finalDispositionRepository.findOne(finalDispositionId);
        if(finalDisposition == null) {
            throw new ApplicationException("Signal Source not found with the given Id : "+finalDispositionId);
        }
        return finalDisposition;
    }

    public List<FinalDispositions> findAll() {
        return finalDispositionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
