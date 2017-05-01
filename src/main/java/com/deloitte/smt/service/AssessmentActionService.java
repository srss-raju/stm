package com.deloitte.smt.service;

import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.util.SignalUtil;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    AttachmentService attachmentService;

    public void createAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws IOException {
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
        signalAction.setTaskId(taskInstance.getId());
        taskInstRepository.save(taskInstance);
        Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setLastModifiedDate(d);
        signalAction.setActionStatus("New");
        signalAction = assessmentActionRepository.save(signalAction);
        attachmentService.addAttachments(signalAction.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, null, signalAction.getFileMetadata());
    }

    public void updateAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(signalAction.getId() == null) {
            throw new UpdateFailedException("Failed to update Action. Invalid Id received");
        }
        if("completed".equalsIgnoreCase(signalAction.getActionStatus())) {
            taskService.complete(signalAction.getTaskId());
        }
        signalAction.setLastModifiedDate(new Date());
        assessmentActionRepository.save(signalAction);
        attachmentService.addAttachments(signalAction.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, signalAction.getDeletedAttachmentIds(), signalAction.getFileMetadata());
    }

    public SignalAction findById(Long id) {
        SignalAction signalAction = assessmentActionRepository.findOne(id);
        if("New".equalsIgnoreCase(signalAction.getActionStatus())){
            signalAction.setActionStatus("In Progress");
            signalAction = assessmentActionRepository.save(signalAction);
        }
        return signalAction;
    }

    public List<SignalAction> findAllByAssessmentId(String assessmentId, String actionStatus) {
        if(actionStatus != null){
            return assessmentActionRepository.findAllByAssessmentIdAndActionStatus(assessmentId, actionStatus);
        }
        return assessmentActionRepository.findAllByAssessmentId(assessmentId);
    }

    public void delete(Long assessmentActionId, String taskId) throws DeleteFailedException {
        SignalAction signalAction = assessmentActionRepository.findOne(assessmentActionId);
        if(signalAction == null) {
            throw new DeleteFailedException("Failed to delete Action. Invalid Id received");
        }
        assessmentActionRepository.delete(signalAction);
        if(taskId != null){
        	taskService.deleteTask(taskId);
        }
    }
    
    public void createOrphanAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws IOException {
    	Date d = new Date();
        signalAction.setCreatedDate(d);
    	SignalUtil.getDueDate(signalAction.getDaysLeft(), signalAction.getCreatedDate());
    	assessmentActionRepository.save(signalAction);
    	attachmentService.addAttachments(signalAction.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, null, signalAction.getFileMetadata());
    }
    
}
