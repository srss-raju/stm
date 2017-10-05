package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.TaskTemplateIngrediant;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;

@Transactional
@Service
public class TaskTemplateService {
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	
	@Autowired
	private AssessmentActionRepository assessmentActionRepository;
	
	@Autowired
	private TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;

	public TaskTemplate createTaskTemplate(TaskTemplate taskTemplate) throws ApplicationException {
		taskTemplate.setCreatedDate(new Date());
		Long taskTemplateExists=taskTemplateRepository.countTaskTemplateByNameIgnoreCase(taskTemplate.getName());
		if(taskTemplateExists>0){
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE);
		}
		TaskTemplate template = taskTemplateRepository.save(taskTemplate);
		if(!CollectionUtils.isEmpty(template.getTaskTemplateIngrediant())){
			for(TaskTemplateIngrediant ingrediant : template.getTaskTemplateIngrediant()){
				ingrediant.setTaskTemplateId(template.getId());
			}
			List<TaskTemplateIngrediant> list = taskTemplateIngrediantRepository.save(template.getTaskTemplateIngrediant());
			template.setTaskTemplateIngrediant(list);
		}
		return template;
	}

	public TaskTemplate updateTaskTemplate(TaskTemplate template) {
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
		
		return taskTemplateRepository.save(template);
	}

	public void delete(Long taskId) throws ApplicationException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(taskId);
		if(taskTemplate == null) {
            throw new ApplicationException("Failed to delete Task. Invalid Id received");
        }
		taskTemplateRepository.delete(taskTemplate);
	}
	
	void deleteIngrediants(List<Long> ids){
		for(Long id:ids){
			taskTemplateIngrediantRepository.deleteById(id);
		}
	}
	
	public List<SignalAction> findAllByTemplateId(Long templateId) {
        return assessmentActionRepository.findAllByTemplateId(templateId);
    }

	public List<TaskTemplate> findAll() {
		List<TaskTemplate> templates = taskTemplateRepository.findAllByOrderByCreatedDateDesc();
		for(TaskTemplate template : templates) {
			template.setTaskTemplateIngrediant(taskTemplateIngrediantRepository.findByTaskTemplateId(template.getId()));
		}
		return templates;
	}

	public TaskTemplate findById(Long templateId) throws ApplicationException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(templateId);
		if(taskTemplate == null) {
			throw new ApplicationException("Template not found with given Id : "+templateId);
		}else{
			taskTemplate.setTaskTemplateIngrediant(taskTemplateIngrediantRepository.findByTaskTemplateId(taskTemplate.getId()));
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
				throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE);
			} else if (id.equals(taskTemplate.getId()) && !taskTemplateNames.contains(name)) {
				taskTemplate.setName(name);
				taskTemplateRepository.save(taskTemplate);
			}
		}
	}
}
