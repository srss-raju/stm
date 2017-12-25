package com.deloitte.smt.service;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.SocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.ProductAssignmentConfigurationRepository;
import com.deloitte.smt.repository.SocAssignmentConfigurationRepository;
import com.deloitte.smt.util.AssignmentUtil;

/**
 * Created by RKB on 04-04-2017.
 */
@Transactional
@Service
public class AssignmentService {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@Autowired
    AssignmentConfigurationService assignmentConfigurationService;
    
    @Autowired
    SocAssignmentConfigurationRepository socAssignmentConfigurationRepository;
    
    @Autowired
    ProductAssignmentConfigurationRepository productAssignmentConfigurationRepository;
    
	
	
	
	@SuppressWarnings("unchecked")
	public AssignmentConfiguration getAssignmentConfiguration(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO) throws ApplicationException {
		
		AssignmentConfiguration assignmentConfigurationFromDB = null;
		StringBuilder queryBuilder = new StringBuilder("select DISTINCT a.id from sm_assignment_configuration a ");
		StringBuilder socBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		if(CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			assignmentConfiguration.setProductEmptyFlag(true);
		}
		if(CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			assignmentConfiguration.setConditionEmptyFlag(true);
		}
		updateQueryBuilder(assignmentConfiguration, queryBuilder);
		queryBuilder.append(" where ");
		boolean socsPresentFlag = updateQueryBuilderWithSocs(assignmentConfiguration, queryBuilder, socBuilder);
		updateQueryBuilderWithProducts(assignmentConfiguration, queryBuilder, productBuilder, socsPresentFlag);
		updateQueryBuilderWithFlags(assignmentConfiguration, queryBuilder);
		
		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		List<Object> records = query.getResultList();
		
		if(!CollectionUtils.isEmpty(records)){
			if(records.size() > 1){
				assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
			}else{
				BigInteger id = (BigInteger) records.get(0);
				assignmentConfigurationFromDB = assignmentConfigurationService.findById(id.longValue());
				if(assignmentConfigurationFromDB == null){
					assignmentConfigurationFromDB = assignmentConfigurationService.findById(id.longValue());
				}
			}
		}else{
			
			assignmentConfigurationFromDB = getAssignmentConfigurationRecursively(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB);
			
		}
	
	return assignmentConfigurationFromDB;
	}

	private void updateQueryBuilder(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()) ){
			queryBuilder.append(" INNER JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id ");
		}
		if(assignmentConfiguration.isConditionEmptyFlag()){
			queryBuilder.append(" LEFT JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id ");
		}
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			queryBuilder.append(" INNER JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id  ");
		}
		if(assignmentConfiguration.isProductEmptyFlag()){
			queryBuilder.append(" LEFT JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id  ");
		}
	}
	
	private boolean updateQueryBuilderWithSocs(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder socBuilder) {
		boolean socsPresentFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
				socBuilder.append("'").append(socConfig.getRecordKey()).append("'");
				socBuilder.append(",");
			}
			String socBuilderValue = socBuilder.toString().substring(0, socBuilder.lastIndexOf(","));
			queryBuilder.append(" c.record_key IN (");
			queryBuilder.append(socBuilderValue);
			queryBuilder.append(")");
			socsPresentFlag = true;
		}
		return socsPresentFlag;
	}
	
	private boolean updateQueryBuilderWithProducts(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder productBuilder, boolean socsPresentFlag) {
		boolean productsPresentFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
				productBuilder.append("'").append(productConfig.getRecordKey()).append("'");
				productBuilder.append(",");
			}
			if(socsPresentFlag){
				queryBuilder.append(" and p.record_key IN (");
			}else{
				queryBuilder.append("  p.record_key IN (");
			}
			String productBuilderValue = productBuilder.toString().substring(0, productBuilder.lastIndexOf(","));
			queryBuilder.append(productBuilderValue);
			queryBuilder.append(")");
			productsPresentFlag = true;
		}
		return productsPresentFlag;
	}
	
	private void updateQueryBuilderWithFlags(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder) {
		if(assignmentConfiguration.isConditionEmptyFlag()){
			queryBuilder.append(" and c.record_key is null");
		}
		if(assignmentConfiguration.isProductEmptyFlag()){
			queryBuilder.append(" and p.record_key is null");
		}
	}
	
	private AssignmentConfiguration getAssignmentConfigurationRecursively(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		if(!assignmentConfiguration.isRepeatProductFlag() && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			assignmentConfigurationFromDB = getAssignmentConfigurationWithProducts(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB);
		}
		
		
		if(!assignmentConfiguration.isRepeatSocFlag() && (assignmentConfigurationFromDB == null && (!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())))){
			assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
			assignmentConfigurationFromDB = getAssignmentConfigurationWithConditions(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB);
		}
		
		if(assignmentConfigurationFromDB == null && (!assignmentConfiguration.isProductEmptyFlag()) && (!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()))){
			assignmentConfiguration.setConditions(conditionProductDTO.getConditions());
			assignmentConfiguration.setProducts(null);
			assignmentConfiguration.setProductEmptyFlag(true);
			assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		}
		
		if(assignmentConfigurationFromDB == null  && (!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()))){
			assignmentConfiguration.setProducts(null);
			assignmentConfiguration.setConditions(conditionProductDTO.getConditions());
			assignmentConfigurationFromDB = getAssignmentConfigurationWithConditionsOnly(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB);
		}
		
		if(assignmentConfigurationFromDB == null && (!assignmentConfiguration.isConditionEmptyFlag()) && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
			assignmentConfiguration.setConditions(null);
			assignmentConfiguration.setConditionEmptyFlag(true);
			assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		}
		if(assignmentConfigurationFromDB == null && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			assignmentConfiguration.setConditions(null);
			assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
			assignmentConfigurationFromDB = getAssignmentConfigurationWithProductsOnly(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB);
		}
		assignmentConfigurationFromDB = getDefaultAssignmentConfiguration(assignmentConfigurationFromDB);
		return assignmentConfigurationFromDB;
	}
	
	private AssignmentConfiguration getAssignmentConfigurationWithProducts(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		if(assignmentConfigurationFromDB == null){
			if(assignmentConfiguration.isConditionEmptyFlag()){
				assignmentConfiguration.setConditions(null);
			}
			for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
				String recordKey = productConfig.getRecordKey();
					while(recordKey != null && assignmentConfigurationFromDB == null && (!assignmentConfiguration.isRepeatProductFlag())){
						recordKey = AssignmentUtil.getRecordKey(recordKey);
						if(recordKey != null){
							productConfig.setRecordKey(recordKey);
							assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
						}else{
							assignmentConfiguration.setRepeatProductFlag(true);
							break;
						}
						
					}
			}
		}
		return assignmentConfigurationFromDB;
	}
	
	private AssignmentConfiguration getAssignmentConfigurationWithConditions(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		if(assignmentConfigurationFromDB == null){
			if(assignmentConfiguration.isProductEmptyFlag()){
				assignmentConfiguration.setProducts(null);
			}
			for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
				String recordKey = socConfig.getRecordKey();
					while(recordKey != null && assignmentConfigurationFromDB == null && (!assignmentConfiguration.isRepeatSocFlag())){
						recordKey = AssignmentUtil.getRecordKey(recordKey);
						if(recordKey != null){
							socConfig.setRecordKey(recordKey);
							assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
						}else{
							assignmentConfiguration.setRepeatSocFlag(true);
							break;
						}
					}
			}
		}
		return assignmentConfigurationFromDB;
	}
	
	private AssignmentConfiguration getDefaultAssignmentConfiguration(AssignmentConfiguration assignmentConfigurationFromDB) {
		if(assignmentConfigurationFromDB == null){
			assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
		}
		return assignmentConfigurationFromDB;
	}
	
	
	private AssignmentConfiguration getAssignmentConfigurationWithProductsOnly(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		if(assignmentConfigurationFromDB == null){
			for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
				String recordKey = productConfig.getRecordKey();
					while(recordKey != null && assignmentConfigurationFromDB == null){
						recordKey = AssignmentUtil.getRecordKey(recordKey);
						if(recordKey != null){
							productConfig.setRecordKey(recordKey);
							assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
						}else{
							assignmentConfiguration.setRepeatProductFlag(true);
							break;
						}
						
					}
			}
		}
		return assignmentConfigurationFromDB;
	}
	
	private AssignmentConfiguration getAssignmentConfigurationWithConditionsOnly(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		if(assignmentConfigurationFromDB == null){
			for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
				String recordKey = socConfig.getRecordKey();
					while(recordKey != null && assignmentConfigurationFromDB == null){
						recordKey = AssignmentUtil.getRecordKey(recordKey);
						if(recordKey != null){
							socConfig.setRecordKey(recordKey);
							assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
						}else{
							assignmentConfiguration.setRepeatSocFlag(true);
							break;
						}
					}
			}
		}
		return assignmentConfigurationFromDB;
	}
	
	
}
