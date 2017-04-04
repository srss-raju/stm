package com.deloitte.smt.service;

import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.repository.TopicRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Service
public class SignalService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TopicRepository topicRepository;

    public String createTopic() {
        topicRepository.save(new Topic());
        String processInstanceId = runtimeService.startProcessInstanceByKey("topicProcess").getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        taskService.delegateTask(task.getId(), "Demo Demo");
        return processInstanceId;
    }

    public void validateTopic(String processInstanceId) throws TaskNotFoundException {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if(task == null) {
            throw new TaskNotFoundException("Task not found for the process "+processInstanceId);
        }
        taskService.complete(task.getId());
    }

    public void prioritizeTopic(String processInstanceId) throws TaskNotFoundException {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if(task == null) {
            throw new TaskNotFoundException("Task not found for the process "+processInstanceId);
        }
        taskService.complete(task.getId());
    }
    
    public int getValidateAndPrioritizeCount(){
    	return 0;
    }
    
    public int getAssesmentCount(){
    	return 0;
    }
    
    public int getRiskCount(){
    	return 0;
    }
}
