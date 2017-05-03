package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;

@Service
public class TaskTemplateService {
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	
	@Autowired
	private AssessmentActionRepository assessmentActionRepository;

	public List<TaskTemplate> createTaskTemplate(TaskTemplate taskTemplate, MultipartFile[] attachments) {
		if(!CollectionUtils.isEmpty(taskTemplate.getIngrediantNames())) {
			List<TaskTemplate> taskTemplates = new ArrayList<>();
			for(String ingrediant:taskTemplate.getIngrediantNames()){
				TaskTemplate ingrediantTaskTemplate = new TaskTemplate();
				ingrediantTaskTemplate.setIngrediantName(ingrediant);
				ingrediantTaskTemplate.setName(taskTemplate.getName());
				taskTemplates.add(ingrediantTaskTemplate);
			}
			return taskTemplateRepository.save(taskTemplates);
		}
		return null;
	}

	public List<TaskTemplate> updateTaskTemplate(TaskTemplate taskTemplate, MultipartFile[] attachments) {
		if(!CollectionUtils.isEmpty(taskTemplate.getIngrediantNames())) {
			List<TaskTemplate> taskTemplates = new ArrayList<>();
			for(String ingrediant:taskTemplate.getIngrediantNames()){
				TaskTemplate ingrediantTaskTemplate = new TaskTemplate();
				ingrediantTaskTemplate.setIngrediantName(ingrediant);
				ingrediantTaskTemplate.setName(taskTemplate.getName());
				taskTemplates.add(ingrediantTaskTemplate);
			}
			return taskTemplateRepository.save(taskTemplates);
		}
		return null;
	}

	public void delete(String name) throws DeleteFailedException {
		if(name == null) {
            throw new DeleteFailedException("Failed to delete Task. Invalid Id received");
        }
		taskTemplateRepository.deleteByName(name);
	}
	
	public List<SignalAction> findAllByTemplateId(Long templateId) {
        return assessmentActionRepository.findAllByTemplateId(templateId);
    }

	public List<TaskTemplate> findAll() {
		return taskTemplateRepository.findAll();
	}

	public TaskTemplate findById(Long templateId) throws EntityNotFoundException {
		TaskTemplate taskTemplate = taskTemplateRepository.findOne(templateId);
		if(taskTemplate == null) {
			throw new EntityNotFoundException("Template not found with given Id : "+templateId);
		}
		return taskTemplate;
	}
}
