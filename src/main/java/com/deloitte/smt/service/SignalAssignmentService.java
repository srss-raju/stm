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

import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.SocAssignmentConfiguration;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignmentCondition;
import com.deloitte.smt.entity.TopicAssignmentProduct;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;
import com.deloitte.smt.entity.TopicSignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.ProductAssignmentConfigurationRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SocAssignmentConfigurationRepository;
import com.deloitte.smt.repository.TopicAssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.util.AssignmentUtil;

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
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
    @Autowired
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
    @Autowired
    TopicSignalValidationAssignmentAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
    
    @Autowired
    TopicAssessmentAssignmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
    
    @Autowired
    TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
    
    @Autowired
    AssignmentConfigurationService assignmentConfigurationService;
    
    @Autowired
    SocAssignmentConfigurationRepository socAssignmentConfigurationRepository;
    
    @Autowired
    ProductAssignmentConfigurationRepository productAssignmentConfigurationRepository;

	public Topic saveSignalAssignmentAssignees(AssignmentConfiguration assignmentConfiguration, Topic topicUpdated) {
		assignmentConfiguration.setSignalAssignees(signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfiguration.getId()));
		return setTopicAssignmentConfigurationAssignees(assignmentConfiguration, topicUpdated);
	}
    
	/**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private Topic setTopicAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, Topic topic) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSignalAssignees())){
        	List<TopicSignalValidationAssignmentAssignees> list = new ArrayList<>();
			for(SignalValidationAssignmentAssignees svaAssignees : assignmentConfiguration.getSignalAssignees()){
        		saveTopicSignalValidationAssignmentAssignees(svaAssignees,topic,list);
        	}
			topic.setTopicSignalValidationAssignmentAssignees(topicSignalValidationAssignmentAssigneesRepository.save(list));
        }
		return topic;
	}
	
	private void saveTopicSignalValidationAssignmentAssignees(SignalValidationAssignmentAssignees svaAssignees, Topic topic, List<TopicSignalValidationAssignmentAssignees> list) {
		TopicSignalValidationAssignmentAssignees assignee = new TopicSignalValidationAssignmentAssignees();
		assignee.setTopicId(topic.getId());
		assignee.setAssignTo(svaAssignees.getAssignTo());
		assignee.setUserGroupKey(svaAssignees.getUserGroupKey());
		assignee.setUserKey(svaAssignees.getUserKey());
		assignee.setCreatedDate(topic.getCreatedDate());
		assignee.setCreatedBy(topic.getCreatedBy());
		assignee.setTopic(topic);
		list.add(assignee);
	}

	public void findSignalAssignmentAssignees(Topic topic) {
		topic.setTopicSignalValidationAssignmentAssignees(topicSignalValidationAssignmentAssigneesRepository.findByTopicId(topic.getId()));
	}
	
	public AssignmentConfiguration convertToAssignmentConfiguration(Topic topic){
		List<SocAssignmentConfiguration> conditions;
		List<ProductAssignmentConfiguration> products;
		AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
		if(!CollectionUtils.isEmpty(topic.getConditions())){
			conditions = new ArrayList<>();
			for(TopicSocAssignmentConfiguration topicSocConfig : topic.getConditions()){
				SocAssignmentConfiguration socConfig = new SocAssignmentConfiguration();
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
			for(TopicProductAssignmentConfiguration topicProductConfig : topic.getProducts()){
				ProductAssignmentConfiguration productConfig = new ProductAssignmentConfiguration();
				productConfig.setAssignmentConfigurationId(topicProductConfig.getAssignmentConfigurationId());
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
	
	
	private void setProductRecordValues(TopicProductAssignmentConfiguration topicProductConfig, ProductAssignmentConfiguration productConfig) {
		List<AssignmentProduct> records = null;
		if(!CollectionUtils.isEmpty(topicProductConfig.getRecordValues())){
			for(TopicAssignmentProduct record : topicProductConfig.getRecordValues()){
				records = new ArrayList<>();
				AssignmentProduct ap = new AssignmentProduct();
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

	private void setSocRecordValues(TopicSocAssignmentConfiguration topicSocConfig, SocAssignmentConfiguration socConfig) {
		List<AssignmentCondition> records = null;
		if(!CollectionUtils.isEmpty(topicSocConfig.getRecordValues())){
			for(TopicAssignmentCondition record : topicSocConfig.getRecordValues()){
				records = new ArrayList<>();
				AssignmentCondition ac = new AssignmentCondition();
				ac.setAssignmentConfigurationId(record.getAssignmentConfigurationId());
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
	public AssignmentConfiguration getAssignmentConfiguration(AssignmentConfiguration assignmentConfiguration) throws ApplicationException {
		AssignmentConfiguration assignmentConfigurationFromDB = null;
		StringBuilder queryBuilder = new StringBuilder("select DISTINCT a.id from sm_assignment_configuration a INNER JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id INNER JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id where ");
		
		StringBuilder queryBuilder2 = new StringBuilder("select DISTINCT a.id from sm_assignment_configuration a ");
		
		StringBuilder socBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		boolean noSocFlag = false;
		boolean noProductFlag = false;
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions()) ){
			queryBuilder2.append(" INNER JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id ");
		}
		if(assignmentConfiguration.isConditionFlag()){
			queryBuilder2.append(" LEFT JOIN sm_soc_assignment_configuration c ON a.id = c.assignment_configuration_id ");
		}
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			queryBuilder2.append(" INNER JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id  ");
		}
		if(assignmentConfiguration.isProductFlag()){
			queryBuilder2.append(" LEFT JOIN sm_product_assignment_configuration p ON a.id = p.assignment_configuration_id  ");
		}
		queryBuilder2.append(" where ");
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
				socBuilder.append("'").append(socConfig.getRecordKey()).append("'");
				socBuilder.append(",");
			}
			String socBuilderValue = socBuilder.toString().substring(0, socBuilder.lastIndexOf(","));
			queryBuilder2.append(" c.record_key IN (");
			queryBuilder2.append(socBuilderValue);
			queryBuilder2.append(")");
			noSocFlag = true;
		}
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
				productBuilder.append("'").append(productConfig.getRecordKey()).append("'");
				productBuilder.append(",");
			}
			if(noSocFlag){
				queryBuilder2.append(" and p.record_key IN (");
			}else{
				queryBuilder2.append("  p.record_key IN (");
			}
			String productBuilderValue = productBuilder.toString().substring(0, productBuilder.lastIndexOf(","));
			queryBuilder2.append(productBuilderValue);
			queryBuilder2.append(")");
			noProductFlag = true;
		} 
		if(assignmentConfiguration.isConditionFlag()){
			queryBuilder2.append(" and c.record_key is null");
		}
		if(assignmentConfiguration.isProductFlag()){
			queryBuilder2.append(" and p.record_key is null");
		}
		Query query = entityManager.createNativeQuery(queryBuilder2.toString());
		List<Object> records = query.getResultList();
		if(!CollectionUtils.isEmpty(records)){
			if(records.size() > 1){
				assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
			}else{
				BigInteger id = (BigInteger) records.get(0);
				if(!noSocFlag){
					if(!CollectionUtils.isEmpty(socAssignmentConfigurationRepository.findByAssignmentConfigurationId(id.longValue()))){
						assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
					}
				}
				if(!noProductFlag){
					if(!CollectionUtils.isEmpty(productAssignmentConfigurationRepository.findByAssignmentConfigurationId(id.longValue()))){
						assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
					}
				}
				if(assignmentConfigurationFromDB == null){
					assignmentConfigurationFromDB = assignmentConfigurationService.findById(id.longValue());
				}
			}
		}else{
			boolean emptyFlag = false;
			if(!noProductFlag){
				// Only Socs
				if(assignmentConfiguration.getConditions().size() == 1){
					for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
						 String key = AssignmentUtil.getRecordKey(socConfig.getRecordKey());
						if(!StringUtils.isEmpty(key)){
							socConfig.setRecordKey(key);
							assignmentConfiguration.setProductFlag(true);
						}else{
							emptyFlag = true;
							assignmentConfiguration.setProductFlag(false);
						}
					}
				}else{
					emptyFlag = true;
				}
				if(!emptyFlag){
					assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration);
				}
				
			}
			
			if(!noSocFlag){
				// Only Products
				if(assignmentConfiguration.getProducts().size() == 1){
					for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
						 String key = AssignmentUtil.getRecordKey(productConfig.getRecordKey());
						if(!StringUtils.isEmpty(key)){
							productConfig.setRecordKey(key);
							assignmentConfiguration.setConditionFlag(true);
						}else{
							emptyFlag = true;
							assignmentConfiguration.setConditionFlag(false);
						}
					}
				}else{
					emptyFlag = true;
				}
				if(!emptyFlag){
					assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration);
				}
			}
			
			//if records not available for product and condition 
			if(noSocFlag && noProductFlag){
				assignmentConfiguration.setProducts(null);
				assignmentConfiguration.setProductFlag(true);
				assignmentConfigurationFromDB = getAssignmentConfiguration(assignmentConfiguration);
			}
			if(assignmentConfigurationFromDB == null){
				assignmentConfigurationFromDB = assignmentConfigurationRepository.findByIsDefault(true);
			}
			
		}
		return assignmentConfigurationFromDB;
	}

}
