package com.deloitte.smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.repository.TaskTemplateRepository;

@Service
public class TaskTemplateService {
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;

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
        return taskTemplateRepository.findAllByTemplateId(templateId);
    }

	public List<TaskTemplate> findAll() {
		return taskTemplateRepository.findAll();
	}

	public List<SignalAction> findTaskById(Long taskId) {
		return null;
	}

}
