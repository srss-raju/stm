package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TaskTemplateService {
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	
	@Autowired
	private AssessmentActionRepository assessmentActionRepository;

	public TaskTemplate createTaskTemplate(TaskTemplate taskTemplate, MultipartFile[] attachments) {
		return taskTemplateRepository.save(taskTemplate);
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
