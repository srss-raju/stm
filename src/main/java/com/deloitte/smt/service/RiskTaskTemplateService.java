package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.RiskTaskTemplate;
import com.deloitte.smt.entity.RiskTaskTemplateProducts;
import com.deloitte.smt.entity.TaskTemplateIngrediant;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.RiskTaskTemplateRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;

@Transactional
@Service
public class RiskTaskTemplateService {
	
	@Autowired
	private RiskTaskTemplateRepository riskTaskTemplateRepository;
	
	@Autowired
	private RiskTaskRepository riskTaskRepository;
	
	@Autowired
	private TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;
	@Autowired
	private ExceptionBuilder exceptionBuilder;
	
	@PersistenceContext
	private EntityManager entityManager;

	public RiskTaskTemplate createTaskTemplate(RiskTaskTemplate riskTaskTemplate) throws ApplicationException {
		if(StringUtils.isEmpty(riskTaskTemplate.getName())){
			throw exceptionBuilder.buildException(ErrorType.NO_NAME, null);
		}
		riskTaskTemplate.setCreatedDate(new Date());
		Long countRiskTask=riskTaskTemplateRepository.countRiskTaskTemplateByNameIgnoreCase(riskTaskTemplate.getName());
		if(countRiskTask>0){
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		if(duplicateRecordCheck(riskTaskTemplate)){
			throw exceptionBuilder.buildException(ErrorType.DUPLICATE_RECORD, null);
		}
		RiskTaskTemplate template = riskTaskTemplateRepository.save(riskTaskTemplate);
		if(!CollectionUtils.isEmpty(template.getTaskTemplateIngrediant())){
			for(TaskTemplateIngrediant ingrediant : template.getTaskTemplateIngrediant()){
				ingrediant.setTaskTemplateId(template.getId());
			}
			List<TaskTemplateIngrediant> list = taskTemplateIngrediantRepository.save(template.getTaskTemplateIngrediant());
			template.setTaskTemplateIngrediant(list);
		}
		return template;
	}
	
	@SuppressWarnings("unchecked")
	private boolean duplicateRecordCheck(RiskTaskTemplate taskTemplate) {
		boolean duplicateFlag = false;
		StringBuilder queryBuilder = new StringBuilder("select DISTINCT a.id from sm_risk_task_template_products a where a.record_key IN (");
		List<RiskTaskTemplateProducts> products = taskTemplate.getProducts();
		StringBuilder productBuilder = new StringBuilder();
		if(!CollectionUtils.isEmpty(products)){
			for(RiskTaskTemplateProducts product : products){
				if(product.getId() == null){
					productBuilder.append("'").append(product.getRecordKey()).append("'");
					productBuilder.append(",");
				}
			}
			String productBuilderValue = productBuilder.toString().substring(0, productBuilder.lastIndexOf(","));
			queryBuilder.append(productBuilderValue);
			queryBuilder.append(")");
			if(!StringUtils.isEmpty(productBuilderValue)){
				Query query = entityManager.createNativeQuery(queryBuilder.toString());
				List<Object> records = query.getResultList();
				if(!CollectionUtils.isEmpty(records)){
					duplicateFlag = true;
				}
			}
		}
		return duplicateFlag;
	}
	

	public RiskTaskTemplate updateTaskTemplate(RiskTaskTemplate template) throws ApplicationException {
		if(duplicateRecordCheck(template)){
			throw exceptionBuilder.buildException(ErrorType.DUPLICATE_RECORD, null);
		}
		if(template.getDeletedIngrediantIds() != null){
			deleteIngrediants(template.getDeletedIngrediantIds());
		}
		if(!CollectionUtils.isEmpty(template.getTaskTemplateIngrediant())){
			for(TaskTemplateIngrediant ingrediant : template.getTaskTemplateIngrediant()){
				ingrediant.setTaskTemplateId(template.getId());
			}
			List<TaskTemplateIngrediant> list = taskTemplateIngrediantRepository.save(template.getTaskTemplateIngrediant());
			template.setTaskTemplateIngrediant(list);
		}
		
		return riskTaskTemplateRepository.save(template);
	}

	public void delete(Long taskId) throws ApplicationException {
		RiskTaskTemplate taskTemplate = riskTaskTemplateRepository.findOne(taskId);
		if(taskTemplate == null) {
            throw new ApplicationException("Failed to delete Task. Invalid Id received");
        }
		riskTaskTemplateRepository.delete(taskTemplate);
	}
	
	void deleteIngrediants(List<Long> ids){
		for(Long id:ids){
			taskTemplateIngrediantRepository.deleteById(id);
		}
	}
	
	public List<RiskTask> findAllByTemplateId(Long templateId) {
        return riskTaskRepository.findAllByTemplateId(templateId);
    }

	public List<RiskTaskTemplate> findAll() {
		List<RiskTaskTemplate> templates = riskTaskTemplateRepository.findAllByOrderByCreatedDateDesc();
		for(RiskTaskTemplate template : templates) {
			template.setTaskTemplateIngrediant(taskTemplateIngrediantRepository.findByTaskTemplateId(template.getId()));
		}
		return templates;
	}

	public RiskTaskTemplate findById(Long templateId) throws ApplicationException {
		RiskTaskTemplate taskTemplate = riskTaskTemplateRepository.findOne(templateId);
		if(taskTemplate == null) {
			throw new ApplicationException("Template not found with given Id : "+templateId);
		}else{
			taskTemplate.setTaskTemplateIngrediant(taskTemplateIngrediantRepository.findByTaskTemplateId(taskTemplate.getId()));
		}
		return taskTemplate;
	}
	
	public List<RiskTaskTemplate> getTaskTamplatesOfIngrediant(String ingrediantName) {
		List<Long> templateIds = taskTemplateIngrediantRepository.findByIngrediantName(ingrediantName);
		return riskTaskTemplateRepository.findByIdIn(templateIds);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @throws ApplicationException
	 */
	public void updateRiskTaskName(Long id,String name) throws ApplicationException{
		List<RiskTaskTemplate> listRiskTasks=riskTaskTemplateRepository.findAll();
		List<String> taskTemplateNames=riskTaskTemplateRepository.findByName(name, id);
		
		for (RiskTaskTemplate risktaskTemplate : listRiskTasks) {

			if (id.equals(risktaskTemplate.getId()) && taskTemplateNames.contains(name)) {
				throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
			} else if (id.equals(risktaskTemplate.getId()) && !taskTemplateNames.contains(name)) {
				risktaskTemplate.setName(name);
				riskTaskTemplateRepository.save(risktaskTemplate);
			}
		}
	}
}
