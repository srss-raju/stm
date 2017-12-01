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
    	Long id = assignmentConfiguration.getId();
    	StringBuilder duplicateExceptionBuilder = new StringBuilder();
    	assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        
        AssignmentConfiguration assignmentConfigurationExists = assignmentConfigurationRepository.findByName(assignmentConfiguration.getName());
        if(assignmentConfigurationExists != null){
        	throw new ApplicationException("AssignmentConfiguration is already exists with given name");
        }
        
        boolean isExists = duplicateCheck(assignmentConfiguration);
        if(isExists){
    		duplicateExceptionBuilder.append("  is already exists");
    		throw new ApplicationException(duplicateExceptionBuilder.toString());
    	}
        
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        saveSocConfiguration(assignmentConfiguration, assignmentConfigurationUpdated,id);
        saveProductConfiguration(assignmentConfiguration, assignmentConfigurationUpdated,id);
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfigurationUpdated);
        return assignmentConfigurationUpdated;
    }

	private boolean duplicateCheck(AssignmentConfiguration assignmentConfiguration) {
		StringBuilder duplicateConfigBuilderDB = new StringBuilder();
		StringBuilder duplicateConfigBuilderUI = new StringBuilder();
		boolean duplicateRecordFlag;
		
		if((!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())) && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			duplicateSocCheck(assignmentConfiguration, duplicateConfigBuilderDB, duplicateConfigBuilderUI, true);
			duplicateProductCheck(assignmentConfiguration, duplicateConfigBuilderDB, duplicateConfigBuilderUI, true);
			duplicateRecordFlag = (duplicateConfigBuilderUI.toString()).equalsIgnoreCase(duplicateConfigBuilderDB.toString());
        }else if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
        	duplicateRecordFlag = duplicateSocCheck(assignmentConfiguration, duplicateConfigBuilderDB, duplicateConfigBuilderUI, false);
        }else {
        	duplicateRecordFlag = duplicateProductCheck(assignmentConfiguration, duplicateConfigBuilderDB, duplicateConfigBuilderUI, false);
        }
		
		return duplicateRecordFlag;
	}


	private boolean duplicateSocCheck(AssignmentConfiguration assignmentConfiguration, StringBuilder duplicateConfigBuilderDB, StringBuilder duplicateConfigBuilderUI, boolean socProductFlag) {
		boolean duplicateSocFlag = false;
		for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
			duplicateConfigBuilderUI.append(socConfig.getRecordKey());
			SocAssignmentConfiguration socAssignmentConfigurationFromDB = socAssignmentConfigurationRepository.findByRecordKey(socConfig.getRecordKey());
			if(socAssignmentConfigurationFromDB != null){
				if(socProductFlag  && (!(String.valueOf(assignmentConfiguration.getId()).equalsIgnoreCase(String.valueOf(socAssignmentConfigurationFromDB.getAssignmentConfigurationId()))))){
					duplicateConfigBuilderDB.append(socAssignmentConfigurationFromDB.getRecordKey());
				}else if(!(String.valueOf(assignmentConfiguration.getId()).equalsIgnoreCase(String.valueOf(socAssignmentConfigurationFromDB.getAssignmentConfigurationId())))){
					duplicateSocFlag = socAssignmentConfigurationFromDB.getRecordKey().equalsIgnoreCase(socConfig.getRecordKey());
				}
			}
		}
		return duplicateSocFlag;
	}
	
	private boolean duplicateProductCheck( AssignmentConfiguration assignmentConfiguration, StringBuilder duplicateConfigBuilderDB, StringBuilder duplicateConfigBuilderUI, boolean socProductFlag) {
		boolean duplicateProductFlag = false;
		for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
			duplicateConfigBuilderUI.append(productConfig.getRecordKey());
			ProductAssignmentConfiguration productAssignmentConfigurationFromDB = productAssignmentConfigurationRepository.findByRecordKey(productConfig.getRecordKey());
			if(productAssignmentConfigurationFromDB != null){
				if(socProductFlag  && (!(String.valueOf(assignmentConfiguration.getId()).equalsIgnoreCase(String.valueOf(productAssignmentConfigurationFromDB.getAssignmentConfigurationId()))))){
					duplicateConfigBuilderDB.append(productAssignmentConfigurationFromDB.getRecordKey());
				}else if(!(String.valueOf(assignmentConfiguration.getId()).equalsIgnoreCase(String.valueOf(productAssignmentConfigurationFromDB.getAssignmentConfigurationId())))){
					duplicateProductFlag = productAssignmentConfigurationFromDB.getRecordKey().equalsIgnoreCase(productConfig.getRecordKey());
				}
			}
		}
		return duplicateProductFlag;
	}
    
    public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
    	
    	StringBuilder duplicateExceptionBuilder = new StringBuilder();
    	if(assignmentConfiguration.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        
        if(!assignmentConfiguration.isDefault()){
        	 boolean isExists = duplicateCheck(assignmentConfiguration);
        	if(isExists){
	    		duplicateExceptionBuilder.append("  is already exists");
	    		throw new ApplicationException(duplicateExceptionBuilder.toString());
	    	}
        }
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        Long id = assignmentConfigurationUpdated.getId();
        if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
        	saveSocConfiguration(assignmentConfiguration, assignmentConfigurationUpdated, id);
        }else{
        	socAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(id);
        }
        if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
        	saveProductConfiguration(assignmentConfiguration, assignmentConfigurationUpdated, id);
        }else{
        	productAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(id);
        }
        
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
    

	private void saveSocConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated, Long id) {
		socAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(id);
		for (SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()) {
			socConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
			SocAssignmentConfiguration socAssignmentConfigurationUpdated = socAssignmentConfigurationRepository.save(socConfig);
			if(!CollectionUtils.isEmpty(socConfig.getRecordValues())){
				assignmentConditionRepository.delete(socConfig.getRecordValues());
				for(AssignmentCondition condition : socConfig.getRecordValues()){
					condition.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
					condition.setSocAssignmentConfigurationId(socAssignmentConfigurationUpdated.getId());
				}
				assignmentConditionRepository.save(socConfig.getRecordValues());
			}
		}
	}
	
	private void saveProductConfiguration(AssignmentConfiguration assignmentConfiguration, AssignmentConfiguration assignmentConfigurationUpdated, Long id) {
		productAssignmentConfigurationRepository.deleteByAssignmentConfigurationId(id);
		for (ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()) {
			productConfig.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
			ProductAssignmentConfiguration productAssignmentConfigurationUpdated = productAssignmentConfigurationRepository.save(productConfig);
			if(!CollectionUtils.isEmpty(productConfig.getRecordValues())){
				assignmentProductRepository.delete(productConfig.getRecordValues());
				for(AssignmentProduct product : productConfig.getRecordValues()){
					product.setAssignmentConfigurationId(assignmentConfigurationUpdated.getId());
					product.setProductAssignmentConfigurationId(productAssignmentConfigurationUpdated.getId());
				}
				assignmentProductRepository.save(productConfig.getRecordValues());
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
        		socConfig.setRecordValues(assignmentConditionRepository.findBySocAssignmentConfigurationId(socConfig.getId()));
        	}
        	assignmentConfiguration.setConditions(socAssignmentConfigurations);
        }
	}
	
	private void setProductConfigurationAndCondition(Long assignmentConfigurationId, AssignmentConfiguration assignmentConfiguration) {
		List<ProductAssignmentConfiguration> productAssignmentConfigurations =  productAssignmentConfigurationRepository.findByAssignmentConfigurationId(assignmentConfigurationId);
        if(!CollectionUtils.isEmpty(productAssignmentConfigurations)){
        	for(ProductAssignmentConfiguration productConfig : productAssignmentConfigurations){
        		productConfig.setRecordValues(assignmentProductRepository.findByProductAssignmentConfigurationId(productConfig.getId()));
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
