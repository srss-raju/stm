package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssessmentPlanStatus;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.exception.TopicNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TopicRepository;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Service
public class SignalService {

    private static final Logger LOG = Logger.getLogger(SignalService.class);

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
    AssessmentPlanRepository assessmentPlanRepository;
    
    @Autowired
    RiskPlanRepository riskPlanRepository;

    @Autowired
    AttachmentService attachmentService;

    public Topic findById(Long topicId){
        Topic topic = topicRepository.findOne(topicId);
        if(null == topic.getSignalConfirmation()) {
            topic.setSignalConfirmation("Validated Signal");
        }
        if(null == topic.getSignalValidation()) {
            topic.setSignalValidation("In Progress");
        }
        return topic;
    }

    public String createTopic(Topic topic, MultipartFile[] attachments) throws IOException {
        String processInstanceId = runtimeService.startProcessInstanceByKey("topicProcess").getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        taskService.delegateTask(task.getId(), "Demo Demo");
        if(topic.getId() != null) {
            topic.setId(null);
        }
        topic.setCreatedDate(new Date());
        if(null == topic.getSignalConfirmation()) {
            topic.setSignalConfirmation("Validated Signal");
        }
        if(null == topic.getSignalValidation()) {
            topic.setSignalValidation("In Progress");
        }
        topic.setSignalStatus("In Progress");
        topic.setProcessId(processInstanceId);
        topic = topicRepository.save(topic);
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, null);
        return processInstanceId;
    }

    public String updateTopic(Topic topic, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(topic.getId() == null) {
            throw new UpdateFailedException("Update failed for Topic, since it does not have any valid Id field.");
        }
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, topic.getDeletedAttachmentIds());
        topicRepository.save(topic);
        return "Update Success";
    }

    public String validateAndPrioritize(Long topicId, AssessmentPlan assessmentPlan) throws TaskNotFoundException, TopicNotFoundException {
        Topic topic = topicRepository.findOne(topicId);
        if(topic == null) {
            throw new TopicNotFoundException("Topic not found with the given Id ["+topicId+"]");
        }
        Task task = taskService.createTaskQuery().processInstanceId(topic.getProcessId()).singleResult();
        if(task == null) {
            throw new TaskNotFoundException("Task not found for the process "+topic.getProcessId());
        }
        taskService.complete(task.getId());
        
        CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
        topic.setProcessId(instance.getCaseInstanceId());
        assessmentPlan.setAssessmentPlanStatus(AssessmentPlanStatus.ACTION_PLAN.getDescription());
        assessmentPlan.setCaseInstanceId(instance.getCaseInstanceId());
        assessmentPlan = assessmentPlanRepository.save(assessmentPlan);
        topic.setAssessmentPlan(assessmentPlan);
        topic.setSignalStatus("Complete");
        topic.setSignalValidation("Complete");
        topicRepository.save(topic);

        return instance.getCaseInstanceId();
    }

    public List<Topic> findAllByStatus(String statuses, String deleteReason, Topic t) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        List<Topic> topics;
        List<TaskInst> taskInsts = null;
        List<String> processIds;
        //Get all by statuses and delete reason
        if (!StringUtils.isEmpty(statuses) && !StringUtils.isEmpty(deleteReason)) {
            taskInsts = taskInstRepository.findAllByTaskDefKeyInAndDeleteReason(Arrays.asList(statuses.split(",")), deleteReason);
        }
        //Get all by statuses
        else if (!StringUtils.isEmpty(statuses)) {
            taskInsts = taskInstRepository.findAllByTaskDefKeyIn(Arrays.asList(statuses.split(",")));
        }
        //Get all by delete reason
        else if (!StringUtils.isEmpty(deleteReason)) {
            taskInsts = taskInstRepository.findAllByDeleteReason(deleteReason);
        }

        if(taskInsts != null) {
            processIds = taskInsts.stream().map(TaskInst::getProcInstId)
                    .filter(s -> !StringUtils.isEmpty(s))
                    .collect(Collectors.toList());
            topics = topicRepository.findAllByProcessIdInOrderByStartDateDesc(processIds);
        } else if(t != null) {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase();
            Example<Topic> topicEx = Example.of(t, matcher);
            topics = topicRepository.findAll(topicEx, sort);
        } else {
            LOG.info("Running query to get all Signals..");
            topics = topicRepository.findAll(sort);
        }

        topics.stream().forEach(topic->{
            if(null == topic.getSignalConfirmation()) {
                topic.setSignalConfirmation("Validated Signal");
            }
            if(null == topic.getSignalValidation()) {
                topic.setSignalValidation("In Progress");
            }
        });
        return topics;
    }

    public Long getValidateAndPrioritizeCount(){
        return topicRepository.count();
    	//return taskInstRepository.countByTaskDefKeyInAndDeleteReasonNot(Arrays.asList("validateTopic"), "completed");
    }
    
    public Long getAssessmentCount(){
    	///return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("assessment"));
        return assessmentPlanRepository.count();
    }
    
    public Long getRiskCount(){
    	return riskPlanRepository.count();
    	//return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("risk"));
    }
}
