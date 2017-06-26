package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalSources;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.SignalSourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by RajeshKumar on 02-05-2017.
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

    public SignalSources update(SignalSources signalSources) throws ApplicationException {
        if(signalSources.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        signalSources.setLastModifiedDate(new Date());
        return signalSourcesRepository.save(signalSources);
    }

    public void delete(Long signalSourceId) throws ApplicationException {
        SignalSources signalSources = signalSourcesRepository.findOne(signalSourceId);
        if(signalSources == null) {
            throw new ApplicationException("Risk Plan Action Type not found with the given Id : "+signalSourceId);
        }
        signalSourcesRepository.delete(signalSources);
    }

    public SignalSources findById(Long signalSourceId) throws ApplicationException {
        SignalSources signalSources = signalSourcesRepository.findOne(signalSourceId);
        if(signalSources == null) {
            throw new ApplicationException("Signal Source not found with the given Id : "+signalSourceId);
        }
        return signalSources;
    }

    public List<SignalSources> findAll() {
        return signalSourcesRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
