package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalSources;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.SignalSourcesRepository;
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
public class SignalSourcesService {

    @Autowired
    SignalSourcesRepository signalSourcesRepository;

    public List<SignalSources> insert(List<SignalSources> signalSources) {
    	signalSourcesRepository.deleteAll();
    	for(SignalSources signalSource:signalSources){
    		signalSource.setCreatedDate(new Date());
            signalSource.setLastModifiedDate(new Date());
    	}
        return signalSourcesRepository.save(signalSources);
    }

    public SignalSources update(SignalSources signalSources) throws UpdateFailedException {
        if(signalSources.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        signalSources.setLastModifiedDate(new Date());
        signalSources = signalSourcesRepository.save(signalSources);
        return signalSources;
    }

    public void delete(Long signalSourceId) throws EntityNotFoundException {
        SignalSources signalSources = signalSourcesRepository.findOne(signalSourceId);
        if(signalSources == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+signalSourceId);
        }
        signalSourcesRepository.delete(signalSources);
    }

    public SignalSources findById(Long signalSourceId) throws EntityNotFoundException {
        SignalSources signalSources = signalSourcesRepository.findOne(signalSourceId);
        if(signalSources == null) {
            throw new EntityNotFoundException("Signal Source not found with the given Id : "+signalSourceId);
        }
        return signalSources;
    }

    public List<SignalSources> findAll() {
        return signalSourcesRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
