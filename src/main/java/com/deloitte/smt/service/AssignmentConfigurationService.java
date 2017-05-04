package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Service
public class AssignmentConfigurationService {

    @Autowired
    AssignmentConfigurationRepository assignmentConfigurationRepository;

    public AssignmentConfiguration insert(AssignmentConfiguration assignmentConfiguration) {
        assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        return assignmentConfigurationRepository.save(assignmentConfiguration);
    }

    public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws UpdateFailedException {
        if(assignmentConfiguration.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        assignmentConfiguration = assignmentConfigurationRepository.save(assignmentConfiguration);
        return assignmentConfiguration;
    }

    public void delete(Long assignmentConfigurationId) throws EntityNotFoundException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new EntityNotFoundException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        assignmentConfigurationRepository.delete(assignmentConfiguration);
    }

    public AssignmentConfiguration findById(Long assignmentConfigurationId) throws EntityNotFoundException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new EntityNotFoundException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        return assignmentConfiguration;
    }

    public List<AssignmentConfiguration> findAll() {
        return assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }
}
