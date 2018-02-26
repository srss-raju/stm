package com.deloitte.smt.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.Algorithm;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AlgorithmRepository;

/**
 * Created by Rajesh on 26-02-2018.
 */
@Transactional
@Service
public class AlgorithmService {

    @Autowired
    AlgorithmRepository algorithmRepository;

    public List<Algorithm> create(List<Algorithm> algorithm) {
        return algorithmRepository.save(algorithm);
    }

    public Algorithm update(Algorithm algorithm) throws ApplicationException {
        return algorithmRepository.save(algorithm);
    }

    public void delete(Long algorithmId) throws ApplicationException {
    	algorithmRepository.delete(algorithmRepository.findOne(algorithmId));
    }

    public Algorithm findById(Long algorithmId) throws ApplicationException {
    	Algorithm algorithm = algorithmRepository.findOne(algorithmId);
        if(algorithm == null) {
            throw new ApplicationException("Algorithm not found with the given Id : "+algorithmId);
        }
        return algorithm;
    }

    public List<Algorithm> findAll() {
        return algorithmRepository.findAll();
    }
}
