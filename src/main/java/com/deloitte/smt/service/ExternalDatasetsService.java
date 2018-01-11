package com.deloitte.smt.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.ExternalDatasets;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.ExternalDatasetsRepository;

/**
 * Created by Rajesh on 02-05-2017.
 */
@Transactional
@Service
public class ExternalDatasetsService {

    @Autowired
    ExternalDatasetsRepository externalDatasetsRepository;

    public List<ExternalDatasets> insert(List<ExternalDatasets> externalDatasets) {
    	externalDatasetsRepository.deleteAll();
        return externalDatasetsRepository.save(externalDatasets);
    }

    public ExternalDatasets update(ExternalDatasets externalDatasets) throws ApplicationException {
        if(externalDatasets.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        return externalDatasetsRepository.save(externalDatasets);
    }

    public void delete(Long externalDatasetsId) throws ApplicationException {
    	ExternalDatasets externalDatasets = externalDatasetsRepository.findOne(externalDatasetsId);
        if(externalDatasets == null) {
            throw new ApplicationException("Risk Plan Action Type not found with the given Id : "+externalDatasetsId);
        }
        externalDatasetsRepository.delete(externalDatasets);
    }

    public ExternalDatasets findById(Long externalDatasetsId) throws ApplicationException {
    	ExternalDatasets externalDatasets = externalDatasetsRepository.findOne(externalDatasetsId);
        if(externalDatasets == null) {
            throw new ApplicationException("External Dataset not found with the given Id : "+externalDatasetsId);
        }
        return externalDatasets;
    }

    public List<ExternalDatasets> findAll() {
        return externalDatasetsRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
