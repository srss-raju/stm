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
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.SocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.repository.ProductAssignmentConfigurationRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SocAssignmentConfigurationRepository;

/**
 * Created by Rajesh on 16-11-2017.
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
    AssignmentProductRepository assignmentProductRepository;
    
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
        
        if(assignmentConfiguration.isDefault()){
        	AssignmentConfiguration defaultAssignmentConfigurationExists = assignmentConfigurationRepository.findByIsDefault(assignmentConfiguration.isDefault());
        	if(defaultAssignmentConfigurationExists != null){
        		throw new ApplicationException("Default AssignmentConfiguration is already exists with name"+defaultAssignmentConfigurationExists.getName());
        	}else{
        		return assignmentConfigurationRepository.save(assignmentConfiguration);
        	}
        }
         
        boolean isSocConfigExists = socAssignmentConfigurationDuplicateCheck(assignmentConfiguration, duplicateExceptionBuilder);
        boolean isProductConfigExists = productAssignmentConfigurationDuplicateCheck(assignmentConfiguration, duplicateExceptionBuilder);
        if(isSocConfigExists || isProductConfigExists){
    		duplicateExceptionBuilder.append("  is already exists");
    		throw new ApplicationException(duplicateExceptionBuilder.toString());
    	}
        
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        saveSocConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        saveProductConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfigurationUpdated);
        return assignmentConfigurationUpdated;
    }
    
    public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
    	StringBuilder duplicateExceptionBuilder = new StringBuilder();
    	if(assignmentConfiguration.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        boolean isSocConfigExists = socAssignmentConfigurationDuplicateCheck(assignmentConfiguration, duplicateExceptionBuilder);
        boolean isProductConfigExists = productAssignmentConfigurationDuplicateCheck(assignmentConfiguration, duplicateExceptionBuilder);
        if(isSocConfigExists || isProductConfigExists){
    		duplicateExceptionBuilder.append("  is already exists");
    		throw new ApplicationException(duplicateExceptionBuilder.toString());
    	}
        
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        saveSocConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        saveProductConfiguration(assignmentConfiguration, assignmentConfigurationUpdated);
        
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfiguration);
        return assignmentConfiguration;
    }
    
    public void delete(Long assignmentConfigurationId) throws ApplicationException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new ApplicationException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        assignmentConfigurationRepository.delete(assignmentConfiguration);
        deleteSocConfigurationAndCondition(assignmentConfigurationId);
        deleteProductConfigurationAndCondition(assignmentConfigurationId);
        
    }
    
    public AssignmentConfiguration findById(Long assignmentConfigurationId) throws ApplicationException {
        AssignmentConfiguration assignmentConfiguration = assignmentConfigurationRepository.findOne(assignmentConfigurationId);
        if(assignmentConfiguration == null) {
            throw new ApplicationException("Assignment Configuration not found with the given Id : "+assignmentConfigurationId);
        }
        assignmentConfiguration.setSignalAssignees(signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setAssessmentAssignees(assessmentAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        assignmentConfiguration.setRiskAssignees(riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfigurationId));
        
        setSocConfigurationAndCondition(assignmentConfigurationId, assignmentConfiguration);
        setProductConfigurationAndCondition(assignmentConfigurationId, assignmentConfiguration);
        return assignmentConfiguration;
    }
    
    public List<AssignmentConfiguration> findAll() throws ApplicationException {
         List<AssignmentConfiguration> all = assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
         if(!CollectionUtils.isEmpty(all)){
        	 for(AssignmentConfiguration aConfig : all){
        		 setSocConfigurationAndCondition( aConfig.getId(), aConfig);
        		 setProductConfigurationAndCondition(aConfig.getId(), aConfig);
        	 }
         }
         return all;
    }
    
    public void deleteSocAssignmentConfiguration(Long socAssignmentConfigurationId) throws ApplicationException {
		SocAssignmentConfiguration socAssignmentConfiguration = socAssignmentConfigurationRepository.findOne(socAssignmentConfigurationId);
		if (socAssignmentConfiguration == null) {
			throw new ApplicationException("Soc Assignment Configuration not found with the given Id : "+socAssignmentConfigurationId);
		}
		List<AssignmentCondition> list = assignmentConditionRepository.findBySocAssignmentConfigurationId(socAssignmentConfigurationId);
		if(!CollectionUtils.isEmpty(list)){
			assignmentConditionRepository.delete(list);
		}
		socAssignmentConfigurationRepository.delete(socAssignmentConfiguration);
	}
    
    public void deleteProductAssignmentConfiguration(Long productAssignmentConfigurationId) throws ApplicationException {
		ProductAssignmentConfiguration productAssignmentConfiguration = productAssignmentConfigurationRepository.findOne(productAssignmentConfigurationId);
		if (productAssignmentConfiguration == null) {
			throw new ApplicationException("Product Assignment Configuration not found with the given Id : "+productAssignmentConfigurationId);
		}
		List<AssignmentProduct> list = assignmentProductRepository.findByProductAssignmentConfigurationId(productAssignmentConfigurationId);
		if(!CollectionUtils.isEmpty(list)){
			assignmentProductRepository.delete(list);
		}
		productAssignmentConfigurationRepository.delete(productAssignmentConfiguration);
	}
    

	private boolean socAssignmentConfigurationDuplicateCheck(AssignmentConfiguration assignmentConfiguration, StringBuilder duplicateExceptionBuilder) throws ApplicationException {
		boolean isSocConfigExists = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
        	for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
        		SocAssignmentConfiguration socAssignmentConfigurationExists = socAssignmentConfigurationRepository.findByConditionKey(socConfig.getConditionKey());
        		if(socAssignmentConfigurationExists != null){
        			if(assignmentConfiguration.getId() != socAssignmentConfigurationExists.getAssignmentConfigurationId()){
        				isSocConfigExists = true;
            			duplicateExceptionBuilder.append(socConfig.getConditionKey());
        			}
        		}
        	}
        }
		return isSocConfigExists;
	}
	
	private boolean productAssignmentConfigurationDuplicateCheck(AssignmentConfiguration assignmentConfiguration, StringBuilder duplicateExceptionBuilder) throws ApplicationException {
		boolean isProductConfigExists = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
        	for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
        		ProductAssignmentConfiguration productAssignmentConfigurationExists = productAssignmentConfigurationRepository.findByProductKey(productConfig.getProductKey());
        		if(productAssignmentConfigurationExists != null){
        			if(assignmentConfiguration.getId() != productAssignmentConfigurationExists.getAssignmentConfigurationId()){
        				isProductConfigExists = true;
            			duplicateExceptionBuilder.append(productConfig.getProductKey());
        			}
        			
        		}
        	}
        }
		return isProductConfigExists;
	}

	private void saveSocConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		socAssignmentConfigurationRepository.delete(assignmentConfiguration.getConditions());
		for (SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()) {
			socConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
			SocAssignmentConfiguration socAssignmentConfigurationUpdated = socAssignmentConfigurationRepository.save(socConfig);
			if(!CollectionUtils.isEmpty(socConfig.getConditionValues())){
				assignmentConditionRepository.delete(socConfig.getConditionValues());
				for(AssignmentCondition condition : socConfig.getConditionValues()){
					condition.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
					condition.setSocAssignmentConfigurationId(socAssignmentConfigurationUpdated.getId());
				}
				assignmentConditionRepository.save(socConfig.getConditionValues());
			}
		}
	}
	
	private void saveProductConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated) {
		productAssignmentConfigurationRepository.delete(assignmentConfiguration.getProducts());
		for (ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()) {
			productConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
			ProductAssignmentConfiguration productAssignmentConfigurationUpdated = productAssignmentConfigurationRepository.save(productConfig);
			if(!CollectionUtils.isEmpty(productConfig.getProductValues())){
				assignmentProductRepository.delete(productConfig.getProductValues());
				for(AssignmentProduct product : productConfig.getProductValues()){
					product.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
					product.setProductAssignmentConfigurationId(productAssignmentConfigurationUpdated.getId());
				}
				assignmentProductRepository.save(productConfig.getProductValues());
			}
		}
	}

	private void deleteSocConfigurationAndCondition(Long assignmentConfigurationId) {
		List<SocAssignmentConfiguration> socAssignmentConfigurations =  socAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId);
        if(!CollectionUtils.isEmpty(socAssignmentConfigurations)){
        	for(SocAssignmentConfiguration socConfig : socAssignmentConfigurations){
        		List<AssignmentCondition> list = assignmentConditionRepository.findBySocAssignmentConfigurationId(socConfig.getId());
        		if(!CollectionUtils.isEmpty(list)){
        			assignmentConditionRepository.delete(list);
        		}
        	}
        }
        socAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(assignmentConfigurationId);
	}
	
	private void deleteProductConfigurationAndCondition(Long assignmentConfigurationId) {
		List<ProductAssignmentConfiguration> productAssignmentConfigurations =  productAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId);
        if(!CollectionUtils.isEmpty(productAssignmentConfigurations)){
        	for(ProductAssignmentConfiguration productConfig : productAssignmentConfigurations){
        		List<AssignmentProduct> list = assignmentProductRepository.findByProductAssignmentConfigurationId(productConfig.getId());
        		if(!CollectionUtils.isEmpty(list)){
        			assignmentProductRepository.delete(list);
        		}
        	}
        }
        productAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(assignmentConfigurationId);
	}

	private void setSocConfigurationAndCondition(Long assignmentConfigurationId, AssignmentConfiguration assignmentConfiguration) {
		List<SocAssignmentConfiguration> socAssignmentConfigurations =  socAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId);
        if(!CollectionUtils.isEmpty(socAssignmentConfigurations)){
        	for(SocAssignmentConfiguration socConfig : socAssignmentConfigurations){
        		socConfig.setConditionValues(assignmentConditionRepository.findBySocAssignmentConfigurationId(socConfig.getId()));
        	}
        	assignmentConfiguration.setConditions(socAssignmentConfigurations);
        }
	}
	
	private void setProductConfigurationAndCondition(Long assignmentConfigurationId, AssignmentConfiguration assignmentConfiguration) {
		List<ProductAssignmentConfiguration> productAssignmentConfigurations =  productAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId);
        if(!CollectionUtils.isEmpty(productAssignmentConfigurations)){
        	for(ProductAssignmentConfiguration productConfig : productAssignmentConfigurations){
        		productConfig.setProductValues(assignmentProductRepository.findByProductAssignmentConfigurationId(productConfig.getId()));
        	}
        	assignmentConfiguration.setProducts(productAssignmentConfigurations);
        }
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

}
