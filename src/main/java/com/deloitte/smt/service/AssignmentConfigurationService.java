package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssessmentAssignmentAssignees;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.SocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.ProductAssignmentConfigurationRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SocAssignmentConfigurationRepository;

/**
 * Created by myelleswarapu on 04-05-2017.
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
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
    @Autowired
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
     

    public AssignmentConfiguration insert(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        socAssignmentConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        productAssignmentConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfigurationUpdated);
        return assignmentConfigurationUpdated;
    }

	private void productAssignmentConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProductsAssignmentConfiguration())){
        	for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProductsAssignmentConfiguration()){
        		productConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        	}
        	productAssignmentConfigurationRepository.save(assignmentConfiguration.getProductsAssignmentConfiguration());
        }
	}

	private void socAssignmentConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSocsAssignmentConfiguration())){
        	for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getSocsAssignmentConfiguration()){
        		socConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        	}
        	socAssignmentConfigurationRepository.save(assignmentConfiguration.getSocsAssignmentConfiguration());
        }
	}

    public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
        if(assignmentConfiguration.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        assignmentConfigurationRepository.save(assignmentConfiguration);
        socAssignmentConfiguration(assignmentConfiguration, assignmentConfiguration);
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
        assignmentConfiguration.setSignalValidationAssignmentAssignees(signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setAssessmentAssignmentAssignees(assessmentAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setRiskPlanAssignmentAssignees(riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setSocsAssignmentConfiguration(socAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setProductsAssignmentConfiguration(productAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        return assignmentConfiguration;
    }

    public List<AssignmentConfiguration> findAll() {
    	List<AssignmentConfiguration> all = assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    	if(!CollectionUtils.isEmpty(all)){
    		for(AssignmentConfiguration config : all){
    			config.setSocsAssignmentConfiguration(socAssignmentConfigurationRepository.findByAssignmentConfigurationId(config.getId()));
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
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSignalValidationAssignmentAssignees())){
        	for(SignalValidationAssignmentAssignees svaAssignees : assignmentConfiguration.getSignalValidationAssignmentAssignees()){
        		svaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
        		svaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
        	signalValidationAssignmentAssigneesRepository.save(assignmentConfiguration.getSignalValidationAssignmentAssignees());
        }
        
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getAssessmentAssignmentAssignees())){
			for(AssessmentAssignmentAssignees aaAssignees : assignmentConfiguration.getAssessmentAssignmentAssignees()){
				aaAssignees.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
				aaAssignees.setCreatedDate(assignmentConfiguration.getCreatedDate());
        	}
			assessmentAssignmentAssigneesRepository.save(assignmentConfiguration.getAssessmentAssignmentAssignees());
		}
		
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getRiskPlanAssignmentAssignees())){
			for(RiskPlanAssignmentAssignees rpaAssignees : assignmentConfiguration.getRiskPlanAssignmentAssignees()){
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
