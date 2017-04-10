package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@Service
public class AssessmentActionService {

    @Autowired
    private TaskService taskService;

    @Autowired
    TaskInstRepository taskInstRepository;

    @Autowired
    AssessmentActionRepository assessmentActionRepository;

    public void createAssessmentAction(SignalAction signalAction) {
        if(signalAction.getCaseInstanceId() != null && "Completed".equalsIgnoreCase(signalAction.getActionStatus())){
            Task task = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).singleResult();
            taskService.complete(task.getId());
        }
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
        if(actionStatus != null){
            return assessmentActionRepository.findAllByAssessmentIdAndActionStatus(assessmentId, actionStatus);
        }
        return assessmentActionRepository.findAllByAssessmentId(assessmentId);
    }
}