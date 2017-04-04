package com.deloitte.smt.service;

import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TopicRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    TaskInstRepository taskInstRepository;

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

    public List<Topic> findAllByStatus(String statuses, String deleteReason) {
        List<TaskInst> taskInsts = null;
        //Get all by statuses and delete reason
        if (!StringUtils.isEmpty(statuses) && !StringUtils.isEmpty(deleteReason)) {
            taskInsts = taskInstRepository.findAllByTaskDefKeyInAndDeleteReasonNot(Arrays.asList(statuses.split(",")), deleteReason);
        }
        //Get all by statuses
        else if (!StringUtils.isEmpty(statuses)) {
            taskInsts = taskInstRepository.findAllByTaskDefKeyIn(Arrays.asList(statuses.split(",")));
        }
        //Get all by delete reason
        else if (!StringUtils.isEmpty(deleteReason)) {
            taskInsts = taskInstRepository.findAllByDeleteReason(deleteReason);
        }
        //Get all
        else {
            taskInsts = taskInstRepository.findAll();
        }
        List<String> processIds = taskInsts.stream().map(TaskInst::getProcInstId).collect(Collectors.toList());
        return topicRepository.findAllByProcessIdIn(processIds);
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
