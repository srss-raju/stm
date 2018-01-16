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
import org.springframework.util.StringUtils;

import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConditionValuesRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.repository.AssignmentProductValuesRepository;

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
    AssignmentConditionRepository socAssignmentConfigurationRepository;
    
    @Autowired
    AssignmentProductRepository productAssignmentConfigurationRepository;
    
    @Autowired
    AssignmentConditionValuesRepository assignmentConditionRepository;
    
    @Autowired
    AssignmentProductValuesRepository assignmentProductRepository;
    

    public AssignmentConfiguration insert(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
    	assignmentConfiguration.setCreatedDate(new Date());
        assignmentConfiguration.setLastModifiedDate(new Date());
        
        duplicateConfigurationCheck(assignmentConfiguration);
        
        boolean isExists = duplicateCheck(assignmentConfiguration);
        if(isExists){
    		throw new ApplicationException("Record already exists");
    	}
        return assignmentConfigurationRepository.save(assignmentConfiguration);
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
       assignmentConfigurationRepository.save(assignmentConfiguration);
        
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
        
        return assignmentConfiguration;
    }
    
    public List<AssignmentConfiguration> findAll(){
          return assignmentConfigurationRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }
    
    
	@SuppressWarnings("unchecked")
	public boolean getProductsAndConditions(AssignmentConfiguration assignmentConfiguration) {
		StringBuilder queryBuilder = new StringBuilder("select a.id, a.name, c.id as cid,c.record_key as crecordkey,p.id as pid,p.record_key as precordkey from sm_assignment_configuration a LEFT JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id LEFT JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id where ");
		StringBuilder socBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		boolean noSocFlag;
		boolean noProductFlag;
		
		StringBuilder socUpdateBuilder = new StringBuilder();
		StringBuilder productUpdateBuilder = new StringBuilder();
		String socUpdateString = null;
		String productUpdateString = null;
		
		noSocFlag = addConditions(assignmentConfiguration, queryBuilder, socBuilder, socUpdateBuilder);
		noProductFlag = addProducts(assignmentConfiguration, queryBuilder, productBuilder, noSocFlag, productUpdateBuilder);
		
		if(!StringUtils.isEmpty(socUpdateBuilder.toString())){
			socUpdateString = socUpdateBuilder.toString().substring(0, socUpdateBuilder.lastIndexOf(","));
		}
		
		if(!StringUtils.isEmpty(productUpdateBuilder.toString())){
			productUpdateString = productUpdateBuilder.toString().substring(0, productUpdateBuilder.lastIndexOf(","));
		}
		
		
		if(!noSocFlag){
			queryBuilder.append(" and c.record_key is null");
			queryBuilder.append(" and c.id is null");
		}else{
			if(!StringUtils.isEmpty(socUpdateString)){
				queryBuilder.append(" and c.id not in (");
				queryBuilder.append(socUpdateString);
				queryBuilder.append(")");
			}
		}
		if(!noProductFlag){
			queryBuilder.append(" and p.record_key is null");
			queryBuilder.append(" and p.id is null");
		}else{
			if(!StringUtils.isEmpty(productUpdateString)){
				queryBuilder.append(" and p.id not in (");
				queryBuilder.append(productUpdateString);
				queryBuilder.append(")");
			}
		}
		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		List<Object> records = query.getResultList();
		if(!CollectionUtils.isEmpty(records)){
			return true;
		}
		return false;
	}

	private boolean addProducts(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder productBuilder, boolean noSocFlag, StringBuilder productUpdateBuilder) {
		boolean noProductFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
				productBuilder.append("'").append(productConfig.getRecordKey()).append("'");
				if(productConfig.getId() != null){
					productUpdateBuilder.append(productConfig.getId());
					productUpdateBuilder.append(",");
				}
				productBuilder.append(",");
			}
			if(noSocFlag){
				queryBuilder.append(" and p.record_key IN (");
			}else{
				queryBuilder.append(" p.record_key IN (");
			}
			String productBuilderValue = productBuilder.toString().substring(0, productBuilder.lastIndexOf(","));
			queryBuilder.append(productBuilderValue);
			queryBuilder.append(")");
			noProductFlag = true;
		}
		return noProductFlag;
	}

	private boolean addConditions(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder socBuilder, StringBuilder socUpdateBuilder) {
		boolean noSocFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
				socBuilder.append("'").append(socConfig.getRecordKey()).append("'");
				if(socConfig.getId() != null){
					socUpdateBuilder.append(socConfig.getId());
					socUpdateBuilder.append(",");
				}
				socBuilder.append(",");
				
			}
			String socBuilderValue = socBuilder.toString().substring(0, socBuilder.lastIndexOf(","));
			queryBuilder.append(" c.record_key IN (");
			queryBuilder.append(socBuilderValue);
			queryBuilder.append(")");
			noSocFlag = true;
		}
		return noSocFlag;
	}

}
