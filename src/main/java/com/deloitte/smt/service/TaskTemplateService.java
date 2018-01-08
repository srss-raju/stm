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

import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.TaskTemplateProducts;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.TaskTemplateProductsRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;

@Transactional
@Service
public class TaskTemplateService {
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	
	@Autowired
	private TaskTemplateProductsRepository taskTemplateProductsRepository;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;
	
	@PersistenceContext
	private EntityManager entityManager;

	public TaskTemplate createTaskTemplate(TaskTemplate taskTemplate) throws ApplicationException {
		taskTemplate.setCreatedDate(new Date());
		if(StringUtils.isEmpty(taskTemplate.getName())){
			throw exceptionBuilder.buildException(ErrorType.NO_NAME, null);
		}
		Long taskTemplateExists=taskTemplateRepository.countTaskTemplateByNameIgnoreCase(taskTemplate.getName());
		if(taskTemplateExists>0){
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENTACCTION_NAME_DUPLICATE, null);
		}
		if(duplicateRecordCheck(taskTemplate)){
			throw exceptionBuilder.buildException(ErrorType.DUPLICATE_RECORD, null);
		}
		return taskTemplateRepository.save(taskTemplate);
	}

	@SuppressWarnings("unchecked")
	private boolean duplicateRecordCheck(TaskTemplate taskTemplate) {
		boolean duplicateFlag = false;
		StringBuilder queryBuilder = new StringBuilder("select DISTINCT a.id from sm_task_template_products a where a.record_key IN (");
		List<TaskTemplateProducts> products = taskTemplate.getProducts();
		StringBuilder productBuilder = new StringBuilder();
		if(!CollectionUtils.isEmpty(products)){
			for(TaskTemplateProducts product : products){
				if(product.getId() == null){
					productBuilder.append("'").append(product.getRecordKey()).append("'");
					productBuilder.append(",");
				}
			}
			String productBuilderValue = null;
			if(!StringUtils.isEmpty(productBuilder.toString())){
				productBuilderValue = productBuilder.toString().substring(0, productBuilder.lastIndexOf(","));
				queryBuilder.append(productBuilderValue);
			}
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

	public TaskTemplate updateTaskTemplate(TaskTemplate template) throws ApplicationException {
		
		TaskTemplate taskTemplate = taskTemplateRepository.findByNameIgnoreCase(template.getName());
		if(taskTemplate != null && (taskTemplate.getId().intValue() != template.getId().intValue())){
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENTACCTION_NAME_DUPLICATE, null);
		}
		
		if(duplicateRecordCheck(template)){
			throw exceptionBuilder.buildException(ErrorType.DUPLICATE_RECORD, null);
		}
		
		if(template.getDeletedProductIds() != null){
			deleteProducts(template.getDeletedProductIds());
		}
		
		return taskTemplateRepository.save(template);
	}

	public void delete(Long templateId) throws ApplicationException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(templateId);
		if(taskTemplate == null) {
            throw new ApplicationException("Failed to delete Task. Invalid Id received");
        }
		taskTemplateRepository.delete(taskTemplate);
	}
	
	void deleteProducts(List<Long> ids){
		for(Long id:ids){
			taskTemplateProductsRepository.deleteById(id);
		}
	}
	
	public List<TaskTemplate> findAll() {
		return taskTemplateRepository.findAllByOrderByCreatedDateDesc();
	}

	public TaskTemplate findById(Long templateId) throws ApplicationException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(templateId);
		if(taskTemplate == null) {
			throw new ApplicationException("Template not found with given Id : "+templateId);
		}
		return taskTemplate;
	}
	/**
	 * 
	 * @param id
	 * @param name
	 * @throws ApplicationException
	 */
	public void updateTaskTemplateName(Long id,String name) throws ApplicationException{
		List<TaskTemplate> listTasks=taskTemplateRepository.findAll();
		List<String> taskTemplateNames=taskTemplateRepository.findByName(name, id);
		
		for (TaskTemplate taskTemplate : listTasks) {

			if (id.equals(taskTemplate.getId()) && taskTemplateNames.contains(name)) {
				throw exceptionBuilder.buildException(ErrorType.ASSESSMENTACCTION_NAME_DUPLICATE, null);
			} else if (id.equals(taskTemplate.getId()) && !taskTemplateNames.contains(name)) {
				taskTemplate.setName(name);
				taskTemplateRepository.save(taskTemplate);
			}
		}
	}
	
	public List<Task> findAllByTemplateId(Long templateId) {
        return taskTemplateRepository.findAllByTemplateId(templateId);
    }
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @throws ApplicationException
	 */
	public void updateRiskTaskName(Long id,String name) throws ApplicationException{
		List<TaskTemplate> listRiskTasks=taskTemplateRepository.findAll();
		List<String> taskTemplateNames=taskTemplateRepository.findByName(name, id);
		
		for (TaskTemplate risktaskTemplate : listRiskTasks) {

			if (id.equals(risktaskTemplate.getId()) && taskTemplateNames.contains(name)) {
				throw exceptionBuilder.buildException(ErrorType.RISKPACTION_NAME_DUPLICATE, null);
			} else if (id.equals(risktaskTemplate.getId()) && !taskTemplateNames.contains(name)) {
				risktaskTemplate.setName(name);
				taskTemplateRepository.save(risktaskTemplate);
			}
		}
	}
}
