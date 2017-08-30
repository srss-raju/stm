package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssessmentAssignmentAssignees;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Service
public class AssignmentConfigurationService {

    @Autowired
    AssignmentConfigurationRepository assignmentConfigurationRepository;
    
    @Autowired
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
    @Autowired
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
    

    public AssignmentConfiguration insert(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        AssignmentConfiguration assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIngredientAndSignalSource(assignmentConfiguration.getIngredient(), assignmentConfiguration.getSignalSource());
        if(assignmentConfigurationFromDB != null){
        	throw new ApplicationException("Configuration already exists");
        }
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfigurationUpdated);
        return assignmentConfigurationUpdated;
    }

    public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        if(assignmentConfiguration.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        assignmentConfigurationRepository.save(assignmentConfiguration);
        
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfiguration);
        return assignmentConfiguration;
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
        assignmentConfiguration.setSignalValidationAssignmentAssignees(signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setAssessmentAssignmentAssignees(assessmentAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setRiskPlanAssignmentAssignees(riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        return assignmentConfiguration;
    }

    public List<AssignmentConfiguration> findAll() {
        return assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }
    
    /**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private void setAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		signalValidationAssignmentAssigneesRepository.deleteByAssignmentConfigurationId(assignmentConfiguration.getId());
		assessmentAssignmentAssigneesRepository.deleteByAssignmentConfigurationId(assignmentConfiguration.getId());
		riskPlanAssignmentAssigneesRepository.deleteByAssignmentConfigurationId(assignmentConfiguration.getId());
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSignalValidationAssignmentAssignees())){
        	for(SignalValidationAssignmentAssignees svaAssignees : assignmentConfiguration.getSignalValidationAssignmentAssignees()){
        		if(svaAssignees.getUserGroupKey()!= -1){
        			svaAssignees.setUserKey(0l);
        		}else{
        			svaAssignees.setUserGroupKey(0l);
        		}
        		svaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        		svaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
        	signalValidationAssignmentAssigneesRepository.save(assignmentConfiguration.getSignalValidationAssignmentAssignees());
        }
        
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getAssessmentAssignmentAssignees())){
			for(AssessmentAssignmentAssignees aaAssignees : assignmentConfiguration.getAssessmentAssignmentAssignees()){
				if(aaAssignees.getUserGroupKey()!= -1){
					aaAssignees.setUserKey(0l);
        		}else{
        			aaAssignees.setUserGroupKey(0l);
        		}
				aaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
				aaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
			assessmentAssignmentAssigneesRepository.save(assignmentConfiguration.getAssessmentAssignmentAssignees());
		}
		
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getRiskPlanAssignmentAssignees())){
			for(RiskPlanAssignmentAssignees rpaAssignees : assignmentConfiguration.getRiskPlanAssignmentAssignees()){
				if(rpaAssignees.getUserGroupKey()!= -1){
					rpaAssignees.setUserKey(0l);
        		}else{
        			rpaAssignees.setUserGroupKey(0l);
        		}
				rpaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
				rpaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
			riskPlanAssignmentAssigneesRepository.save(assignmentConfiguration.getRiskPlanAssignmentAssignees());
		}
	}

	public void deleteSignalValidationAssignmentAssignee(Long assigneeId) throws ApplicationException {
		SignalValidationAssignmentAssignees assignee = signalValidationAssignmentAssigneesRepository.findOne(assigneeId);
		if (assignee == null) {
			throw new ApplicationException("Signl Assignment Configuration not found with the given Id : "+assigneeId);
		}
		signalValidationAssignmentAssigneesRepository.delete(assignee);
	}

	public void deleteAssessmentAssignmentAssignee(Long assigneeId) throws ApplicationException {
		AssessmentAssignmentAssignees assignee = assessmentAssignmentAssigneesRepository.findOne(assigneeId);
		if (assignee == null) {
			throw new ApplicationException("Assessment Assignment Configuration not found with the given Id : "+assigneeId);
		}
		assessmentAssignmentAssigneesRepository.delete(assignee);
	}

	public void deleteRiskPlanAssignmentAssignee(Long assigneeId) throws ApplicationException {
		RiskPlanAssignmentAssignees assignee = riskPlanAssignmentAssigneesRepository.findOne(assigneeId);
		if (assignee == null) {
			throw new ApplicationException("Risk Plan Assignment Configuration not found with the given Id : "+assigneeId);
		}
		riskPlanAssignmentAssigneesRepository.delete(assignee);
	}
}
