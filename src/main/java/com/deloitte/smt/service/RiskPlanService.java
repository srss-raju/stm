package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskPlanAssignees;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.TaskTemplateProducts;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.RiskPlanAssigneesRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskTemplateProductsRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RajeshKumar on 12-04-2017.
 */
@Transactional
@Service
public class RiskPlanService {
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;

	@Autowired
	AssessmentPlanRepository assessmentPlanRepository;

	@Autowired
	RiskPlanRepository riskPlanRepository;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	SignalService signalService;
	
	@Autowired
	private SignalAssignmentService signalAssignmentService;
	
	 @Autowired
	 AssessmentPlanService assessmentPlanService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CommentsRepository commentsRepository;

	@Autowired
	SignalURLRepository signalURLRepository;
	
	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	private AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@Autowired
	RiskPlanAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
	
	@Autowired
    TaskTemplateRepository riskTaskTemplateRepository;
	    
	@Autowired
	TaskTemplateProductsRepository riskTaskTemplateProductsRepository;
	    
	/**
	 * 
	 * @param riskPlan
	 * @param attachments
	 * @param assessmentId
	 * @return
	 * @throws ApplicationException
	 */
	public RiskPlan insert(RiskPlan riskPlan, Long assessmentId)
			throws ApplicationException {
		riskPlan.setStatus("New");
		Date d = new Date();
		riskPlan.setCreatedDate(d);
		riskPlan.setLastModifiedDate(d);
		riskPlan.setRiskTaskStatus("Completed");
		riskPlan.setSummary(SmtConstant.SUMMARY.getDescription());
		AssignmentConfiguration assignmentConfiguration = null;

		RiskPlan riskPlanUpdated;
		Long riskPlanExist = riskPlanRepository.countByNameIgnoreCase(riskPlan.getName());
		
		if (assessmentId != null) {
			AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
			if (assessmentPlan == null) {
				throw new ApplicationException("Assessment Plan not found with the given Id : " + assessmentId);
			}
			assessmentPlan.setRiskPlan(riskPlan);
			riskPlan.setAssessmentPlan(assessmentPlan);
			if (riskPlanExist>0) {
				throw exceptionBuilder.buildException(ErrorType.RISKPLAN_NAME_DUPLICATE, null);
			}
			
			riskPlanUpdated = riskPlanRepository.save(riskPlan);
			assessmentPlanRepository.save(assessmentPlan);
		} else {
			if (riskPlanExist>0) {
				throw exceptionBuilder.buildException(ErrorType.RISKPLAN_NAME_DUPLICATE, null);
			}
			riskPlanUpdated = riskPlanRepository.save(riskPlan);
		}
		updateSignalUrl(riskPlanUpdated);
		AssessmentPlan assessmentPlan = null;
		if(riskPlan.getAssessmentPlan().getId() != null) {
			try{
				assessmentPlan = assessmentPlanService.findById(riskPlan.getAssessmentPlan().getId());
			}catch(Exception ex){
				logger.error(ex);
			}
			Topic topic = getTopic(assessmentPlan);
			
			if(topic != null){
				
				Topic topicWithConditionsAndProducts = signalService.findById(topic.getId());
				assignmentConfiguration = signalAssignmentService.convertToAssignmentConfiguration(topicWithConditionsAndProducts);
				ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
				conditionProductDTO.setConditions(assignmentConfiguration.getConditions());
				conditionProductDTO.setProducts(assignmentConfiguration.getProducts());
				assignmentConfiguration = signalAssignmentService.getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
			}
			if(assignmentConfiguration == null){
				assignmentConfiguration = assignmentConfigurationRepository.findByIsDefault(true);
			}
			
			
			if (assignmentConfiguration != null) {
				if(assignmentConfiguration.getRiskOwner()!=null){
					riskPlan.setOwner(assignmentConfiguration.getRiskOwner());
					riskPlanUpdated.setOwner(assignmentConfiguration.getRiskOwner());
				}
				//TODO
				//riskPlanAssignmentService.saveAssignmentAssignees(assignmentConfiguration, riskPlanUpdated);
			}
		}
		return riskPlanUpdated;
	}
	private Topic getTopic(AssessmentPlan assessmentPlan) {
		Topic topic = null;
		if(assessmentPlan != null){
			Set<Topic> topics = assessmentPlanRepository.findAllSignals(assessmentPlan.getId());
			if(!CollectionUtils.isEmpty(topics)){
				for(Topic signal : topics){
					topic = signal;
					break;
				}
			}
		}
		return topic;
	}
	/**
	 * 
	 * @param riskPlanUpdated
	 */
	private void updateSignalUrl(RiskPlan riskPlanUpdated) {
		if (!CollectionUtils.isEmpty(riskPlanUpdated.getSignalUrls())) {
			for (SignalURL url : riskPlanUpdated.getSignalUrls()) {
				url.setTopicId(riskPlanUpdated.getId());
				url.setCreatedDate(riskPlanUpdated.getCreatedDate());
				url.setCreatedBy(riskPlanUpdated.getCreatedBy());
				url.setModifiedBy(riskPlanUpdated.getModifiedBy());
				url.setModifiedDate(riskPlanUpdated.getLastModifiedDate());
			}
			signalURLRepository.save(riskPlanUpdated.getSignalUrls());
		}
	}
	public List<Task> associateRiskTasks(RiskPlan riskPlan){
		List<Task> tasks = null;
		
		if(!CollectionUtils.isEmpty(riskPlan.getRiskTemplateIds())){
			List<Task> riskTaskList = new ArrayList<>();
			for(Long id:riskPlan.getRiskTemplateIds()){
				List<Task> riskTasks = riskTaskRepository.findAllByTemplateId(id);
				tasks = createRiskTask(riskTaskList, riskTasks, riskPlan);
			}
		}
		checkRiskTaskStatus(riskPlan);
		return tasks;
	}
	
	private void checkRiskTaskStatus(RiskPlan riskPlan){
		boolean allTasksCompletedFlag=true;
		List<Task> riskTaskList=riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(String.valueOf(riskPlan.getId()));
		if(!CollectionUtils.isEmpty(riskTaskList)){
			for(Task riskTask:riskTaskList){
        		if(!"Completed".equals(riskTask.getStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
			if(allTasksCompletedFlag){
				riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(),riskPlan.getId());
	        }else{
	        	riskPlanRepository.updateRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription(),riskPlan.getId());
	        }
			
		}
	}
	
	List<RiskPlanAssignees> associateRiskPlanAssignmentAssignees(List<RiskPlan> riskPlanList){
		List<RiskPlanAssignees> topicRiskPlanAssignmentAssigneesList=new ArrayList<>();
		for(RiskPlan riskPlan :riskPlanList){
			topicRiskPlanAssignmentAssigneesList =	topicRiskPlanAssignmentAssigneesRepository.findByRiskId(riskPlan.getId());
		}
		return topicRiskPlanAssignmentAssigneesList;
	}


	private List<Task> createRiskTask(List<Task> riskTaskList, List<Task> templateRiskTasks, RiskPlan riskPlan) {
		if(!CollectionUtils.isEmpty(templateRiskTasks)){
			for(Task templateTask:templateRiskTasks){
				Task riskTask = new Task();
				riskTask.setActionType(templateTask.getActionType());
				riskTask.setCreatedBy(templateTask.getCreatedBy());
				riskTask.setCreatedDate(new Date());
				riskTask.setDescription(templateTask.getDescription());
				riskTask.setDueDate(templateTask.getDueDate());
				riskTask.setName(templateTask.getName());
				riskTask.setNotes(templateTask.getNotes());
				riskTask.setStatus("New");
				riskTask.setRecipients(templateTask.getRecipients());
				riskTask.setInDays(templateTask.getInDays());
				if(templateTask.getInDays() != 0){
					riskTask.setDueDate(SignalUtil.getDueDate(templateTask.getInDays(), riskTask.getCreatedDate()));
				}
				riskTask.setRiskId(String.valueOf(riskPlan.getId()));
				riskTask = riskTaskRepository.save(riskTask);
				riskTaskList.add(riskTask);
			}
		}
		return riskTaskList;
	}
	
	/**
	 * @param sort
	 * @param action
	 * @param signalAction
	 */
	public void associateTemplateAttachments(Task riskTask, Task templateRiskTask) {
		Sort sort = new Sort(Sort.Direction.DESC, SmtConstant.CREATED_DATE.getDescription());
		List<Attachment> attachments = attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(templateRiskTask.getId(), AttachmentType.RISK_TASK_ASSESSMENT, sort);
		if (!CollectionUtils.isEmpty(attachments)) {
			List<Attachment> riskTaskAttachments = new ArrayList<>();
			for (Attachment attachment : attachments) {
				Attachment riskTaskAttachment = new Attachment();

				riskTaskAttachment.setDescription(attachment.getDescription());
				riskTaskAttachment.setAttachmentsURL(attachment.getAttachmentsURL());
				riskTaskAttachment.setAttachmentResourceId(riskTask.getId());
				riskTaskAttachment.setContentType(attachment.getContentType());
				riskTaskAttachment.setContent(attachment.getContent());
				riskTaskAttachment.setFileName(attachment.getFileName());
				riskTaskAttachment.setCreatedDate(new Date());
				riskTaskAttachment.setAttachmentType(attachment.getAttachmentType());

				riskTaskAttachments.add(riskTaskAttachment);
			}
			attachmentRepository.save(riskTaskAttachments);
		}
	}
	
	/**
	 * @param riskTask
	 * @param templateTask
	 */
	public void associateTemplateURLs(Task riskTask, Task templateRiskTask) {
		List<SignalURL> riskTemplateTaskUrls = signalURLRepository.findByTopicId(templateRiskTask.getId());
		if (!CollectionUtils.isEmpty(riskTemplateTaskUrls)) {
			List<SignalURL> riskTaskURLs = new ArrayList<>();
			for (SignalURL url : riskTemplateTaskUrls) {
				SignalURL riskTaskURL = new SignalURL();
				riskTaskURL.setDescription(url.getDescription());
				riskTaskURL.setTopicId(riskTask.getId());
				riskTaskURL.setUrl(url.getUrl());
				riskTaskURL.setModifiedDate(new Date());
				riskTaskURLs.add(riskTaskURL);
			}
			signalURLRepository.save(riskTaskURLs);
		}
	}

	
	/**
	 * 
	 * @param riskTask
	 * @param attachments
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public void createRiskTask(Task riskTask) throws ApplicationException {
		    
		Date d = new Date();
		riskTask.setCreatedDate(d);
		riskTask.setLastUpdatedDate(d);
		riskTask.setStatus("New");
		if(riskTask.getRiskId() != null){
			riskPlanRepository.findOne(Long.valueOf(riskTask.getRiskId()));
		}

		Long riskTaskExists=riskTaskRepository.countByNameIgnoreCaseAndRiskId(riskTask.getName(),riskTask.getRiskId());
		if (riskTaskExists > 0) {
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		
		Task riskTaskUpdated = riskTaskRepository.save(riskTask);
		checkRiskTaskStatus(riskTaskUpdated);
		saveUrls(riskTaskUpdated);
		
	}
	
	/**
	 * 
	 * @param riskTask
	 * @param attachments
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public void createRiskTemplateTask(Task riskTask) throws ApplicationException {
		    
		Date d = new Date();
		riskTask.setCreatedDate(d);
		riskTask.setLastUpdatedDate(d);
		riskTask.setStatus("New");
		
		Task riskTaskExists=riskTaskRepository.findByNameIgnoreCaseAndTemplateId(riskTask.getName(),riskTask.getTemplateId());
		if (riskTaskExists != null) {
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		
		Task riskTaskUpdated = riskTaskRepository.save(riskTask);
		saveUrls(riskTaskUpdated);
		
	}
	
	/**
	 * 
	 * @param riskTask
	 */
	public void checkRiskTaskStatus(Task riskTask) {
		List<Task> risks = findAllByRiskId(riskTask.getRiskId(), null);
		boolean allTasksCompletedFlag = true;
		if (!CollectionUtils.isEmpty(risks)) {
			for (Task risk : risks) {
				if (!SmtConstant.COMPLETED.getDescription().equals(risk.getStatus())) {
					allTasksCompletedFlag = false;
				}
			}
		}
		if(riskTask.getRiskId() != null){
			if(allTasksCompletedFlag){
				riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(riskTask.getRiskId()));
			}
			else{
				riskPlanRepository.updateRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription(),  Long.valueOf(riskTask.getRiskId()));
			}
		}
	}
	public Task findById(Long id) {
		Task riskTask = riskTaskRepository.findOne(id);
		if ("New".equalsIgnoreCase(riskTask.getStatus())) {
			riskTask.setStatus("In Progress");
			riskTask = riskTaskRepository.save(riskTask);
		}
		riskTask.setSignalUrls(signalURLRepository.findByTopicId(riskTask.getId()));
		return riskTask;
	}

	public List<Task> findAllByRiskId(String riskId, String status) {
		if (status != null) {
			return riskTaskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(riskId, status);
		}
		return riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(riskId);
	}

	public void delete(Long riskTaskId) throws ApplicationException {
		Task riskTask = riskTaskRepository.findOne(riskTaskId);
		if (riskTask == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		
		riskTaskRepository.delete(riskTask);
		checkRiskTaskStatus(riskTask);
	}

	public void updateRiskTask(Task riskTask) throws ApplicationException {
		if (riskTask.getId() == null) {
			throw new ApplicationException("Failed to update Action. Invalid Id received");
		}
		Task riskTaskExists;
		if(riskTask.getTemplateId() != null){
			riskTaskExists = riskTaskRepository.findByNameIgnoreCaseAndTemplateId(riskTask.getName(),riskTask.getTemplateId());
		}else{
			riskTaskExists = riskTaskRepository.findByNameIgnoreCaseAndRiskId(riskTask.getName(),riskTask.getRiskId());
		}
		if(riskTaskExists != null && (riskTaskExists.getId().intValue() != riskTask.getId().intValue())){
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		
		riskTask.setLastUpdatedDate(new Date());
		riskTaskRepository.save(riskTask);
		List<Task> risks = findAllByRiskId(riskTask.getRiskId(), null);
		boolean allTasksCompletedFlag = true;
		if (!CollectionUtils.isEmpty(risks)) {
			for (Task risk : risks) {
				if (!SmtConstant.COMPLETED.getDescription().equals(risk.getStatus())) {
					allTasksCompletedFlag = false;
				}
			}
		}
		saveUrls(riskTask);
		if (allTasksCompletedFlag) {
			riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(riskTask.getRiskId()));
		}
		
	}
	private void saveUrls(Task riskTask) {
		if (!CollectionUtils.isEmpty(riskTask.getSignalUrls())) {
			for (SignalURL url : riskTask.getSignalUrls()) {
				url.setTopicId(riskTask.getId());
				url.setModifiedDate(new Date());
			}
			signalURLRepository.save(riskTask.getSignalUrls());
		}
	}

	public RiskPlan findByRiskId(Long riskId) throws ApplicationException {
		RiskPlan riskPlan = riskPlanRepository.findOne(riskId);
		if (riskPlan == null) {
			throw new ApplicationException("Risk Plan not found with the given Id : " + riskId);
		}
		if ("New".equalsIgnoreCase(riskPlan.getStatus())) {
			riskPlan.setStatus("In Progress");
			riskPlan = riskPlanRepository.save(riskPlan);
		}
		riskPlan.setComments(commentsRepository.findByRiskPlanId(riskId));
		riskPlan.setSignalUrls(signalURLRepository.findByTopicId(riskId));
		AssessmentPlan assessmentPlan=riskPlan.getAssessmentPlan();
		if(null!=assessmentPlan){
		assessmentPlan.setSignalUrls(signalURLRepository.findByTopicId(assessmentPlan.getId()));
		}
		checkRiskTaskStatus(riskPlan);
		return riskPlan;
	}

	public void riskPlanSummary(RiskPlan riskPlan) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		riskPlan.setLastModifiedDate(new Date());
		riskPlan.setStatus(SmtConstant.COMPLETED.getDescription());
		riskPlanRepository.save(riskPlan);
	}

	public void updateRiskPlan(RiskPlan riskPlan) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		
		RiskPlan riskPlanFromDB = riskPlanRepository.findOne(riskPlan.getId());
		ownerCheck(riskPlan, riskPlanFromDB);
		
		
		riskPlan.setLastModifiedDate(new Date());
		
		if(null==riskPlan.getSummary()|| riskPlan.getSummary().isEmpty()){
		riskPlan.setSummary(SmtConstant.SUMMARY.getDescription());
			if(riskPlan.getStatus().equals(SmtConstant.COMPLETED.getDescription())){
				riskPlan.setSummary(SmtConstant.SUMMARY_COMPLETED.getDescription());
			}
		}
		else{
			riskPlan.setSummary(riskPlan.getSummary());
		}
		setRiskTaskStatus(riskPlan);
		riskPlanRepository.save(riskPlan);
		
		
		List<Comments> list = riskPlan.getComments();
		if (!CollectionUtils.isEmpty(list)) {
			for (Comments comment : list) {
				comment.setRiskPlanId(riskPlan.getId());
				comment.setModifiedDate(new Date());
			}
		}
		commentsRepository.save(riskPlan.getComments());
		
		updateSignalUrl(riskPlan);
	}
	private void ownerCheck(RiskPlan riskPlan, RiskPlan riskPlanFromDB)
			throws ApplicationException {
		if(riskPlanFromDB != null && riskPlan.getOwner()!=null && riskPlanFromDB.getOwner() != null && (!riskPlanFromDB.getOwner().equalsIgnoreCase(riskPlan.getOwner()))){
			throw new ApplicationException("Unable to make you as a Owner");
		}
	}
	
	
	/**
	 * This method sets the risk task status as completed 
     * when all tasks are completed else Not completed
	 * @param riskPlan
	 */
	private void setRiskTaskStatus(RiskPlan riskPlan){
		boolean riskTaskStatus=false;
		List<Task> riskTaskStatues=	riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(String.valueOf(riskPlan.getId()));
		 if(!CollectionUtils.isEmpty(riskTaskStatues)){
			 for(Task riskTask:riskTaskStatues){
				 if(!riskTask.getStatus().equals(SmtConstant.COMPLETED.getDescription())){
					 riskTaskStatus=true;
				 }
			 }
			 if(riskTaskStatus){
				 riskPlan.setRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription());
			 }
		 }
	}
		 
	public List<Task> associateRiskTemplateTasks(RiskPlan riskPlan) {
		return associateRiskTasks(riskPlan);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @throws ApplicationException
	 */
	public void updateRiskName(Long id,String name) throws ApplicationException{
		List<RiskPlan> list = riskPlanRepository.findAll();
		List<String> riskPlanNames = riskPlanRepository.findByRiskName(name, id);

		for (RiskPlan riskPlan : list) {

			if (id.equals(riskPlan.getId()) && riskPlanNames.contains(name)) {
				throw exceptionBuilder.buildException(ErrorType.RISKPLAN_NAME_DUPLICATE, null);
			} else if (id.equals(riskPlan.getId()) && !riskPlanNames.contains(name)) {
				riskPlan.setLastModifiedDate(new Date());
				riskPlan.setName(name);
				riskPlanRepository.save(riskPlan);
			}
		}
	}
	public List<TaskTemplate> getTaskTamplatesOfRiskProducts(Long riskPlanId) throws ApplicationException {
		List<TaskTemplate> taskTemplates = new ArrayList<>();
		AssessmentPlan assessmentPlan = null;
		RiskPlan riskPlanFromDB = findByRiskId(riskPlanId);
		try{
			assessmentPlan = assessmentPlanService.findById(riskPlanFromDB.getAssessmentPlan().getId());
		}catch(Exception ex){
			logger.error(ex);
		}
		Topic topic = getTopic(assessmentPlan);
		
		if(topic != null){
			Topic topicWithConditionsAndProducts = signalService.findById(topic.getId());
			if(!CollectionUtils.isEmpty(topicWithConditionsAndProducts.getProducts())){
				getTaskTemplates(taskTemplates, topicWithConditionsAndProducts);
			}
		}
		//AssignmentUtil
		return taskTemplates;
	}

	private void getTaskTemplates(List<TaskTemplate> taskTemplates, Topic topicWithConditionsAndProducts) {
		for(TopicProduct recordKey : topicWithConditionsAndProducts.getProducts()){
			TaskTemplateProducts taskTemplateProducts = getTaskTemplateProduct(recordKey);
			if(taskTemplateProducts != null){
				Long taskTemplateId = riskTaskTemplateProductsRepository.findTemplateId(taskTemplateProducts.getId());
				taskTemplates.add(riskTaskTemplateRepository.findOne(taskTemplateId));
			}
		}
	}

	private TaskTemplateProducts getTaskTemplateProduct(TopicProduct recordKey) {
		return riskTaskTemplateProductsRepository.findByRecordKey(recordKey.getRecordKey());
	}
}
