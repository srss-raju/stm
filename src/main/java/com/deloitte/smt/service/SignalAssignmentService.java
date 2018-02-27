package com.deloitte.smt.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConditionValues;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.entity.AssignmentProductValues;
import com.deloitte.smt.entity.AssignmentSignalAssignees;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignees;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicConditionValues;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.entity.TopicProductValues;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.repository.RiskPlanAssigneesRepository;
import com.deloitte.smt.repository.TopicAssigneesRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RKB on 31-07-2017.
 */
@Transactional
@Service
public class SignalAssignmentService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    AssignmentConfigurationRepository assignmentConfigurationRepository;
	
    @Autowired
    TopicAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
    
    @Autowired
    AssignmentConfigurationService assignmentConfigurationService;
    
    @Autowired
    AssignmentConditionRepository socAssignmentConfigurationRepository;
    
    @Autowired
    AssignmentProductRepository productAssignmentConfigurationRepository;
    
    public Topic saveSignalAssignmentAssignees(AssignmentConfiguration assignmentConfiguration, Topic topicUpdated) {
		return setTopicAssignmentConfigurationAssignees(assignmentConfiguration, topicUpdated);
	}
    
	/**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private Topic setTopicAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, Topic topic) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSignalAssignees())){
        	List<TopicAssignees> list = new ArrayList<>();
			for(AssignmentSignalAssignees svaAssignees : assignmentConfiguration.getSignalAssignees()){
        		saveTopicSignalValidationAssignmentAssignees(svaAssignees,topic,list);
        	}
			topic.setTopicAssignees(topicSignalValidationAssignmentAssigneesRepository.save(list));
        }
		return topic;
	}
	
	private void saveTopicSignalValidationAssignmentAssignees(AssignmentSignalAssignees svaAssignees, Topic topic, List<TopicAssignees> list) {
		TopicAssignees assignee = new TopicAssignees();
		assignee.setUserGroupKey(svaAssignees.getUserGroupKey());
		assignee.setUserKey(svaAssignees.getUserKey());
		assignee.setCreatedDate(topic.getCreatedDate());
		assignee.setCreatedBy(topic.getCreatedBy());
		assignee.setTopic(topic);
		list.add(assignee);
	}
    
    public AssignmentConfiguration convertToAssignmentConfiguration(Topic topic){
		List<AssignmentCondition> conditions;
		List<AssignmentProduct> products;
		AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
		if(!CollectionUtils.isEmpty(topic.getConditions())){
			conditions = new ArrayList<>();
			for(TopicCondition topicSocConfig : topic.getConditions()){
				AssignmentCondition socConfig = new AssignmentCondition();
				socConfig.setAssignmentConfigurationId(topicSocConfig.getAssignmentConfigurationId());
				socConfig.setConditionName(topicSocConfig.getConditionName());
				socConfig.setCreatedBy(topicSocConfig.getCreatedBy());
				socConfig.setCreatedDate(topicSocConfig.getCreatedDate());
				socConfig.setId(topicSocConfig.getId());
				socConfig.setLastModifiedBy(topicSocConfig.getLastModifiedBy());
				socConfig.setLastModifiedDate(topicSocConfig.getLastModifiedDate());
				socConfig.setRecordKey(topicSocConfig.getRecordKey());
				setSocRecordValues(topicSocConfig, socConfig);
				conditions.add(socConfig);
			}
			assignmentConfiguration.setConditions(conditions);
		}
		
		if(!CollectionUtils.isEmpty(topic.getProducts())){
			products = new ArrayList<>();
			for(TopicProduct topicProductConfig : topic.getProducts()){
				AssignmentProduct productConfig = new AssignmentProduct();
				productConfig.setCreatedBy(topicProductConfig.getCreatedBy());
				productConfig.setCreatedDate(topicProductConfig.getCreatedDate());
				productConfig.setId(topicProductConfig.getId());
				productConfig.setLastModifiedBy(topicProductConfig.getLastModifiedBy());
				productConfig.setLastModifiedDate(topicProductConfig.getLastModifiedDate());
				productConfig.setProductName(topicProductConfig.getProductName());
				productConfig.setRecordKey(topicProductConfig.getRecordKey());
				setProductRecordValues(topicProductConfig, productConfig);
				products.add(productConfig);
			}
			assignmentConfiguration.setProducts(products);
		}
		return assignmentConfiguration;
		
	}
	
	
	private void setProductRecordValues(TopicProduct topicProductConfig, AssignmentProduct productConfig) {
		List<AssignmentProductValues> records = null;
		if(!CollectionUtils.isEmpty(topicProductConfig.getRecordValues())){
			for(TopicProductValues record : topicProductConfig.getRecordValues()){
				records = new ArrayList<>();
				AssignmentProductValues ap = new AssignmentProductValues();
				ap.setAssignmentConfigurationId(record.getAssignmentConfigurationId());
				ap.setCategory(record.getCategory());
				ap.setCategoryCode(record.getCategoryCode());
				ap.setCategoryDesc(record.getCategoryDesc());
				ap.setCreatedBy(record.getCreatedBy());
				ap.setCreatedDate(record.getCreatedDate());
				ap.setId(record.getId());
				ap.setLastModifiedDate(record.getLastModifiedDate());
				records.add(ap);
			}
			productConfig.setRecordValues(records);
		}
		
	}

	private void setSocRecordValues(TopicCondition topicSocConfig, AssignmentCondition socConfig) {
		List<AssignmentConditionValues> records = null;
		if(!CollectionUtils.isEmpty(topicSocConfig.getRecordValues())){
			for(TopicConditionValues record : topicSocConfig.getRecordValues()){
				records = new ArrayList<>();
				AssignmentConditionValues ac = new AssignmentConditionValues();
				ac.setCategory(record.getCategory());
				ac.setCategoryCode(record.getCategoryCode());
				ac.setCategoryDesc(record.getCategoryDesc());
				ac.setCreatedBy(record.getCreatedBy());
				ac.setCreatedDate(record.getCreatedDate());
				ac.setId(record.getId());
				ac.setLastModifiedDate(record.getLastModifiedDate());
				records.add(ac);
			}
			socConfig.setRecordValues(records);
		}
	}

	@SuppressWarnings("unchecked")
	public AssignmentConfiguration getAssignmentConfiguration(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDB = null;
		StringBuilder queryBuilder = new StringBuilder("select DISTINCT a.id from sm_assignment_configuration a ");
		
		StringBuilder socBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		boolean noSocFlag = false;
		boolean noProductFlag = false;
		
		buildQuery(assignmentConfiguration, queryBuilder);
		queryBuilder.append(" where ");
		noSocFlag = buildQueryWithConditions(assignmentConfiguration,queryBuilder, socBuilder, noSocFlag);
		noProductFlag = buildQueryWithProducts(assignmentConfiguration, queryBuilder, productBuilder, noSocFlag, noProductFlag); 
		buildQueryWithKey(assignmentConfiguration, queryBuilder);
		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		List<Object> records = query.getResultList();
		if(!CollectionUtils.isEmpty(records)){
			if(records.size() > 1){
				assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
			}else{
				
				assignmentConfigurationFromDB = getResult(assignmentConfigurationFromDB, noSocFlag, noProductFlag, records);
			}
		}else{
			boolean emptyFlag = false;
			if(!noProductFlag){
				// Only Socs
				if(assignmentConfiguration.getConditions().size() == 1){
					emptyFlag = setProductFlag(assignmentConfiguration,
							emptyFlag);
				}else{
					emptyFlag = true;
				}
				if(!emptyFlag){
					assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
				}
				
			}
			
			assignmentConfigurationFromDB = configWithProducts(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB, noSocFlag, emptyFlag);
			
			//if records not available for product and condition 
			assignmentConfigurationFromDB = configWithNoConditionNoProduct(assignmentConfiguration, conditionProductDTO, assignmentConfigurationFromDB, noSocFlag, noProductFlag);
			assignmentConfigurationFromDB = defaultConfiguration(assignmentConfigurationFromDB);
			
		}
		return assignmentConfigurationFromDB;
	}

	private AssignmentConfiguration configWithProducts(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB, boolean noSocFlag, boolean emptyFlag) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		boolean emptyFlagx = emptyFlag;
		if(!noSocFlag){
			// Only Products
			if(assignmentConfiguration.getProducts().size() == 1){
				emptyFlagx = setConditionFlag(assignmentConfiguration, emptyFlagx);
			}else{
				emptyFlagx = true;
			}
			if(!emptyFlagx){
				assignmentConfigurationFromDBx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
			}
		}
		return assignmentConfigurationFromDBx;
	}

	public AssignmentConfiguration getResult(AssignmentConfiguration assignmentConfigurationFromDB, boolean noSocFlag, boolean noProductFlag, List<Object> records) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		BigInteger id = (BigInteger) records.get(0);
		AssignmentConfiguration assignmentConfig = assignmentConfigurationRepository.findOne(id.longValue());
		if((!noSocFlag) && (!CollectionUtils.isEmpty(assignmentConfig.getConditions()))){
			assignmentConfigurationFromDBx = assignmentConfigurationRepository.findByIsDefault(true);
		}
		if((!noProductFlag) && (!CollectionUtils.isEmpty(assignmentConfig.getProducts()))){
			assignmentConfigurationFromDBx = assignmentConfigurationRepository.findByIsDefault(true);
		}
		if(assignmentConfigurationFromDBx == null){
			assignmentConfigurationFromDBx = assignmentConfigurationService.findById(id.longValue());
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration defaultConfiguration(AssignmentConfiguration assignmentConfigurationFromDB) {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(assignmentConfigurationFromDBx == null){
			assignmentConfigurationFromDBx = assignmentConfigurationRepository.findByIsDefault(true);
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration configWithNoConditionNoProduct(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB, boolean noSocFlag, boolean noProductFlag) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		if(noSocFlag && noProductFlag){
			if(!assignmentConfiguration.isRepeatProductFlag()){
				assignmentConfigurationFromDBx = setRepeatProductFlag(
						assignmentConfiguration, conditionProductDTO,
						assignmentConfigurationFromDBx);
			}
			
			if((!assignmentConfiguration.isRepeatSocFlag()) && assignmentConfigurationFromDBx == null){
				assignmentConfiguration.setProducts(conditionProductDTO.getProducts());
				assignmentConfigurationFromDBx = setRepeatSocFlag(
						assignmentConfiguration, conditionProductDTO,
						assignmentConfigurationFromDB);
			}
			
		}
		return assignmentConfigurationFromDBx;
	}

	private boolean buildQueryWithProducts(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder productBuilder, boolean noSocFlag, boolean noProductFlag) {
		boolean noProductFlagx = noProductFlag;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
				productBuilder.append("'").append(productConfig.getRecordKey()).append("'");
				productBuilder.append(",");
			}
			if(noSocFlag){
				queryBuilder.append(" and p.record_key IN (");
			}else{
				queryBuilder.append("  p.record_key IN (");
			}
			String productBuilderValue = productBuilder.toString().substring(0, productBuilder.lastIndexOf(","));
			queryBuilder.append(productBuilderValue);
			queryBuilder.append(")");
			noProductFlagx = true;
		}
		return noProductFlagx;
	}

	private boolean buildQueryWithConditions(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder, StringBuilder socBuilder, boolean noSocFlag) {
		boolean noSocFlagx = noSocFlag;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
				socBuilder.append("'").append(socConfig.getRecordKey()).append("'");
				socBuilder.append(",");
			}
			String socBuilderValue = socBuilder.toString().substring(0, socBuilder.lastIndexOf(","));
			queryBuilder.append(" c.record_key IN (");
			queryBuilder.append(socBuilderValue);
			queryBuilder.append(")");
			noSocFlagx = true;
		}
		return noSocFlagx;
	}

	private void buildQueryWithKey(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder) {
		if(assignmentConfiguration.isConditionFlag()){
			queryBuilder.append(" and c.record_key is null");
		}
		if(assignmentConfiguration.isProductFlag()){
			queryBuilder.append(" and p.record_key is null");
		}
	}

	private void buildQuery(AssignmentConfiguration assignmentConfiguration, StringBuilder queryBuilder) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()) ){
			queryBuilder.append(" INNER JOIN sm_assignment_condition c ON a.id = c.assignment_configuration_id ");
		}
		if(assignmentConfiguration.isConditionFlag()){
			queryBuilder.append(" LEFT JOIN sm_assignment_condition c ON a.id = c.assignment_configuration_id ");
		}
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			queryBuilder.append(" INNER JOIN sm_assignment_product p ON a.id = p.assignment_configuration_id  ");
		}
		if(assignmentConfiguration.isProductFlag()){
			queryBuilder.append(" LEFT JOIN sm_assignment_product p ON a.id = p.assignment_configuration_id  ");
		}
	}

	private boolean setProductFlag(AssignmentConfiguration assignmentConfiguration, boolean emptyFlag) {
		boolean emptyFlagx = emptyFlag;
		for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
			 String key = SignalUtil.getRecordKey(socConfig.getRecordKey());
			if(!StringUtils.isEmpty(key)){
				socConfig.setRecordKey(key);
				assignmentConfiguration.setProductFlag(true);
			}else{
				emptyFlagx = true;
				assignmentConfiguration.setProductFlag(false);
			}
		}
		return emptyFlagx;
	}

	private AssignmentConfiguration setRepeatProductFlag(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
			String recordKey = productConfig.getRecordKey();
			if(assignmentConfigurationFromDBx == null){
				while(recordKey != null && assignmentConfigurationFromDBx == null && (!assignmentConfiguration.isRepeatProductFlag())){
					recordKey = SignalUtil.getRecordKey(recordKey);
					assignmentConfigurationFromDBx = setRProductFlag(
							assignmentConfiguration, conditionProductDTO,
							assignmentConfigurationFromDBx, productConfig,
							recordKey);
					
				}
			}
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration setRProductFlag(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDBx, AssignmentProduct productConfig, String recordKey) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBxx = assignmentConfigurationFromDBx;
		if(recordKey != null){
			productConfig.setRecordKey(recordKey);
			assignmentConfigurationFromDBxx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		}else{
			assignmentConfiguration.setRepeatProductFlag(true);
		}
		return assignmentConfigurationFromDBxx;
	}

	private AssignmentConfiguration setRepeatSocFlag(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDB) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBx = assignmentConfigurationFromDB;
		for(AssignmentCondition socConfig : assignmentConfiguration.getConditions()){
			String recordKey = socConfig.getRecordKey();
			if(assignmentConfigurationFromDBx == null){
				while(recordKey != null && assignmentConfigurationFromDBx == null && (!assignmentConfiguration.isRepeatSocFlag())){
					recordKey = SignalUtil.getRecordKey(recordKey);
					assignmentConfigurationFromDBx = setRSocFlag(
							assignmentConfiguration, conditionProductDTO,
							assignmentConfigurationFromDBx, socConfig,
							recordKey);
				}
			}
		}
		return assignmentConfigurationFromDBx;
	}

	private AssignmentConfiguration setRSocFlag(AssignmentConfiguration assignmentConfiguration, ConditionProductDTO conditionProductDTO, AssignmentConfiguration assignmentConfigurationFromDBx, AssignmentCondition socConfig, String recordKey) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDBxx = assignmentConfigurationFromDBx;
		if(recordKey != null){
			socConfig.setRecordKey(recordKey);
			assignmentConfigurationFromDBxx = getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		}else{
			assignmentConfiguration.setRepeatSocFlag(true);
		}
		return assignmentConfigurationFromDBxx;
	}

	private boolean setConditionFlag(AssignmentConfiguration assignmentConfiguration, boolean emptyFlag) {
		boolean emptyFlagx = emptyFlag;
		for(AssignmentProduct productConfig : assignmentConfiguration.getProducts()){
			 String key = SignalUtil.getRecordKey(productConfig.getRecordKey());
			if(!StringUtils.isEmpty(key)){
				productConfig.setRecordKey(key);
				assignmentConfiguration.setConditionFlag(true);
			}else{
				emptyFlagx = true;
				assignmentConfiguration.setConditionFlag(false);
			}
		}
		return emptyFlagx;
	}

}
