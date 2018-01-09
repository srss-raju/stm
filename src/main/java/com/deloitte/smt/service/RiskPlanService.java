package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.RiskTaskTemplate;
import com.deloitte.smt.entity.RiskTaskTemplateProducts;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;
import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.RiskTaskTemplateProductsRepository;
import com.deloitte.smt.repository.RiskTaskTemplateRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.util.JsonUtil;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RajeshKumar on 12-04-2017.
 */
@Transactional
@Service
public class RiskPlanService {
	private static final Logger LOG = Logger.getLogger(RiskPlanService.class);
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;

	@Autowired
	AssessmentPlanRepository assessmentPlanRepository;

	@Autowired
	RiskPlanRepository riskPlanRepository;

	@Autowired
	RiskTaskRepository riskTaskRepository;

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
	RiskPlanAssignmentService riskPlanAssignmentService;
	
	@Autowired
	SignalAuditService signalAuditService;
	@Autowired
	TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
	
	@Autowired
    RiskTaskTemplateRepository riskTaskTemplateRepository;
	    
	@Autowired
	RiskTaskTemplateProductsRepository riskTaskTemplateProductsRepository;
	    
	/**
	 * 
	 * @param riskPlan
	 * @param attachments
	 * @param assessmentId
	 * @return
	 * @throws ApplicationException
	 */
	public RiskPlan insert(RiskPlan riskPlan, MultipartFile[] attachments, Long assessmentId)
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
		List<Attachment> attachmentList = attachmentService.addAttachments(riskPlanUpdated.getId(), attachments, AttachmentType.RISK_ASSESSMENT, null,
				riskPlanUpdated.getFileMetadata(), riskPlanUpdated.getCreatedBy());
		updateSignalUrl(riskPlanUpdated);
		AssessmentPlan assessmentPlan;
		if(riskPlan.getAssessmentId() != null){
			try{
				assessmentPlan = assessmentPlanService.findById(riskPlan.getAssessmentId());
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
					riskPlanAssignmentService.saveAssignmentAssignees(assignmentConfiguration, riskPlanUpdated);
				}
			}catch(Exception ex){
				LOG.error(ex);
			}
		}
		
		signalAuditService.saveOrUpdateRiskPlanAudit(riskPlanUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
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
	public List<RiskTask> associateRiskTasks(RiskPlan riskPlan){
		List<RiskTask> tasks = null;
		
		if(!CollectionUtils.isEmpty(riskPlan.getRiskTemplateIds())){
			List<RiskTask> riskTaskList = new ArrayList<>();
			for(Long id:riskPlan.getRiskTemplateIds()){
				List<RiskTask> riskTasks = riskTaskRepository.findAllByTemplateId(id);
				tasks = createRiskTask(riskTaskList, riskTasks, riskPlan);
			}
		}
		checkRiskTaskStatus(riskPlan);
		return tasks;
	}
	
	private void checkRiskTaskStatus(RiskPlan riskPlan){
		boolean allTasksCompletedFlag=true;
		List<RiskTask> riskTaskList=riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(String.valueOf(riskPlan.getId()));
		if(!CollectionUtils.isEmpty(riskTaskList)){
			for(RiskTask riskTask:riskTaskList){
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
	
	List<TopicRiskPlanAssignmentAssignees> associateRiskPlanAssignmentAssignees(List<RiskPlan> riskPlanList){
		List<TopicRiskPlanAssignmentAssignees> topicRiskPlanAssignmentAssigneesList=new ArrayList<>();
		for(RiskPlan riskPlan :riskPlanList){
			topicRiskPlanAssignmentAssigneesList =	topicRiskPlanAssignmentAssigneesRepository.findByRiskId(riskPlan.getId());
			riskPlan.setTopicRiskPlanAssignmentAssignees(topicRiskPlanAssignmentAssigneesList);
		}
		return topicRiskPlanAssignmentAssigneesList;
	}


	private List<RiskTask> createRiskTask(List<RiskTask> riskTaskList, List<RiskTask> templateRiskTasks, RiskPlan riskPlan) {
		if(!CollectionUtils.isEmpty(templateRiskTasks)){
			for(RiskTask templateTask:templateRiskTasks){
				RiskTask riskTask = new RiskTask();
				riskTask.setActionType(templateTask.getActionType());
				riskTask.setAssignTo(templateTask.getAssignTo());
				if (templateTask.getAssignTo() == null) {
					riskTask.setAssignTo(riskPlan.getAssignTo());
				} else {
					riskTask.setAssignTo(templateTask.getAssignTo());
				}
				
				riskTask.setCreatedBy(templateTask.getCreatedBy());
				riskTask.setCreatedDate(new Date());
				riskTask.setDescription(templateTask.getDescription());
				riskTask.setDueDate(templateTask.getDueDate());
				riskTask.setName(templateTask.getName());
				riskTask.setNotes(templateTask.getNotes());
				riskTask.setStatus("New");
				riskTask.setOwner(riskPlan.getAssignTo());
				riskTask.setRecipients(templateTask.getRecipients());
				riskTask.setInDays(templateTask.getInDays());
				if(templateTask.getInDays() != null){
					riskTask.setDueDate(SignalUtil.getDueDate(templateTask.getInDays().intValue(), riskTask.getCreatedDate()));
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
	public void associateTemplateAttachments(RiskTask riskTask, RiskTask templateRiskTask) {
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
	public void associateTemplateURLs(RiskTask riskTask, RiskTask templateRiskTask) {
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
	public void createRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
		    
		Date d = new Date();
		riskTask.setCreatedDate(d);
		riskTask.setLastUpdatedDate(d);
		riskTask.setStatus("New");
		if(riskTask.getRiskId() != null){
			RiskPlan riskPlan = riskPlanRepository.findOne(Long.valueOf(riskTask.getRiskId()));
			riskTask.setAssignTo(riskPlan.getAssignTo());
			riskTask.setOwner(riskPlan.getAssignTo());
		}

		Long riskTaskExists=riskTaskRepository.countByNameIgnoreCaseAndRiskId(riskTask.getName(),riskTask.getRiskId());
		if (riskTaskExists > 0) {
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		
		RiskTask riskTaskUpdated = riskTaskRepository.save(riskTask);
		checkRiskTaskStatus(riskTaskUpdated);
		List<Attachment> attachmentList = attachmentService.addAttachments(riskTaskUpdated.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				null, riskTaskUpdated.getFileMetadata(), riskTaskUpdated.getCreatedBy());
		saveUrls(riskTaskUpdated);
		
		signalAuditService.saveOrUpdateRiskTaskAudit(riskTaskUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
	}
	
	/**
	 * 
	 * @param riskTask
	 * @param attachments
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public void createRiskTemplateTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
		    
		Date d = new Date();
		riskTask.setCreatedDate(d);
		riskTask.setLastUpdatedDate(d);
		riskTask.setStatus("New");
		
		RiskTask riskTaskExists=riskTaskRepository.findByNameIgnoreCaseAndTemplateId(riskTask.getName(),riskTask.getTemplateId());
		if (riskTaskExists != null) {
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		
		RiskTask riskTaskUpdated = riskTaskRepository.save(riskTask);
		List<Attachment> attachmentList = attachmentService.addAttachments(riskTaskUpdated.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				null, riskTaskUpdated.getFileMetadata(), riskTaskUpdated.getCreatedBy());
		saveUrls(riskTaskUpdated);
		
		signalAuditService.saveOrUpdateRiskTaskAudit(riskTaskUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
	}
	
	/**
	 * 
	 * @param riskTask
	 */
	public void checkRiskTaskStatus(RiskTask riskTask) {
		List<RiskTask> risks = findAllByRiskId(riskTask.getRiskId(), null);
		boolean allTasksCompletedFlag = true;
		if (!CollectionUtils.isEmpty(risks)) {
			for (RiskTask risk : risks) {
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
	public RiskTask findById(Long id) {
		RiskTask riskTask = riskTaskRepository.findOne(id);
		if ("New".equalsIgnoreCase(riskTask.getStatus())) {
			riskTask.setStatus("In Progress");
			riskTask = riskTaskRepository.save(riskTask);
		}
		riskTask.setSignalUrls(signalURLRepository.findByTopicId(riskTask.getId()));
		return riskTask;
	}

	public List<RiskTask> findAllByRiskId(String riskId, String status) {
		if (status != null) {
			return riskTaskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(riskId, status);
		}
		return riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(riskId);
	}

	public void delete(Long riskTaskId) throws ApplicationException {
		RiskTask riskTask = riskTaskRepository.findOne(riskTaskId);
		if (riskTask == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		
		riskTaskRepository.delete(riskTask);
		checkRiskTaskStatus(riskTask);
	}

	public void updateRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
		if (riskTask.getId() == null) {
			throw new ApplicationException("Failed to update Action. Invalid Id received");
		}
		RiskTask riskTaskExists;
		if(riskTask.getTemplateId() != null){
			riskTaskExists = riskTaskRepository.findByNameIgnoreCaseAndTemplateId(riskTask.getName(),riskTask.getTemplateId());
		}else{
			riskTaskExists = riskTaskRepository.findByNameIgnoreCaseAndRiskId(riskTask.getName(),riskTask.getRiskId());
		}
		if(riskTaskExists != null && (riskTaskExists.getId().intValue() != riskTask.getId().intValue())){
			throw exceptionBuilder.buildException(ErrorType.RISKTASK_NAME_DUPLICATE, null);
		}
		String riskTaskOriginal = JsonUtil.converToJson(riskTaskRepository.findOne(riskTask.getId()));
		
		riskTask.setLastUpdatedDate(new Date());
		riskTaskRepository.save(riskTask);
		List<Attachment> attachmentList = attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				riskTask.getDeletedAttachmentIds(), riskTask.getFileMetadata(), riskTask.getCreatedBy());
		List<RiskTask> risks = findAllByRiskId(riskTask.getRiskId(), null);
		boolean allTasksCompletedFlag = true;
		if (!CollectionUtils.isEmpty(risks)) {
			for (RiskTask risk : risks) {
				if (!SmtConstant.COMPLETED.getDescription().equals(risk.getStatus())) {
					allTasksCompletedFlag = false;
				}
			}
		}
		saveUrls(riskTask);
		if (allTasksCompletedFlag) {
			String riskPlanOriginal = JsonUtil.converToJson(riskPlanRepository.findOne(Long.parseLong(riskTask.getRiskId())));
			riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(riskTask.getRiskId()));
			signalAuditService.saveOrUpdateRiskPlanAudit(riskPlanRepository.findOne(Long.parseLong(riskTask.getRiskId())), riskPlanOriginal, null, SmtConstant.UPDATE.getDescription());
		}
		
		signalAuditService.saveOrUpdateRiskTaskAudit(riskTask, riskTaskOriginal, attachmentList, SmtConstant.UPDATE.getDescription());
	}
	private void saveUrls(RiskTask riskTask) {
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
		List<TopicRiskPlanAssignmentAssignees> topicRiskPlanAssignmentAssigneesList=topicRiskPlanAssignmentAssigneesRepository.findByRiskId(riskId);
		riskPlan.setTopicRiskPlanAssignmentAssignees(topicRiskPlanAssignmentAssigneesList);
		riskPlan.setComments(commentsRepository.findByRiskPlanId(riskId));
		riskPlan.setSignalUrls(signalURLRepository.findByTopicId(riskId));
		AssessmentPlan assessmentPlan=riskPlan.getAssessmentPlan();
		if(null!=assessmentPlan){
		assessmentPlan.setSignalUrls(signalURLRepository.findByTopicId(assessmentPlan.getId()));
		}
		checkRiskTaskStatus(riskPlan);
		return riskPlan;
	}

	public void riskPlanSummary(RiskPlan riskPlan, MultipartFile[] attachments) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		riskPlan.setLastModifiedDate(new Date());
		riskPlan.setStatus(SmtConstant.COMPLETED.getDescription());
		attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT,
				riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata(), riskPlan.getCreatedBy());
		riskPlanRepository.save(riskPlan);
	}

	public void updateRiskPlan(RiskPlan riskPlan, MultipartFile[] attachments) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		
		RiskPlan riskPlanFromDB = riskPlanRepository.findOne(riskPlan.getId());
		ownerCheck(riskPlan, riskPlanFromDB);
		
		String riskPlanOriginal = JsonUtil.converToJson(riskPlanRepository.findOne(riskPlan.getId()));
		
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
		List<Attachment> attachmentList = attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT,
				riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata(), riskPlan.getCreatedBy());
		setRiskTaskStatus(riskPlan);
		RiskPlan riskPlanUpdated = riskPlanRepository.save(riskPlan);
		List<TopicRiskPlanAssignmentAssignees> assigneeList = riskPlan.getTopicRiskPlanAssignmentAssignees();
		if(!CollectionUtils.isEmpty(assigneeList)){
			topicRiskPlanAssignmentAssigneesRepository.deleteByRiskId(riskPlanUpdated.getId());
			topicRiskPlanAssignmentAssigneesRepository.save(assigneeList);
		}
		
		List<Comments> list = riskPlan.getComments();
		if (!CollectionUtils.isEmpty(list)) {
			for (Comments comment : list) {
				comment.setRiskPlanId(riskPlan.getId());
				comment.setModifiedDate(new Date());
			}
		}
		commentsRepository.save(riskPlan.getComments());
		
		updateSignalUrl(riskPlan);
		signalAuditService.saveOrUpdateRiskPlanAudit(riskPlan, riskPlanOriginal, attachmentList, SmtConstant.UPDATE.getDescription());
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
		List<RiskTask> riskTaskStatues=	riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(String.valueOf(riskPlan.getId()));
		 if(!CollectionUtils.isEmpty(riskTaskStatues)){
			 for(RiskTask riskTask:riskTaskStatues){
				 if(!riskTask.getStatus().equals(SmtConstant.COMPLETED.getDescription())){
					 riskTaskStatus=true;
				 }
			 }
			 if(riskTaskStatus){
				 riskPlan.setRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription());
			 }
		 }
	}
		 
	public List<RiskTask> associateRiskTemplateTasks(RiskPlan riskPlan) {
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
	public List<RiskTaskTemplate> getTaskTamplatesOfRiskProducts(Long riskPlanId) throws ApplicationException {
		List<RiskTaskTemplate> taskTemplates = new ArrayList<>();
		AssessmentPlan assessmentPlan = null;
		RiskPlan riskPlanFromDB = findByRiskId(riskPlanId);
		try{
			assessmentPlan = assessmentPlanService.findById(riskPlanFromDB.getAssessmentPlan().getId());
		}catch(Exception ex){
			LOG.error(ex);
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

	private void getTaskTemplates(List<RiskTaskTemplate> taskTemplates, Topic topicWithConditionsAndProducts) {
		for(TopicProductAssignmentConfiguration recordKey : topicWithConditionsAndProducts.getProducts()){
			RiskTaskTemplateProducts taskTemplateProducts = getTaskTemplateProduct(recordKey);
			if(taskTemplateProducts != null){
				Long taskTemplateId = riskTaskTemplateProductsRepository.findTemplateId(taskTemplateProducts.getId());
				taskTemplates.add(riskTaskTemplateRepository.findOne(taskTemplateId));
			}
		}
	}

	private RiskTaskTemplateProducts getTaskTemplateProduct(TopicProductAssignmentConfiguration recordKey) {
		return riskTaskTemplateProductsRepository.findByRecordKey(recordKey.getRecordKey());
	}
}
