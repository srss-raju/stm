package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalConfirmations;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.SignalConfirmationRespository;
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
public class SignalConfirmationsService {

    @Autowired
    SignalConfirmationRespository signalConfirmationRespository;

    public List<SignalConfirmations> insert(List<SignalConfirmations> signalConfirmations) {
    	signalConfirmationRespository.deleteAll();
    	for(SignalConfirmations signalConfirmation:signalConfirmations){
    		signalConfirmation.setCreatedDate(new Date());
            signalConfirmation.setLastModifiedDate(new Date());
    	}
        return signalConfirmationRespository.save(signalConfirmations);
    }

    public SignalConfirmations update(SignalConfirmations signalConfirmation) throws ApplicationException {
        if(signalConfirmation.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        signalConfirmation.setLastModifiedDate(new Date());
        return signalConfirmationRespository.save(signalConfirmation);
    }

    public void delete(Long signalConfirmationId) throws ApplicationException {
        SignalConfirmations signalConfirmation = signalConfirmationRespository.findOne(signalConfirmationId);
        if(signalConfirmation == null) {
            throw new ApplicationException("Risk Plan Action Type not found with the given Id : "+signalConfirmationId);
        }
        signalConfirmationRespository.delete(signalConfirmation);
    }

    public SignalConfirmations findById(Long signalConfirmationId) throws ApplicationException {
        SignalConfirmations signalConfirmation = signalConfirmationRespository.findOne(signalConfirmationId);
        if(signalConfirmation == null) {
            throw new ApplicationException("Signal Confirmation not found with the given Id : "+signalConfirmationId);
        }
        return signalConfirmation;
    }

    public List<SignalConfirmations> findAll() {
        return signalConfirmationRespository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
