package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.TaskTemplateIngrediant;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;

@Service
public class TaskTemplateService {
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	
	@Autowired
	private AssessmentActionRepository assessmentActionRepository;
	
	@Autowired
	private TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;

	public TaskTemplate createTaskTemplate(TaskTemplate taskTemplate, MultipartFile[] attachments) {
		taskTemplate.setCreatedDate(new Date());
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

	public TaskTemplate updateTaskTemplate(TaskTemplate taskTemplate, MultipartFile[] attachments) {
		return taskTemplateRepository.save(taskTemplate);
	}

	public void delete(Long taskId) throws DeleteFailedException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(taskId);
		if(taskTemplate == null) {
            throw new DeleteFailedException("Failed to delete Task. Invalid Id received");
        }
		taskTemplateRepository.delete(taskTemplate);
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

	public TaskTemplate findById(Long templateId) throws EntityNotFoundException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(templateId);
		if(taskTemplate == null) {
			throw new EntityNotFoundException("Template not found with given Id : "+templateId);
		}
		return taskTemplate;
	}
}
