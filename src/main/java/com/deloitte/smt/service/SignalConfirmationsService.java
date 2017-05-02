package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalConfirmations;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.SignalConfirmationRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Service
public class SignalConfirmationsService {

    @Autowired
    SignalConfirmationRespository signalConfirmationRespository;

    public SignalConfirmations insert(SignalConfirmations signalConfirmation) {
        signalConfirmation.setCreatedDate(new Date());
        signalConfirmation.setLastModifiedDate(new Date());
        signalConfirmation = signalConfirmationRespository.save(signalConfirmation);
        return signalConfirmation;
    }

    public SignalConfirmations update(SignalConfirmations signalConfirmation) throws UpdateFailedException {
        if(signalConfirmation.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        signalConfirmation.setLastModifiedDate(new Date());
        signalConfirmation = signalConfirmationRespository.save(signalConfirmation);
        return signalConfirmation;
    }

    public void delete(Long signalConfirmationId) throws EntityNotFoundException {
        SignalConfirmations signalConfirmation = signalConfirmationRespository.findOne(signalConfirmationId);
        if(signalConfirmation == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+signalConfirmationId);
        }
        signalConfirmationRespository.delete(signalConfirmation);
    }

    public SignalConfirmations findById(Long signalConfirmationId) throws EntityNotFoundException {
        SignalConfirmations signalConfirmation = signalConfirmationRespository.findOne(signalConfirmationId);
        if(signalConfirmation == null) {
            throw new EntityNotFoundException("Signal Confirmation not found with the given Id : "+signalConfirmationId);
        }
        return signalConfirmation;
    }

    public List<SignalConfirmations> findAll() {
        return signalConfirmationRespository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
