package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssessmentAssignmentAssignees;
import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.SocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.ProductAssignmentConfigurationRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SocAssignmentConfigurationRepository;

/**
 * Created by Rajesh on 04-05-2017.
 */
@Service
public class AssignmentConfigurationService {

    @Autowired
    AssignmentConfigurationRepository assignmentConfigurationRepository;
    
    @Autowired
    SocAssignmentConfigurationRepository socAssignmentConfigurationRepository;
    
    @Autowired
    ProductAssignmentConfigurationRepository productAssignmentConfigurationRepository;
    
    @Autowired
    AssignmentConditionRepository assignmentConditionRepository;
    
    @Autowired
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
    @Autowired
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
     

    public AssignmentConfiguration insert(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
    	StringBuilder duplicateExceptionBuilder = new StringBuilder();
    	assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        
        AssignmentConfiguration assignmentConfigurationExists = assignmentConfigurationRepository.findByName(assignmentConfiguration.getName());
        if(assignmentConfigurationExists != null){
        	throw new ApplicationException("AssignmentConfiguration is already exists with given name");
        }
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        socAssignmentConfigurationDuplicateCheck(assignmentConfiguration, duplicateExceptionBuilder, assignmentConfigurationUpdated);
        
        
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfigurationUpdated);
        return assignmentConfigurationUpdated;
    }

	private void socAssignmentConfigurationDuplicateCheck(AssignmentConfiguration assignmentConfiguration, StringBuilder duplicateExceptionBuilder, AssignmentConfiguration assignmentConfigurationUpdated) throws ApplicationException {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
        	boolean isSocConfigExists = false;
        	for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
        		socConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        		SocAssignmentConfiguration socAssignmentConfigurationUpdated = socAssignmentConfigurationRepository.findByConditionKey(socConfig.getConditionKey());
        		if(socAssignmentConfigurationUpdated != null){
        			isSocConfigExists = true;
        			duplicateExceptionBuilder.append(socConfig.getConditionKey());
        			
        		}
        	}
        	if(isSocConfigExists){
        		duplicateExceptionBuilder.append("Configuration is already exists");
        		throw new ApplicationException(duplicateExceptionBuilder.toString());
        	}
			saveSocConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        }
	}

	private void saveSocConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		for (SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()) {
			socConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
			SocAssignmentConfiguration socAssignmentConfigurationUpdated = socAssignmentConfigurationRepository.save(socConfig);
			if(!CollectionUtils.isEmpty(socConfig.getConditionValues())){
				for(AssignmentCondition condition : socConfig.getConditionValues()){
					condition.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
					condition.setSocAssignmentConfigurationId(socAssignmentConfigurationUpdated.getId());
				}
				assignmentConditionRepository.save(socConfig.getConditionValues());
			}
		}
	}

	private void productAssignmentConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProductsAssignmentConfiguration())){
        	for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProductsAssignmentConfiguration()){
        		productConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        	}
        	productAssignmentConfigurationRepository.save(assignmentConfiguration.getProductsAssignmentConfiguration());
        }
	}

	

    public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        if(assignmentConfiguration.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        assignmentConfigurationRepository.save(assignmentConfiguration);
        productAssignmentConfiguration(assignmentConfiguration, assignmentConfiguration);
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfiguration);
        return assignmentConfiguration;
    }

    public void delete(Long assignmentConfigurationId) throws ApplicationException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new ApplicationException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        assignmentConfigurationRepository.delete(assignmentConfiguration);
        socAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(assignmentConfigurationId);
        productAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(assignmentConfigurationId);
    }

    public AssignmentConfiguration findById(Long assignmentConfigurationId) throws ApplicationException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new ApplicationException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        assignmentConfiguration.setSignalAssignees(signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setAssessmentAssignees(assessmentAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setRiskAssignees(riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setProductsAssignmentConfiguration(productAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        return assignmentConfiguration;
    }

    public List<AssignmentConfiguration> findAll() {
    	List<AssignmentConfiguration> all = assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    	if(!CollectionUtils.isEmpty(all)){
    		for(AssignmentConfiguration config : all){
    			config.setProductsAssignmentConfiguration(productAssignmentConfigurationRepository.findByAssignmentConfigurationId(config.getId()));
    		}
    	}
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
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSignalAssignees())){
        	for(SignalValidationAssignmentAssignees svaAssignees : assignmentConfiguration.getSignalAssignees()){
        		svaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        		svaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
        	signalValidationAssignmentAssigneesRepository.save(assignmentConfiguration.getSignalAssignees());
        }
        
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getAssessmentAssignees())){
			for(AssessmentAssignmentAssignees aaAssignees : assignmentConfiguration.getAssessmentAssignees()){
				aaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
				aaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
			assessmentAssignmentAssigneesRepository.save(assignmentConfiguration.getAssessmentAssignees());
		}
		
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getRiskAssignees())){
			for(RiskPlanAssignmentAssignees rpaAssignees : assignmentConfiguration.getRiskAssignees()){
				rpaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
				rpaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
			riskPlanAssignmentAssigneesRepository.save(assignmentConfiguration.getRiskAssignees());
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

	public void deleteSocAssignmentConfiguration(Long socAssignmentConfigurationId) throws ApplicationException {
		SocAssignmentConfiguration socAssignmentConfiguration = socAssignmentConfigurationRepository.findOne(socAssignmentConfigurationId);
		if (socAssignmentConfiguration == null) {
			throw new ApplicationException("Soc Assignment Configuration not found with the given Id : "+socAssignmentConfigurationId);
		}
		socAssignmentConfigurationRepository.delete(socAssignmentConfiguration);
		
	}

	public void deleteProductAssignmentConfiguration(Long productAssignmentConfigurationId) throws ApplicationException {
		ProductAssignmentConfiguration productAssignmentConfiguration = productAssignmentConfigurationRepository.findOne(productAssignmentConfigurationId);
		if (productAssignmentConfiguration == null) {
			throw new ApplicationException("Product Assignment Configuration not found with the given Id : "+productAssignmentConfigurationId);
		}
		productAssignmentConfigurationRepository.delete(productAssignmentConfigurationId);
		
	}
}
