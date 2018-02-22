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
import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.util.SignalUtil;

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
    AssignmentConditionRepository socAssignmentConfigurationRepository;
    
    @Autowired
    AssignmentProductRepository productAssignmentConfigurationRepository;
    
	
	
	
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
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()) || !CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			queryBuilder.append(" where ");
		}
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
			queryBuilder.append(" INNER JOIN sm_assignment_condition c ON a.id = c.assignment_configuration_id ");
		}
		if(assignmentConfiguration.isConditionEmptyFlag()){
			queryBuilder.append(" LEFT JOIN sm_assignment_condition c ON a.id = c.assignment_configuration_id ");
		}
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			queryBuilder.append(" INNER JOIN sm_assignment_product p ON a.id = p.assignment_configuration_id  ");
		}
		if(assignmentConfiguration.isProductEmptyFlag()){
			queryBuilder.append(" LEFT JOIN sm_assignment_product p ON a.id = p.assignment_configuration_id  ");
		}
	}
	
	private boolean updateQueryBuilderWithSocs(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder socBuilder) {
		boolean socsPresentFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
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
			for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
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
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		assignmentConfigurationFromDBx = configWithProducts(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB, assignmentConfigurationFromDBx);
		
		
		if(!assignmentConfiguration.isRepeatSocFlag() && (assignmentConfigurationFromDBx == null && (!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())))){
			assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
			assignmentConfigurationFromDBx = getAssignmentConfigurationWithConditions(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDBx);
		}
		
		assignmentConfigurationFromDBx = setEmptyProductFlag(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDBx);
		
		if(assignmentConfigurationFromDBx == null  && (!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()))){
			assignmentConfiguration.setProducts(null);
			assignmentConfiguration.setConditions(conditionProductDTO.getConditions());
			assignmentConfigurationFromDBx = getAssignmentConfigurationWithConditionsOnly(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDBx);
		}
		
		assignmentConfigurationFromDBx = setConditions(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDBx);
		if(assignmentConfigurationFromDBx == null && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			assignmentConfiguration.setConditions(null);
			assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
			assignmentConfigurationFromDBx = getAssignmentConfigurationWithProductsOnly(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDBx);
		}
		assignmentConfigurationFromDBx = getDefaultAssignmentConfiguration(assignmentConfigurationFromDBx);
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration setConditions(
			AssignmentConfiguration assignmentConfiguration,
			ConditionProductDTO conditionProductDTO,
			AssignmentConfiguration assignmentConfigurationFromDBx)
			throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBxx = assignmentConfigurationFromDBx;
		if(assignmentConfigurationFromDBxx == null && (!assignmentConfiguration.isConditionEmptyFlag()) && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
			assignmentConfiguration.setConditions(null);
			assignmentConfiguration.setConditionEmptyFlag(true);
			assignmentConfigurationFromDBxx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		}
		return assignmentConfigurationFromDBxx;
	}

	private AssignmentConfiguration setEmptyProductFlag(
			AssignmentConfiguration assignmentConfiguration,
			ConditionProductDTO conditionProductDTO,
			AssignmentConfiguration assignmentConfigurationFromDBx)
			throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBxx = assignmentConfigurationFromDBx;
		if(assignmentConfigurationFromDBxx == null && (!assignmentConfiguration.isProductEmptyFlag()) && (!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()))){
			assignmentConfiguration.setConditions(conditionProductDTO.getConditions());
			assignmentConfiguration.setProducts(null);
			assignmentConfiguration.setProductEmptyFlag(true);
			assignmentConfigurationFromDBxx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		}
		return assignmentConfigurationFromDBxx;
	}

	private AssignmentConfiguration configWithProducts(
			AssignmentConfiguration assignmentConfiguration,
			ConditionProductDTO conditionProductDTO,
			AssignmentConfiguration assignmentConfigurationFromDB,
			AssignmentConfiguration assignmentConfigurationFromDBx)
			throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBxx = assignmentConfigurationFromDBx;
		if(!assignmentConfiguration.isRepeatProductFlag() && (!CollectionUtils.isEmpty(assignmentConfiguration.getProducts()))){
			assignmentConfigurationFromDBxx = getAssignmentConfigurationWithProducts(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB);
		}
		return assignmentConfigurationFromDBxx;
	}
	
	private AssignmentConfiguration getAssignmentConfigurationWithProducts(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(assignmentConfigurationFromDBx == null){
			if(assignmentConfiguration.isConditionEmptyFlag()){
				assignmentConfiguration.setConditions(null);
			}
			for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
				String recordKey = productConfig.getRecordKey();
					assignmentConfigurationFromDBx = setRepeatProductFlag(
							assignmentConfiguration, conditionProductDTO,
							assignmentConfigurationFromDBx, productConfig,
							recordKey);
			}
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration setRepeatProductFlag(
			AssignmentConfiguration assignmentConfiguration,
			ConditionProductDTO conditionProductDTO,
			AssignmentConfiguration assignmentConfigurationFromDBx,
			AssignmentProduct productConfig, String recordKey)
			throws ApplicationException {
		String recordKeyx = recordKey;
		AssignmentConfiguration assignmentConfigurationFromDBxx = assignmentConfigurationFromDBx;
		while(recordKeyx != null && assignmentConfigurationFromDBxx == null && (!assignmentConfiguration.isRepeatProductFlag())){
			recordKeyx = SignalUtil.getRecordKey(recordKeyx);
			if(recordKeyx != null){
				productConfig.setRecordKey(recordKeyx);
				assignmentConfigurationFromDBxx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
			}else{
				assignmentConfiguration.setRepeatProductFlag(true);
				break;
			}
			
		}
		return assignmentConfigurationFromDBx;
	}
	
	private AssignmentConfiguration getAssignmentConfigurationWithConditions(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(assignmentConfigurationFromDBx == null){
			if(assignmentConfiguration.isProductEmptyFlag()){
				assignmentConfiguration.setProducts(null);
			}
			assignmentConfigurationFromDBx = getConfiguration(
					assignmentConfiguration, conditionProductDTO,
					assignmentConfigurationFromDBx);
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration getConfiguration(
			AssignmentConfiguration assignmentConfiguration,
			ConditionProductDTO conditionProductDTO,
			AssignmentConfiguration assignmentConfigurationFromDB)
			throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
			String recordKey = socConfig.getRecordKey();
				while(recordKey != null && assignmentConfigurationFromDBx == null && (!assignmentConfiguration.isRepeatSocFlag())){
					recordKey = SignalUtil.getRecordKey(recordKey);
					if(recordKey != null){
						socConfig.setRecordKey(recordKey);
						assignmentConfigurationFromDBx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
					}else{
						assignmentConfiguration.setRepeatSocFlag(true);
						break;
					}
				}
		}
		return assignmentConfigurationFromDB;
	}
	
	private AssignmentConfiguration getDefaultAssignmentConfiguration(AssignmentConfiguration assignmentConfigurationFromDB) {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(assignmentConfigurationFromDBx == null){
			assignmentConfigurationFromDBx = assignmentConfigurationRepository.findByIsDefault(true);
		}
		return assignmentConfigurationFromDBx;
	}
	
	
	private AssignmentConfiguration getAssignmentConfigurationWithProductsOnly(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(assignmentConfigurationFromDBx == null){
			assignmentConfigurationFromDBx = getProductConfiguration(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDBx);
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration getProductConfiguration(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
			String recordKey = productConfig.getRecordKey();
				while(recordKey != null && assignmentConfigurationFromDBx == null){
					recordKey = SignalUtil.getRecordKey(recordKey);
					if(recordKey != null){
						productConfig.setRecordKey(recordKey);
						assignmentConfigurationFromDBx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
					}else{
						assignmentConfiguration.setRepeatProductFlag(true);
						break;
					}
					
				}
		}
		return assignmentConfigurationFromDBx;
	}
	
	private AssignmentConfiguration getAssignmentConfigurationWithConditionsOnly(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(assignmentConfigurationFromDBx == null){
			assignmentConfigurationFromDBx = getConditionConfiguration(
					assignmentConfiguration, conditionProductDTO,
					assignmentConfigurationFromDBx);
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration getConditionConfiguration(
			AssignmentConfiguration assignmentConfiguration,
			ConditionProductDTO conditionProductDTO,
			AssignmentConfiguration assignmentConfigurationFromDB)
			throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
			String recordKey = socConfig.getRecordKey();
				while(recordKey != null && assignmentConfigurationFromDBx == null){
					recordKey = SignalUtil.getRecordKey(recordKey);
					if(recordKey != null){
						socConfig.setRecordKey(recordKey);
						assignmentConfigurationFromDBx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
					}else{
						assignmentConfiguration.setRepeatSocFlag(true);
						break;
					}
				}
		}
		return assignmentConfigurationFromDBx;
	}
	
	
}
