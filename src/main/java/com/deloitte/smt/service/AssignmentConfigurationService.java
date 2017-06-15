package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Service
public class AssignmentConfigurationService {

    @Autowired
    AssignmentConfigurationRepository assignmentConfigurationRepository;

    public void insert(List<AssignmentConfiguration> assignmentConfigurationList) throws ApplicationException {
    	for (AssignmentConfiguration assignmentConfiguration : assignmentConfigurationList) {
    		assignmentConfiguration.setCreatedDate(new Date());
            assignmentConfiguration.setLastModifiedDate(new Date());
            AssignmentConfiguration assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIngredientAndSignalSource(assignmentConfiguration.getIngredient(), assignmentConfiguration.getSignalSource());
            if(assignmentConfigurationFromDB != null){
            	throw new ApplicationException("Configuration already exists");
            }
		}
         assignmentConfigurationRepository.save(assignmentConfigurationList);
    }

    public void update(List<AssignmentConfiguration> assignmentConfigurationList) throws ApplicationException {
    	for (AssignmentConfiguration assignmentConfiguration : assignmentConfigurationList) {
    		if(assignmentConfiguration.getId() == null) {
                throw new ApplicationException("Required field Id is no present in the given request.");
            }
    		assignmentConfiguration.setLastModifiedDate(new Date());
		}
         assignmentConfigurationRepository.save(assignmentConfigurationList);
    }

    public void delete(Long assignmentConfigurationId) throws ApplicationException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new ApplicationException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        assignmentConfigurationRepository.delete(assignmentConfiguration);
    }

    public AssignmentConfiguration findById(Long assignmentConfigurationId) throws ApplicationException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new ApplicationException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        return assignmentConfiguration;
    }

    public List<AssignmentConfiguration> findAll() {
        return assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }
}
