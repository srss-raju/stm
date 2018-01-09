package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SignalConfigurationType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalConfiguration;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalStrength;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.SocAssignmentConfiguration;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignmentCondition;
import com.deloitte.smt.entity.TopicAssignmentProduct;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.NonSignalRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalConfigurationRepository;
import com.deloitte.smt.repository.SignalStrengthRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicAssignmentConditionRepository;
import com.deloitte.smt.repository.TopicAssignmentProductRepository;
import com.deloitte.smt.repository.TopicProductAssignmentConfigurationRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.repository.TopicSocAssignmentConfigurationRepository;
import com.deloitte.smt.util.JsonUtil;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RKB on 04-04-2017.
 */
@Transactional
@Service
public class SignalService {

	private static final Logger LOG = Logger.getLogger(SignalService.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder exceptionBuilder;

	@Autowired
	DashboardCountService dashboardCountService;

	@Autowired
	private SignalMatchService signalMatchService;
	
	@Autowired
	private SignalAssignmentService signalAssignmentService;
	
	@Autowired
	AssignmentService assignmentService;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	SignalURLRepository signalURLRepository;
	
	@Autowired
	SignalAuditService signalAuditService;
	
	@Autowired
	AssessmentPlanRepository assessmentPlanRepository;

	@Autowired
	RiskPlanRepository riskPlanRepository;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	private SocRepository socRepository;

	@Autowired
	private PtRepository ptRepository;

	@Autowired
	private AssessmentActionRepository assessmentActionRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	SignalConfigurationRepository signalConfigurationRepository;

	@Autowired
	NonSignalRepository nonSignalRepository;

	
	@Autowired
	CommentsRepository commentsRepository;
	
	@Autowired
	AssessmentAssignmentService assessmentAssignmentService;
	
	@Autowired
	SignalStrengthRepository signalStrengthRepository;
	@Autowired
	TopicSocAssignmentConfigurationRepository topicSocAssignmentConfigurationRepository;
	@Autowired
	TopicProductAssignmentConfigurationRepository topicProductAssignmentConfigurationRepository;
	
	@Autowired
	TopicAssignmentConditionRepository topicAssignmentConditionRepository;
	
	@Autowired
	TopicAssignmentProductRepository topicAssignmentProductRepository;
	
	public NonSignal createOrupdateNonSignal(NonSignal nonSignal) {
		Calendar c = Calendar.getInstance();

		if (nonSignal.getId() == null) {
			nonSignal.setCreatedDate(c.getTime());
		}

		nonSignal.setLastModifiedDate(c.getTime());
		List<NonSignal> nonSignals = nonSignalRepository.findAll();
		boolean isNonSignalMatched = nonSignals.stream()
				.anyMatch(ns -> ns.getProductKey().equals(nonSignal.getProductKey())
						&& ns.getPtDesc().equals(nonSignal.getPtDesc()) && ns.getName().equals(nonSignal.getName()));

		if (isNonSignalMatched) {
			return null;
		} else {
			return nonSignalRepository.save(nonSignal);
		}

	}

	public Topic findById(Long topicId) throws ApplicationException {
		Topic topic = topicRepository.findOne(topicId);
		if (topic == null) {
			throw new ApplicationException("Signal not found with the given Id :" + topicId);
		}
		
		if (null == topic.getSignalValidation()) {
			topic.setSignalValidation(SmtConstant.IN_PROGRESS.getDescription());
		}
		if ("New".equalsIgnoreCase(topic.getSignalStatus())) {
			topic.setSignalStatus(SmtConstant.IN_PROGRESS.getDescription());
			topic = topicRepository.save(topic);
		}
		
		setTopicConditionsAndProducts(topicId, topic);
		
		signalAssignmentService.findSignalAssignmentAssignees(topic);
		
		topic.setSignalUrls(signalURLRepository.findByTopicId(topicId));
		findSoc(topic);
		if (!SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(topic.getSignalStatus())) {
			LOG.debug("FOUND SIGNAL" + topic.getId());
			topic = signalMatchService.findMatchingSignal(topic);
		}
		return topic;
	}

	private void setTopicConditionsAndProducts(Long topicId, Topic topic) {
		List<TopicSocAssignmentConfiguration> socList = topicSocAssignmentConfigurationRepository.findByTopicId(topicId);
		List<TopicProductAssignmentConfiguration> productList = topicProductAssignmentConfigurationRepository.findByTopicId(topicId);
		
		if(!CollectionUtils.isEmpty(socList)){
			for(TopicSocAssignmentConfiguration socConfig:socList){
				socConfig.setRecordValues(topicAssignmentConditionRepository.findByTopicSocAssignmentConfigurationId(socConfig.getId()));
			}
		}
		
		if(!CollectionUtils.isEmpty(productList)){
			for(TopicProductAssignmentConfiguration productConfig:productList){
				productConfig.setRecordValues(topicAssignmentProductRepository.findByTopicProductAssignmentConfigurationId(productConfig.getId()));
			}
		}
		topic.setConditions(socList);
		topic.setProducts(productList);
	}
	
	public List<Comments> getTopicComments(Long topicId){
		return commentsRepository.findByTopicId(topicId);
	}

	public Topic createTopic(Topic topic, MultipartFile[] attachments) throws ApplicationException {
		if (topic.getId() != null) {
			topic.setId(null);
		}
		if (null == topic.getSignalValidation()) {
			topic.setSignalValidation(SmtConstant.IN_PROGRESS.getDescription());
		}
		Calendar c = Calendar.getInstance();
		topic.setCreatedDate(c.getTime());
		topic.setLastModifiedDate(c.getTime());
		topic.setSignalStatus("New");
		c.add(Calendar.DAY_OF_YEAR, 5);
		topic.setDueDate(c.getTime());
		setTopicIdForSignalStatistics(topic);

		SignalConfiguration signalConfiguration = signalConfigurationRepository
				.findByConfigName(SignalConfigurationType.DEFAULT_CONFIG.name());
		if (null != signalConfiguration) {
			topic.setCohortPercentage(Long.valueOf(signalConfiguration.getCohortPercentage()));
			topic.setConfidenceIndex(Long.valueOf(signalConfiguration.getConfidenceIndex()));
		}

		Long topicExist = topicRepository.countByNameIgnoreCase(topic.getName());
		if (topicExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.SIGNAL_NAME_DUPLICATE, null);
		}

		Topic topicUpdated = topicRepository.save(topic);
		saveProductsAndConditions(topic, topicUpdated);
		AssignmentConfiguration assignmentConfiguration = signalAssignmentService.convertToAssignmentConfiguration(topic);
		
		List<SocAssignmentConfiguration> cList = new ArrayList<>();
		List<ProductAssignmentConfiguration> pList = new ArrayList<>();
		setConditions(assignmentConfiguration, cList);
		
		setProducts(assignmentConfiguration, pList);
		ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
		conditionProductDTO.setConditions(cList);
		conditionProductDTO.setProducts(pList);
		assignmentConfiguration = assignmentService.getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
		if(assignmentConfiguration != null){
			if(assignmentConfiguration.getSignalOwner()!=null){
				topicUpdated.setOwner(assignmentConfiguration.getSignalOwner());
			}
			topicUpdated = topicRepository.save(topicUpdated);
			topicUpdated = signalAssignmentService.saveSignalAssignmentAssignees(assignmentConfiguration, topicUpdated);
		}
		saveSignalUrl(topicUpdated);
		saveSignalStrength(topicUpdated);
		
		List<Attachment> attchmentList = attachmentService.addAttachments(topicUpdated.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, null,
				topicUpdated.getFileMetadata(), topic.getCreatedBy());
		LOG.info("Start Algorithm for matching signal");
		signalAuditService.saveOrUpdateSignalAudit(topicUpdated, null, attchmentList, SmtConstant.CREATE.getDescription());
		return signalMatchService.findMatchingSignal(topicUpdated);
	}

	private void setProducts(AssignmentConfiguration assignmentConfiguration,
			List<ProductAssignmentConfiguration> pList) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getProducts())){
			for(ProductAssignmentConfiguration productConfig : assignmentConfiguration.getProducts()){
				ProductAssignmentConfiguration config = new ProductAssignmentConfiguration();
				config.setAssignmentConfigurationId(productConfig.getAssignmentConfigurationId());
				config.setProductName(productConfig.getProductName());
				config.setCreatedBy(productConfig.getCreatedBy());
				config.setCreatedDate(productConfig.getCreatedDate());
				config.setId(productConfig.getId());
				config.setLastModifiedBy(productConfig.getLastModifiedBy());
				config.setLastModifiedDate(productConfig.getLastModifiedDate());
				config.setRecordKey(productConfig.getRecordKey());
				config.setRecordValues(productConfig.getRecordValues());
				pList.add(config);
			}
		}
	}

	private void setConditions(AssignmentConfiguration assignmentConfiguration,
			List<SocAssignmentConfiguration> cList) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getConditions())){
			for(SocAssignmentConfiguration socConfig : assignmentConfiguration.getConditions()){
				SocAssignmentConfiguration config = new SocAssignmentConfiguration();
				config.setAssignmentConfigurationId(socConfig.getAssignmentConfigurationId());
				config.setConditionName(socConfig.getConditionName());
				config.setCreatedBy(socConfig.getCreatedBy());
				config.setCreatedDate(socConfig.getCreatedDate());
				config.setId(socConfig.getId());
				config.setLastModifiedBy(socConfig.getLastModifiedBy());
				config.setLastModifiedDate(socConfig.getLastModifiedDate());
				config.setRecordKey(socConfig.getRecordKey());
				config.setRecordValues(socConfig.getRecordValues());
				cList.add(config);
			}
		}
	}
	
	

	private void saveProductsAndConditions(Topic topic, Topic topicUpdated) {
		saveConditions(topic, topicUpdated);
		saveProducts(topic, topicUpdated);
	}

	private void saveProducts(Topic topic, Topic topicUpdated) {
		if(!CollectionUtils.isEmpty(topic.getProducts())){
			for(TopicProductAssignmentConfiguration productConfig : topic.getProducts()){
				productConfig.setTopicId(topicUpdated.getId());
			}
			List<TopicProductAssignmentConfiguration> updatedProductList = topicProductAssignmentConfigurationRepository.save(topic.getProducts());
			for(TopicProductAssignmentConfiguration updatedProductConfig : updatedProductList){
				saveAssignmentProduct(updatedProductConfig);
			}
		}
	}

	private void saveAssignmentProduct(TopicProductAssignmentConfiguration updatedProductConfig) {
		if(!CollectionUtils.isEmpty(updatedProductConfig.getRecordValues())){
			for(TopicAssignmentProduct record : updatedProductConfig.getRecordValues()){
				record.setTopicProductAssignmentConfigurationId(updatedProductConfig.getId());
			}
			topicAssignmentProductRepository.save(updatedProductConfig.getRecordValues());
		}
	}

	private void saveConditions(Topic topic, Topic topicUpdated) {
		if(!CollectionUtils.isEmpty(topic.getConditions())){
			for(TopicSocAssignmentConfiguration socConfig : topic.getConditions()){
				socConfig.setTopicId(topicUpdated.getId());
			}
			List<TopicSocAssignmentConfiguration> updatedConditionList = topicSocAssignmentConfigurationRepository.save(topic.getConditions());
			for(TopicSocAssignmentConfiguration updateConditionConfig : updatedConditionList){
				savecAssignmentCondition(updateConditionConfig);
			}
		}
	}

	private void savecAssignmentCondition(TopicSocAssignmentConfiguration updateConditionConfig) {
		if(!CollectionUtils.isEmpty(updateConditionConfig.getRecordValues())){
			for(TopicAssignmentCondition record : updateConditionConfig.getRecordValues()){
				record.setTopicSocAssignmentConfigurationId(updateConditionConfig.getId());
			}
			topicAssignmentConditionRepository.save(updateConditionConfig.getRecordValues());
		}
	}

	public List<Comments> updateComments(Topic topic){
		List<Comments> list = topic.getComments();
		if (!CollectionUtils.isEmpty(list)) {
			for (Comments comment : list) {
				comment.setTopicId(topic.getId());
				comment.setModifiedDate(new Date());
				comment.setCreatedBy(topic.getOwner());
			}
		}
		return commentsRepository.save(topic.getComments());
	}

	/**
	 * @param topicUpdated
	 */
	private void saveSignalUrl(Topic topicUpdated) {
		if (!CollectionUtils.isEmpty(topicUpdated.getSignalUrls())) {
			for (SignalURL url : topicUpdated.getSignalUrls()) {
				url.setTopicId(topicUpdated.getId());
				url.setCreatedDate(topicUpdated.getCreatedDate());
				url.setCreatedBy(topicUpdated.getCreatedBy());
				url.setModifiedBy(topicUpdated.getModifiedBy());
				url.setModifiedDate(topicUpdated.getLastModifiedDate());
			}
			signalURLRepository.save(topicUpdated.getSignalUrls());
		}
	}
	
	private void saveSignalStrength(Topic topicUpdated){
		if (!CollectionUtils.isEmpty(topicUpdated.getSignalStrengthAtrributes())) {
			for(SignalStrength signalStrength : topicUpdated.getSignalStrengthAtrributes()){
				signalStrength.setTopicId(topicUpdated.getId());
				signalStrength.setCreatedDate(topicUpdated.getCreatedDate());
				signalStrength.setCreatedBy(topicUpdated.getCreatedBy());
				signalStrength.setModifiedBy(topicUpdated.getModifiedBy());
				signalStrength.setModifiedDate(topicUpdated.getLastModifiedDate());
			}
			signalStrengthRepository.save(topicUpdated.getSignalStrengthAtrributes());
		}
	}

	/**
	 * @param topic
	 */
	private void setTopicIdForSignalStatistics(Topic topic) {
		if (!CollectionUtils.isEmpty(topic.getSignalStatistics())) {
			for (SignalStatistics signalStatistic : topic.getSignalStatistics()) {
				signalStatistic.setTopic(topic);
			}
		}
	}

	public String updateTopic(Topic topic, MultipartFile[] attachments) throws ApplicationException {
		if (topic.getId() == null) {
			throw new ApplicationException("Update failed for Topic, since it does not have any valid Id field.");
		}
		ownerCheck(topic);
		topic.setLastModifiedDate(new Date());
		setTopicIdForSignalStatistics(topic);
		List<Attachment> attchmentList = attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT,
				topic.getDeletedAttachmentIds(), topic.getFileMetadata(), topic.getCreatedBy());
		String topicOriginal = JsonUtil.converToJson(findById(topic.getId()));
		
		Topic topicUpdated = topicRepository.save(topic);
		saveSignalUrl(topic);
		signalAuditService.saveOrUpdateSignalAudit(topicUpdated, topicOriginal, attchmentList, SmtConstant.UPDATE.getDescription());
		return "Update Success";
	}

	private void ownerCheck(Topic topic) throws ApplicationException {
		Topic topicFromDB = topicRepository.findOne(topic.getId());
		if(topicFromDB != null && topicFromDB.getOwner() != null && (!topicFromDB.getOwner().equalsIgnoreCase(topic.getOwner()))){
				throw new ApplicationException("Unable to make you as a Owner");
		}
	}

	public AssessmentPlan validateAndPrioritize(Long topicId, AssessmentPlan assessmentPlan)
			throws ApplicationException {
		AssignmentConfiguration assignmentConfiguration = null;
		Topic topic = topicRepository.findOne(topicId);
		if (topic == null) {
			throw new ApplicationException("Topic not found with the given Id [" + topicId + "]");
		}
		String topicOriginal = JsonUtil.converToJson(topic);
		if (assessmentPlan.getId() == null) {
		
			Date d = new Date();
			assessmentPlan.setCreatedDate(d);
			assessmentPlan.setLastModifiedDate(d);
			assessmentPlan.setAssessmentPlanStatus("New");
			assessmentPlan.setFinalAssessmentSummary(SmtConstant.SUMMARY.getDescription());
			
			Topic topicWithConditionsAndProducts = findById(topicId);
			assignmentConfiguration = signalAssignmentService.convertToAssignmentConfiguration(topicWithConditionsAndProducts);
			ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
			conditionProductDTO.setConditions(assignmentConfiguration.getConditions());
			conditionProductDTO.setProducts(assignmentConfiguration.getProducts());
			assignmentConfiguration = signalAssignmentService.getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO);
			
			/**when no tasks and for newly created assessment assessmenttaskstatus is 'Completed' **/
			assessmentPlan.setAssessmentTaskStatus("Completed");
			Long assessmentPlanExist = assessmentPlanRepository.countByAssessmentNameIgnoreCase(assessmentPlan.getAssessmentName());
			if (assessmentPlanExist > 0) {
				throw exceptionBuilder.buildException(ErrorType.ASSESSMENTPLAN_NAME_DUPLICATE, null);
			}
			topic.setAssessmentPlan(assessmentPlanRepository.save(assessmentPlan));
		}else{
			topic.setAssessmentPlan(assessmentPlan);
		}
		topic.setSignalStatus(SmtConstant.COMPLETED.getDescription());
		topic.setSignalValidation(SmtConstant.COMPLETED.getDescription());
		topic.setLastModifiedDate(new Date());
		Topic topicUpdated = topicRepository.save(topic);
		signalAuditService.saveOrUpdateSignalAudit(topicUpdated, topicOriginal, null, SmtConstant.UPDATE.getDescription());
		if (assignmentConfiguration != null) {
			if(assignmentConfiguration.getAssessmentOwner()!=null){
				assessmentPlan.setOwner(assignmentConfiguration.getAssessmentOwner());
			}
			assessmentAssignmentService.saveAssignmentAssignees(assignmentConfiguration, assessmentPlan);
		}
		signalAuditService.saveOrUpdateAssessmentPlanAudit(assessmentPlan, null, null, SmtConstant.CREATE.getDescription());
		return assessmentPlan;
	}

	public String getCountsByFilter(String assignTo) {
		List<Long> topicIds = new ArrayList<>();
		List<Topic> signals = topicRepository.findAllByIdInAndAssignToAndSignalStatusNotLikeOrderByCreatedDateDesc(
				topicIds, assignTo, SmtConstant.COMPLETED.getDescription());
		List<AssessmentPlan> assessmentPlanList = signals.stream().map(signal -> signal.getAssessmentPlan()).filter(
				e -> e != null && !SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(e.getAssessmentPlanStatus()))
				.collect(Collectors.toList());
		List<RiskPlan> riskPlanList = assessmentPlanList.stream().map(assessmentPlan -> assessmentPlan.getRiskPlan())
				.filter(e -> e != null && !SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(e.getStatus()))
				.collect(Collectors.toList());
		return SignalUtil.getCounts(Long.valueOf(signals.size()), Long.valueOf(assessmentPlanList.size()),
				Long.valueOf(riskPlanList.size()));
	}

	/**
	 * 
	 * @param assessmentPlan
	 * @return
	 */
	public List<SignalAction> associateTemplateTasks(AssessmentPlan assessmentPlan) {
		Sort sort = new Sort(Sort.Direction.DESC, SmtConstant.CREATED_DATE.getDescription());
		 
		List<SignalAction> signalActionList = null;
		if (!CollectionUtils.isEmpty(assessmentPlan.getTemplateIds())) {
			for (Long id : assessmentPlan.getTemplateIds()) {
				List<SignalAction> actions = assessmentActionRepository.findAllByTemplateId(id);
				signalActionList = new ArrayList<>();
				createAssessmentActions(assessmentPlan, sort, signalActionList, actions);
			}
		}
		checkAssessmentTaskStatus(assessmentPlan);
		return signalActionList;
	}
	
	private void checkAssessmentTaskStatus(AssessmentPlan assessmentPlan){
		boolean allTasksCompletedFlag = true;
		
		List<SignalAction> actions=	assessmentActionRepository.findAllByAssessmentId(String.valueOf(assessmentPlan.getId()));
		if(!CollectionUtils.isEmpty(actions)){
			for(SignalAction signalAction:actions){
        		if(!"Completed".equals(signalAction.getActionStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
			if(allTasksCompletedFlag){
	        	assessmentPlanRepository.updateAssessmentTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(assessmentPlan.getId()));
	        }else{
	       	 assessmentPlanRepository.updateAssessmentTaskStatus(SmtConstant.NOTCOMPLETED.getDescription(), Long.valueOf(assessmentPlan.getId()));
	        }
			
		}
	}
	/**
	 * @param assessmentPlan
	 * @param sort
	 * @param signalActionList
	 * @param actions
	 */
	private void createAssessmentActions(AssessmentPlan assessmentPlan, Sort sort, List<SignalAction> signalActionList,
			List<SignalAction> actions) {
		if (!CollectionUtils.isEmpty(actions)) {
			for (SignalAction action : actions) {
				createAssessmentAction(assessmentPlan, sort, signalActionList, action);
			}
		}
	}

	/**
	 * @param assessmentPlan
	 * @param sort
	 * @param signalActionList
	 * @param action
	 */
	private void createAssessmentAction(AssessmentPlan assessmentPlan, Sort sort, List<SignalAction> signalActionList,
			SignalAction action) {
		SignalAction signalAction = new SignalAction();
		signalAction.setCaseInstanceId(assessmentPlan.getCaseInstanceId());
		signalAction.setActionName(action.getActionName());
		signalAction.setCreatedDate(new Date());
		signalAction.setLastModifiedDate(action.getLastModifiedDate());
		signalAction.setActionStatus(action.getActionStatus());
		signalAction.setDueDate(action.getDueDate());
		signalAction.setAssessmentId(String.valueOf(assessmentPlan.getId()));
		signalAction.setActionType(action.getActionType());
		signalAction.setDueDate(SignalUtil.getDueDate(action.getInDays(), signalAction.getCreatedDate()));
		signalAction.setInDays(action.getInDays());
		signalAction.setActionDescription(action.getActionDescription());
		signalAction.setActionNotes(action.getActionNotes());
		signalAction.setRecipients(action.getRecipients());
		if (action.getAssignTo() == null) {
			signalAction.setAssignTo(assessmentPlan.getAssignTo());
		} else {
			signalAction.setAssignTo(action.getAssignTo());
		}
		signalAction.setOwner(assessmentPlan.getAssignTo());
		signalAction = assessmentActionRepository.save(signalAction);
		signalActionList.add(signalAction);
		List<Attachment> signalActionAttachments = associateTemplateAttachments(sort, action, signalAction);
		associateTemplateURLs(action, signalAction);
		signalAuditService.saveOrUpdateSignalActionAudit(signalAction, null, signalActionAttachments, SmtConstant.CREATE.getDescription());
	}

	/**
	 * @param action
	 * @param signalAction
	 */
	public List<SignalURL> associateTemplateURLs(SignalAction action, SignalAction signalAction) {
		List<SignalURL> templateTaskUrls = signalURLRepository.findByTopicId(action.getId());
		List<SignalURL> signalActionTaskUrls = null;
		if (!CollectionUtils.isEmpty(templateTaskUrls)) {
			List<SignalURL> assessmentActionSignalURLs = new ArrayList<>();
			for (SignalURL url : templateTaskUrls) {
				SignalURL assessmentActionSignalURL = new SignalURL();
				assessmentActionSignalURL.setDescription(url.getDescription());
				assessmentActionSignalURL.setTopicId(signalAction.getId());
				assessmentActionSignalURL.setUrl(url.getUrl());
				assessmentActionSignalURL.setCreatedDate(new Date());
				assessmentActionSignalURL.setModifiedDate(new Date());
				assessmentActionSignalURL.setCreatedBy(signalAction.getCreatedBy());
				assessmentActionSignalURL.setModifiedBy(signalAction.getModifiedBy());
				assessmentActionSignalURLs.add(assessmentActionSignalURL);
			}
			signalActionTaskUrls = signalURLRepository.save(assessmentActionSignalURLs);
		}
		return signalActionTaskUrls;
	}

	/**
	 * @param sort
	 * @param action
	 * @param signalAction
	 */
	public List<Attachment> associateTemplateAttachments(Sort sort, SignalAction action, SignalAction signalAction) {
		List<Attachment> attachments = attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(
				action.getId(), AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, sort);
		List<Attachment> signalActionAttachments = null;
		if (!CollectionUtils.isEmpty(attachments)) {
			List<Attachment> assessmentActionAttachments = new ArrayList<>();
			for (Attachment attachment : attachments) {
				Attachment assessmentActionAttachment = new Attachment();

				assessmentActionAttachment.setDescription(attachment.getDescription());
				assessmentActionAttachment.setAttachmentsURL(attachment.getAttachmentsURL());
				assessmentActionAttachment.setAttachmentResourceId(signalAction.getId());
				assessmentActionAttachment.setContentType(attachment.getContentType());
				assessmentActionAttachment.setContent(attachment.getContent());
				assessmentActionAttachment.setFileName(attachment.getFileName());
				assessmentActionAttachment.setCreatedDate(new Date());
				assessmentActionAttachment.setAttachmentType(attachment.getAttachmentType());

				assessmentActionAttachments.add(assessmentActionAttachment);
			}
			signalActionAttachments = attachmentRepository.save(assessmentActionAttachments);
		}
		return signalActionAttachments;
	}

	public List<Topic> findTopicsByRunInstanceId(Long runInstanceId) {
		List<Topic> topics = topicRepository.findTopicByRunInstanceIdOrderByCreatedDateAsc(runInstanceId);
		if (!CollectionUtils.isEmpty(topics)) {
			for (Topic topic : topics) {
				topic.setSocs(findSoc(topic));
			}
		}
		return topics;
	}

	/**
	 * @param topic
	 * @return
	 */
	private List<Soc> findSoc(Topic topic) {
		List<Soc> socs = socRepository.findByTopicId(topic.getId());
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setPts(ptRepository.findBySocId(soc.getId()));
			}
		}
		return socs;
	}

	public void deleteSignalURL(Long signalUrlId) throws ApplicationException {
		SignalURL signalURL = signalURLRepository.findOne(signalUrlId);
		if (signalURL == null) {
			throw new ApplicationException("Risk Plan Action Type not found with the given Id : " + signalUrlId);
		}
		signalURLRepository.delete(signalURL);
	}

	public List<NonSignal> findNonSignalsByRunInstanceId(Long runInstanceId) {
		return nonSignalRepository.findNonSignalByRunInstanceIdOrderByCreatedDateAsc(runInstanceId);
	}

	public void deleteTopicComments(Long commentsId) throws ApplicationException {
		Comments comments = commentsRepository.findOne(commentsId);
		if (comments == null) {
			throw new ApplicationException("Risk Plan Action Type not found with the given Id : " + commentsId);
		}
		commentsRepository.delete(comments);
	}
	
	public Long getValidateAndPrioritizeCount(String owner, List<String> userKeys, List<String> userGroupKeys) {
		return dashboardCountService.getValidateAndPrioritizeCount(owner, userKeys, userGroupKeys);
	}
	
	public Long getAssessmentCount(String owner, List<String> userKeys, List<String> userGroupKeys) {
		return dashboardCountService.getAssessmentCount(owner, userKeys, userGroupKeys);
	}
	
	public Long getRiskCount(String owner, List<String> userKeys, List<String> userGroupKeys) {
		return dashboardCountService.getRiskCount(owner, userKeys, userGroupKeys);
	}

}
