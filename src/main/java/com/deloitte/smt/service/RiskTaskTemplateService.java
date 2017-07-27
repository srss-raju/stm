package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.RiskTaskTemplate;
import com.deloitte.smt.entity.TaskTemplateIngrediant;
import com.deloitte.smt.exception.ApplicationException;
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

	public RiskTaskTemplate createTaskTemplate(RiskTaskTemplate riskTaskTemplate) {
		riskTaskTemplate.setCreatedDate(new Date());
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

	public RiskTaskTemplate updateTaskTemplate(RiskTaskTemplate template) {
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
}
