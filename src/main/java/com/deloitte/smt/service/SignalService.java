package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TopicRepository;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    
    @Autowired
    CaseService caseService;
    
    @Autowired
    AssessmentActionRepository assessmentActionRepository;
    
    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;

    public String createTopic(Topic topic) {
        String processInstanceId = runtimeService.startProcessInstanceByKey("topicProcess").getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        taskService.delegateTask(task.getId(), "Demo Demo");
        if(topic.getId() != null) {
            topic.setId(null);
        }
        topic.setProcessId(processInstanceId);
        topicRepository.save(topic);
        return processInstanceId;
    }

    public String validateAndPrioritize(Topic topic) throws TaskNotFoundException {
        topicRepository.save(topic);
        Task task = taskService.createTaskQuery().processInstanceId(topic.getProcessId()).singleResult();
        if(task == null) {
            throw new TaskNotFoundException("Task not found for the process "+topic.getProcessId());
        }
        taskService.complete(task.getId());
        
        CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
        topic.setProcessId(instance.getCaseInstanceId());
        topicRepository.save(topic);
        return instance.getCaseInstanceId();
    }

    public List<Topic> findAllByStatus(String statuses, String deleteReason) {
        List<TaskInst> taskInsts = null;
        //Get all by statuses and delete reason
        if (!StringUtils.isEmpty(statuses) && !StringUtils.isEmpty(deleteReason)) {
            taskInsts = taskInstRepository.findAllByTaskDefKeyInAndProcInstIdNotNullAndDeleteReasonNotOrDeleteReasonNull(Arrays.asList(statuses.split(",")), deleteReason);
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
        return topicRepository.findAllByProcessIdInOrderByStartDateDesc(processIds);
    }
    
    public Long getValidateAndPrioritizeCount(){
    	return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("validateTopic","prioritizeAndTopicAssignment"));
    }
    
    public Long getAssesmentCount(){
    	return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("assesment"));
    }
    
    public Long getRiskCount(){
    	return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("risk"));
    }
    
    public void createAssessmentAction(SignalAction signalAction) {
    	Task task = taskService.newTask();
		task.setCaseInstanceId(signalAction.getCaseInstanceId());
		task.setName(signalAction.getActionName());
		taskService.saveTask(task);
		List<Task> list = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).list();
		TaskInst taskInstance = new TaskInst();
		taskInstance.setId(list.get(list.size()-1).getId());
		taskInstance.setCaseDefKey("assessment");
		taskInstance.setTaskDefKey("assessment");
		taskInstance.setCaseInstId(signalAction.getCaseInstanceId());
		taskInstance.setStartTime(new Date());
		taskInstRepository.save(taskInstance);
		assessmentActionRepository.save(signalAction);
    }

	public List<SignalAction> findAllByAssessmentId(String assessmentId, String actionStatus) {
		return assessmentActionRepository.findAllByAssessmentIdAndActionStatus(assessmentId, actionStatus);
	}

	public void createAssessmentPlan(Long topicId, AssessmentPlan assessmentPlan) {
		CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
		assessmentPlan.setCaseInstanceId(instance.getCaseInstanceId());
		assessmentPlanRepository.save(assessmentPlan);
        Topic topic = topicRepository.findOne(topicId);
        topic.setAssessmentPlan(assessmentPlan);
        topicRepository.save(topic);
	}

	public List<AssessmentPlan> findAllAssessmentPlansByStatus() {
        return new ArrayList<>();
    }
}
