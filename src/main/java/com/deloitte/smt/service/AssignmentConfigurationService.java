package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.AssignmentDTO;
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
	
	@PersistenceContext
	private EntityManager entityManager;

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
    	StringBuilder totalRecordKeyFromUI = new StringBuilder();
    	assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        
        duplicateConfigurationCheck(assignmentConfiguration);
        
        boolean isExists = duplicateCheck(assignmentConfiguration);
        if(isExists){
    		throw new ApplicationException("Record already exists");
    	}
        assignmentConfiguration.setTotalRecordKey(totalRecordKeyFromUI.toString());
        AssignmentConfiguration assignmentConfigurationUpdated = assignmentConfigurationRepository.save(assignmentConfiguration);
        saveSocConfiguration(assignmentConfiguration, assignmentConfigurationUpdated,id);
        saveProductConfiguration(assignmentConfiguration, assignmentConfigurationUpdated,id);
        setAssignmentConfigurationAssignees(assignmentConfiguration, assignmentConfigurationUpdated);
        return assignmentConfigurationUpdated;
    }

	private void duplicateConfigurationCheck(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationExists = assignmentConfigurationRepository.findByNameIgnoreCase(assignmentConfiguration.getName());
        if(assignmentConfigurationExists != null && assignmentConfiguration.getName().equalsIgnoreCase(assignmentConfigurationExists.getName())){
        		throw new ApplicationException("Assignment Configuration is already exists with given name");
        }
	}

	private boolean duplicateCheck(AssignmentConfiguration assignmentConfiguration) {
		return getProductsAndConditions(assignmentConfiguration);
	}
	
	public AssignmentConfiguration update(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
    	
		StringBuilder totalRecordKeyFromUI = new StringBuilder();
    	if(assignmentConfiguration.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        assignmentConfiguration.setLastModifiedDate(new Date());
        AssignmentConfiguration assignmentConfigurationExists = assignmentConfigurationRepository.findByNameIgnoreCase(assignmentConfiguration.getName());
        if(assignmentConfigurationExists != null && (assignmentConfigurationExists.getId().longValue() != assignmentConfiguration.getId().longValue())){
        	throw new ApplicationException("Assignment Configuration is already exists with given name");
        }
        if(!assignmentConfiguration.isDefault()){
        	boolean isExists = duplicateCheck(assignmentConfiguration);
        	if(isExists){
	    		throw new ApplicationException("Record is already exists");
	    	}
        }
        assignmentConfiguration.setTotalRecordKey(totalRecordKeyFromUI.toString());
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
	
	@SuppressWarnings("unchecked")
	public boolean getProductsAndConditions(AssignmentConfiguration assignmentConfiguration) {
		StringBuilder queryBuilder = new StringBuilder("select a.id as id, a.id as assignment_id, a.name as assignment_name, c.id as cid,c.record_key as crecordkey,p.id as pid,p.record_key as precordkey from sm_assignment_configuration a LEFT JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id LEFT JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id where ");
		StringBuilder socBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		StringBuilder productIdBuilder = new StringBuilder();
		StringBuilder socIdBuilder = new StringBuilder();
		String socIds = null;
		String productIds = null;
		boolean noSocFlag = false;
		boolean noProductFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
				socBuilder.append("'").append(socConfig.getRecordKey()).append("'");
				if(socConfig.getId() != null){
					socIdBuilder.append(socConfig.getId());
					socIdBuilder.append(",");
				}
			}
			queryBuilder.append(" c.record_key IN (");
			queryBuilder.append(socBuilder.toString());
			queryBuilder.append(")");
			noSocFlag = true;
			socIds = socIdBuilder.toString().substring(0, socIdBuilder.lastIndexOf(","));
		}
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
				productBuilder.append("'").append(productConfig.getRecordKey()).append("'");
				if(productConfig.getId() != null){
					productIdBuilder.append(productConfig.getId());
					productIdBuilder.append(",");
				}
			}
			queryBuilder.append(" and p.record_key IN (");
			queryBuilder.append(productBuilder.toString());
			queryBuilder.append(")");
			noProductFlag = true;
			productIds = productIdBuilder.toString().substring(0, productIdBuilder.lastIndexOf(","));
		}
		if(!noSocFlag){
			queryBuilder.append(" and c.record_key is null");
		}
		if(!noProductFlag){
			queryBuilder.append(" and p.record_key is null");
		} 
		
		if(socIds != null){
			queryBuilder.append(" and c.id NOT IN (");
			queryBuilder.append(socIds);
			queryBuilder.append(") ");
		}
		if(productIds != null){
			queryBuilder.append(" and p.id NOT IN (");
			queryBuilder.append(productIds);
			queryBuilder.append(") ");
		}
		
		Query query = entityManager.createNativeQuery(queryBuilder.toString(), AssignmentDTO.class);
		List<AssignmentDTO> records = query.getResultList();
		if(!CollectionUtils.isEmpty(records)){
			return true;
		}
		
		return false;
	}

}
