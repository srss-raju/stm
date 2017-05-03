package com.deloitte.smt.service;

import com.deloitte.smt.entity.FinalDispositions;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.FinalDispositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
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

    public FinalDispositions update(FinalDispositions finalDisposition) throws UpdateFailedException {
        if(finalDisposition.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        finalDisposition.setLastModifiedDate(new Date());
        finalDisposition = finalDispositionRepository.save(finalDisposition);
        return finalDisposition;
    }

    public void delete(Long finalDispositionId) throws EntityNotFoundException {
        FinalDispositions finalDisposition = finalDispositionRepository.findOne(finalDispositionId);
        if(finalDisposition == null) {
            throw new EntityNotFoundException("Final Disposition not found with the given Id : "+finalDispositionId);
        }
        finalDispositionRepository.delete(finalDisposition);
    }

    public FinalDispositions findById(Long finalDispositionId) throws EntityNotFoundException {
        FinalDispositions finalDisposition = finalDispositionRepository.findOne(finalDispositionId);
        if(finalDisposition == null) {
            throw new EntityNotFoundException("Signal Source not found with the given Id : "+finalDispositionId);
        }
        return finalDisposition;
    }

    public List<FinalDispositions> findAll() {
        return finalDispositionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}