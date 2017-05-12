package com.deloitte.smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.ExternalDatasets;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.ExternalDatasetsRepository;

/**
 * Created by Rajesh on 02-05-2017.
 */
@Service
public class ExternalDatasetsService {

    @Autowired
    ExternalDatasetsRepository externalDatasetsRepository;

    public List<ExternalDatasets> insert(List<ExternalDatasets> externalDatasets) {
    	externalDatasetsRepository.deleteAll();
        return externalDatasetsRepository.save(externalDatasets);
    }

    public ExternalDatasets update(ExternalDatasets externalDatasets) throws UpdateFailedException {
        if(externalDatasets.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        externalDatasets = externalDatasetsRepository.save(externalDatasets);
        return externalDatasets;
    }

    public void delete(Long externalDatasetsId) throws EntityNotFoundException {
    	ExternalDatasets externalDatasets = externalDatasetsRepository.findOne(externalDatasetsId);
        if(externalDatasets == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+externalDatasetsId);
        }
        externalDatasetsRepository.delete(externalDatasets);
    }

    public ExternalDatasets findById(Long externalDatasetsId) throws EntityNotFoundException {
    	ExternalDatasets externalDatasets = externalDatasetsRepository.findOne(externalDatasetsId);
        if(externalDatasets == null) {
            throw new EntityNotFoundException("External Dataset not found with the given Id : "+externalDatasetsId);
        }
        return externalDatasets;
    }

    public List<ExternalDatasets> findAll() {
        return externalDatasetsRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
