package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.exception.TopicNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RKB on 04-04-2017.
 */
@Service
public class SignalService {

    private static final Logger LOG = Logger.getLogger(SignalService.class);

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private SignalMatchService signalMatchService;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    TaskInstRepository taskInstRepository;
    @Autowired
    SignalURLRepository signalURLRepository;
    
    @Autowired
    CaseService caseService;
    
    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;
    
    @Autowired
    RiskPlanRepository riskPlanRepository;

    @Autowired
    AttachmentService attachmentService;

    @PersistenceContext
    private EntityManager entityManager;
    
	@Autowired
	private SocRepository socRepository;

	@Autowired
	private HlgtRepository hlgtRepository;

	@Autowired
	private HltRepository hltRepository;

	@Autowired
	private PtRepository ptRepository;
	
	@Autowired
	private AssessmentActionRepository assessmentActionRepository;
	
	@Autowired
	private TaskTemplateRepository taskTemplateRepository;
	
	@Autowired
	private TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;

    @Autowired
    SearchService searchService;
    
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    private AssignmentConfigurationRepository assignmentConfigurationRepository;

    public Topic findById(Long topicId) throws EntityNotFoundException {
        Topic topic = topicRepository.findOne(topicId);
        if(topic == null) {
            throw new EntityNotFoundException("Signal not found with the given Id :"+topicId);
        }
        if(null == topic.getSignalValidation()) {
            topic.setSignalValidation("In Progress");
        }
        if("New".equalsIgnoreCase(topic.getSignalStatus())) {
            topic.setSignalStatus("In Progress");
            topic = topicRepository.save(topic);
        }
        Ingredient ingredient = ingredientRepository.findByTopicId(topic.getId());
        List<Product> products = productRepository.findByTopicId(topic.getId());
        List<License> licenses = licenseRepository.findByTopicId(topic.getId());
        if(ingredient != null) {
            ingredient.setProducts(products);
            ingredient.setLicenses(licenses);
            topic.setIngredient(ingredient);
        }
        List<Soc> socs  = socRepository.findByTopicId(topic.getId());
        if(!CollectionUtils.isEmpty(socs)) {
        	for(Soc soc:socs){
        		soc.setHlgts(hlgtRepository.findBySocId(soc.getId()));
        		soc.setHlts(hltRepository.findBySocId(soc.getId()));
        		soc.setPts(ptRepository.findBySocId(soc.getId()));
        	}
        }
        topic.setSignalUrls(signalURLRepository.findByTopicId(topicId));
        
        topic.setSocs(socs);
        if(!"Completed".equalsIgnoreCase(topic.getSignalStatus())){
        	topic = signalMatchService.findMatchingSignal(topic);
        }
        return topic;
    }

    public Topic createTopic(Topic topic, MultipartFile[] attachments) throws IOException {
    	String processInstanceId = runtimeService.startProcessInstanceByKey("topicProcess").getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        taskService.delegateTask(task.getId(), "Demo Demo");
        if(topic.getId() != null) {
            topic.setId(null);
        }
        if(null == topic.getSignalValidation()) {
            topic.setSignalValidation("In Progress");
        }
        Calendar c = Calendar.getInstance();
        topic.setCreatedDate(c.getTime());
        topic.setLastModifiedDate(c.getTime());
        topic.setSignalStatus("New");
        topic.setProcessId(processInstanceId);
        c.add(Calendar.DAY_OF_YEAR, 5);
        topic.setDueDate(c.getTime());
        if(!CollectionUtils.isEmpty(topic.getSignalStatistics())) {
            for (SignalStatistics signalStatistic : topic.getSignalStatistics()) {
                signalStatistic.setTopic(topic);
            }
        }
        topic.setConfidenceIndex(60l);
        topic = topicRepository.save(topic);

        Ingredient ingredient = topic.getIngredient();
        if(ingredient != null) {
            AssignmentConfiguration assignmentConfiguration = null;
            if(!StringUtils.isEmpty(topic.getSourceName())) {
                assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSource(ingredient.getIngredientName(), topic.getSourceName());
            } 
            // If Source is  null and combination not available we have to fetch with Ingredient
            if(assignmentConfiguration == null){
            	assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSourceIsNull(ingredient.getIngredientName());
            }
            
            
            if(assignmentConfiguration != null) {
                topic.setAssignTo(assignmentConfiguration.getSignalValidationAssignmentUser());
                topic = topicRepository.save(topic);
            }
            List<Product> products = ingredient.getProducts();
            List<License> licenses = ingredient.getLicenses();
            ingredient.setTopicId(topic.getId());
            ingredient = ingredientRepository.save(ingredient);

            if(!CollectionUtils.isEmpty(products)){
                for (Product singleProduct : products) {
                    singleProduct.setIngredientId(ingredient.getId());
                    singleProduct.setTopicId(topic.getId());
                }
                productRepository.save(products);
            }
            if(!CollectionUtils.isEmpty(licenses)) {
                for (License singleLicense : licenses) {
                    singleLicense.setIngredientId(ingredient.getId());
                    singleLicense.setTopicId(topic.getId());
                }
                licenseRepository.save(licenses);
            }
        }
        
        List<Soc> socs  = topic.getSocs();
        if(!CollectionUtils.isEmpty(socs)) {
            for (Soc soc : socs) {
                soc.setTopicId(topic.getId());
            }
            socs = socRepository.save(socs);
            List<Hlgt> hlgts;
            List<Hlt> hlts;
            List<Pt> pts;

            for (Soc soc : socs) {
                hlgts = soc.getHlgts();
                hlts = soc.getHlts();
                pts = soc.getPts();
                if (!CollectionUtils.isEmpty(hlgts)) {
                    for (Hlgt hlgt : hlgts) {
                        hlgt.setSocId(soc.getId());
                        hlgt.setTopicId(topic.getId());
                    }
                    hlgtRepository.save(hlgts);
                }
                if (!CollectionUtils.isEmpty(hlts)) {
                    for (Hlt hlt : hlts) {
                        hlt.setSocId(soc.getId());
                        hlt.setTopicId(topic.getId());
                    }
                    hltRepository.save(hlts);
                }
                if (!CollectionUtils.isEmpty(pts)) {
                    for (Pt pt : pts) {
                        pt.setSocId(soc.getId());
                        pt.setTopicId(topic.getId());
                    }
                    ptRepository.save(pts);
                }
            }
        }
        if(!CollectionUtils.isEmpty(topic.getSignalUrls())){
        	for(SignalURL url:topic.getSignalUrls()){
        		url.setTopicId(topic.getId());
        	}
        	signalURLRepository.save(topic.getSignalUrls());
        }
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, null, topic.getFileMetadata());
        LOG.info("Start Algorithm for matching signal");
        topic = signalMatchService.findMatchingSignal(topic);
        return topic;
    }

	

    public String updateTopic(Topic topic, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(topic.getId() == null) {
            throw new UpdateFailedException("Update failed for Topic, since it does not have any valid Id field.");
        }
        topic.setLastModifiedDate(new Date());
        if(!CollectionUtils.isEmpty(topic.getSignalStatistics())) {
            for (SignalStatistics signalStatistic : topic.getSignalStatistics()) {
                signalStatistic.setTopic(topic);
            }
        }
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, topic.getDeletedAttachmentIds(), topic.getFileMetadata());
        topicRepository.save(topic);
        if(!CollectionUtils.isEmpty(topic.getSignalUrls())){
        	for(SignalURL url:topic.getSignalUrls()){
        		url.setTopicId(topic.getId());
        	}
        	signalURLRepository.save(topic.getSignalUrls());
        }
        return "Update Success";
    }

    public AssessmentPlan validateAndPrioritize(Long topicId, AssessmentPlan assessmentPlan) throws TaskNotFoundException, TopicNotFoundException {
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
        Date d = new Date();
        assessmentPlan.setCreatedDate(d);
        assessmentPlan.setLastModifiedDate(d);
        if(assessmentPlan.getId() == null) {
            assessmentPlan.setAssessmentPlanStatus("New");
        }
        AssignmentConfiguration assignmentConfiguration = null;
        
        if(!StringUtils.isEmpty(topic.getSourceName())) {
            assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSource(assessmentPlan.getIngrediantName(), assessmentPlan.getSource());
        } 
        // If Source is not null and combination not available we have to fetch with Ingredient
        if(assignmentConfiguration == null){
        	assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSourceIsNull(assessmentPlan.getIngrediantName());
        }
        
        if(assignmentConfiguration != null){
            assessmentPlan.setAssignTo(assignmentConfiguration.getAssessmentAssignmentUser());
        }
        assessmentPlan.setCaseInstanceId(instance.getCaseInstanceId());
        assessmentPlan.setAssessmentTaskStatus("Not Completed");
        topic.setAssessmentPlan(assessmentPlanRepository.save(assessmentPlan));
        topic.setSignalStatus("Completed");
        topic.setSignalValidation("Completed");
        topic.setLastModifiedDate(new Date());
        topicRepository.save(topic);

        return assessmentPlan;
    }

	public List<Topic> findTopics(SearchDto searchDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Topic> query = criteriaBuilder.createQuery(Topic.class);
		Root<Topic> rootTopic = query.from(Topic.class);

		if (null != searchDto) {
			List<Predicate> predicates = new ArrayList<Predicate>(10);
			
			if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
				Root<Product> rootProduct = query.from(Product.class);
				Predicate productEquals = criteriaBuilder.equal(rootTopic.get("id"), rootProduct.get("topicId"));
				Predicate producNameEquals = criteriaBuilder.isTrue(rootProduct.get("productName").in(searchDto.getProducts()));
				predicates.add(productEquals);
				predicates.add(producNameEquals);
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
				Root<License> rootLicense = query.from(License.class);
				Predicate licenseEquals = criteriaBuilder.equal(rootTopic.get("id"), rootLicense.get("topicId"));
				Predicate licenseNameEquals = criteriaBuilder.isTrue(rootLicense.get("licenseName").in(searchDto.getLicenses()));
				predicates.add(licenseEquals);
				predicates.add(licenseNameEquals);
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getIngredients())) {
				Root<Ingredient> rootIngredient = query.from(Ingredient.class);
				Predicate ingredientEquals = criteriaBuilder.equal(rootTopic.get("id"), rootIngredient.get("topicId"));
				Predicate ingredientNameEquals = criteriaBuilder.isTrue(rootIngredient.get("ingredientName").in(searchDto.getIngredients()));
				predicates.add(ingredientEquals);
				predicates.add(ingredientNameEquals);
			}
			
			
			if (!CollectionUtils.isEmpty(searchDto.getHlts())) {
				Root<Hlt> rootHlt = query.from(Hlt.class);
				Predicate hltTopicEquals = criteriaBuilder.equal(rootTopic.get("id"), rootHlt.get("topicId"));
				Predicate hltNameEquals = criteriaBuilder.isTrue(rootHlt.get("hltName").in(searchDto.getHlts()));
				predicates.add(hltTopicEquals);
				predicates.add(hltNameEquals);
			}

			if (!CollectionUtils.isEmpty(searchDto.getHlgts())) {
				Root<Hlgt> rootHlgt = query.from(Hlgt.class);
				Predicate hlgtTopicEquals = criteriaBuilder.equal(rootTopic.get("id"), rootHlgt.get("topicId"));
				Predicate hlgtNameEquals = criteriaBuilder.isTrue(rootHlgt.get("hlgtName").in(searchDto.getHlgts()));
				predicates.add(hlgtTopicEquals);
				predicates.add(hlgtNameEquals);
			}

			if (!CollectionUtils.isEmpty(searchDto.getPts())) {
				Root<Pt> rootPt = query.from(Pt.class);
				Predicate ptTopicEquals = criteriaBuilder.equal(rootTopic.get("id"), rootPt.get("topicId"));
				Predicate ptNameEquals = criteriaBuilder.isTrue(rootPt.get("ptName").in(searchDto.getPts()));
				predicates.add(ptTopicEquals);
				predicates.add(ptNameEquals);
			}

			if (null != searchDto.getCreatedDate()) {
				predicates.add(criteriaBuilder.equal(rootTopic.get("createdDate"), searchDto.getCreatedDate()));
			}

			if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
				predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalStatus").in(searchDto.getStatuses())));
			}

			if (!CollectionUtils.isEmpty(searchDto.getSignalNames())) {
				predicates.add(criteriaBuilder.isTrue(rootTopic.get("name").in(searchDto.getSignalNames())));
			}

			if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
				predicates.add(criteriaBuilder.isTrue(rootTopic.get("assignTo").in(searchDto.getAssignees())));
			}

			if (null != searchDto.getStartDate() && null != searchDto.getEndDate()) {
				if (searchDto.isDueDate()) {
					predicates.add(
							criteriaBuilder.greaterThanOrEqualTo(rootTopic.get("dueDate"), searchDto.getStartDate()));
					predicates.add(criteriaBuilder.lessThanOrEqualTo(rootTopic.get("dueDate"), searchDto.getEndDate()));
				} else {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(rootTopic.get("createdDate"),
							searchDto.getStartDate()));
					predicates.add(
							criteriaBuilder.lessThanOrEqualTo(rootTopic.get("createdDate"), searchDto.getEndDate()));
				}

			}

			if (!CollectionUtils.isEmpty(searchDto.getSignalConfirmations())) {
				predicates.add(criteriaBuilder
						.isTrue(rootTopic.get("signalConfirmation").in(searchDto.getSignalConfirmations())));
			}

			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			query.select(rootTopic).where(andPredicate).orderBy(criteriaBuilder.desc(rootTopic.get("createdDate")))
					.distinct(true);
		} else {
			query.select(rootTopic).orderBy(criteriaBuilder.desc(rootTopic.get("createdDate")));
		}
		
		TypedQuery<Topic> q = entityManager.createQuery(query);
		return q.getResultList();
	}

    public Long getValidateAndPrioritizeCount(String assignTo){
        return topicRepository.countByAssignToAndSignalStatusNotLikeIgnoreCase(assignTo, "Completed");
    	//return taskInstRepository.countByTaskDefKeyInAndDeleteReasonNot(Arrays.asList("validateTopic"), "completed");
    }
    
    public Long getAssessmentCount(String assignTo){
    	///return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("assessment"));
        return assessmentPlanRepository.countByAssignToAndAssessmentPlanStatusNotLikeIgnoreCase(assignTo, "Completed");
    }
    
    public Long getRiskCount(String assignTo){
    	return riskPlanRepository.countByAssignToAndStatusNotLikeIgnoreCase(assignTo, "Completed");
    	//return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("risk"));
    }

    public String getCountsByFilter(String ingredientName, String assignTo) {
        List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameIn(Arrays.asList(ingredientName));
        List<Long> topicIds = new ArrayList<>();
        ingredients.parallelStream().forEach(ingredient -> topicIds.add(ingredient.getTopicId()));
        List<Topic> signals = topicRepository.findAllByIdInAndAssignToAndSignalStatusNotLikeOrderByCreatedDateDesc(topicIds, assignTo, "Completed");
        List<AssessmentPlan> assessmentPlanList = signals.stream().map(signal -> signal.getAssessmentPlan()).filter(e -> e != null && !"Completed".equalsIgnoreCase(e.getAssessmentPlanStatus())).collect(Collectors.toList());
        List<RiskPlan> riskPlanList = assessmentPlanList.stream().map(assessmentPlan -> assessmentPlan.getRiskPlan()).filter(e -> e != null && !"Completed".equalsIgnoreCase(e.getStatus())).collect(Collectors.toList());
        return SignalUtil.getCounts(Long.valueOf(signals.size()), Long.valueOf(assessmentPlanList.size()), Long.valueOf(riskPlanList.size()));
    }

    /*public List<SignalAction> attachTasksToAssessment(AssessmentPlan assessmentPlan) throws TaskNotFoundException{
    	List<SignalAction> signalActionList = new ArrayList<>();
    	TaskTemplateIngrediant taskTemplateIngrediant = taskTemplateIngrediantRepository.findByName(assessmentPlan.getIngrediantName());
		if (taskTemplateIngrediant != null) {
			List<SignalAction> actions = assessmentActionRepository.findAllByTemplateId(taskTemplateIngrediant.getId());
			if (!CollectionUtils.isEmpty(actions)) {
				for (SignalAction action : actions) {
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

					Task task = taskService.newTask();
					task.setCaseInstanceId(signalAction.getCaseInstanceId());
					task.setName(signalAction.getActionName());
					taskService.saveTask(task);
					List<Task> list = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).list();
					TaskInst taskInstance = new TaskInst();
					taskInstance.setId(list.get(list.size() - 1).getId());
					taskInstance.setCaseDefKey("assessment");
					taskInstance.setTaskDefKey("assessment");
					taskInstance.setCaseInstId(assessmentPlan.getCaseInstanceId());
					taskInstance.setStartTime(new Date());
					signalAction.setTaskId(taskInstance.getId());
					signalActionList.add(signalAction);
				}
			}
		}
    	return signalActionList;
    }*/
    
    public List<TaskTemplate> getTaskTamplatesOfIngrediant(String ingrediantName){
    	List<Long> templateIds = taskTemplateIngrediantRepository.findByIngrediantName(ingrediantName);
    	return taskTemplateRepository.findByIdIn(templateIds);
    }

	public List<SignalAction> associateTemplateTasks(AssessmentPlan assessmentPlan) {
		Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
		List<SignalAction> signalActionList = null;
		if (!CollectionUtils.isEmpty(assessmentPlan.getTemplateIds())) {
			for (Long id : assessmentPlan.getTemplateIds()) {
				List<SignalAction> actions = assessmentActionRepository.findAllByTemplateId(id);
				signalActionList = new ArrayList<>();
				if (!CollectionUtils.isEmpty(actions)) {
					for (SignalAction action : actions) {
						SignalAction signalAction = new SignalAction();
						signalAction.setCaseInstanceId(assessmentPlan.getCaseInstanceId());
						signalAction.setActionName(action.getActionName());
						signalAction.setCreatedDate(new Date());
						signalAction.setLastModifiedDate(action.getLastModifiedDate());
						signalAction.setActionStatus(action.getActionStatus());
						signalAction.setDueDate(action.getDueDate());
						signalAction.setAssessmentId(String.valueOf(assessmentPlan.getId()));
						signalAction.setActionType(action.getActionType());
						signalAction.setDueDate(SignalUtil.getDueDate(action.getInDays(),signalAction.getCreatedDate()));
						signalAction.setInDays(action.getInDays());
						signalAction.setActionDescription(action.getActionDescription());
						signalAction.setActionNotes(action.getActionNotes());
                        if(action.getAssignTo() == null) {
                            signalAction.setAssignTo(assessmentPlan.getAssignTo());
                        }else{
                        	signalAction.setAssignTo(action.getAssignTo());
                        }
                        signalAction.setOwner(assessmentPlan.getAssignTo());
						Task task = taskService.newTask();
						task.setCaseInstanceId(signalAction.getCaseInstanceId());
						task.setName(signalAction.getActionName());
						taskService.saveTask(task);
						List<Task> list = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).list();
						TaskInst taskInstance = new TaskInst();
						taskInstance.setId(list.get(list.size() - 1).getId());
						taskInstance.setCaseDefKey("assessment");
						taskInstance.setTaskDefKey("assessment");
						taskInstance.setCaseInstId(assessmentPlan.getCaseInstanceId());
						taskInstance.setStartTime(new Date());
						signalAction.setTaskId(taskInstance.getId());
						signalAction = assessmentActionRepository.save(signalAction);
						signalActionList.add(signalAction);
						associateTemplateAttachments(sort, action, signalAction);
						associateTemplateURLs(action, signalAction);
					}
				}
			}
		}
		return signalActionList;
	}

	/**
	 * @param action
	 * @param signalAction
	 */
	private void associateTemplateURLs(SignalAction action, SignalAction signalAction) {
		List<SignalURL> templateTaskUrls = signalURLRepository.findByTopicId(action.getId());
		if(!CollectionUtils.isEmpty(templateTaskUrls)){
			List<SignalURL> assessmentActionSignalURLs = new ArrayList<>();
			for(SignalURL url:templateTaskUrls){
				SignalURL assessmentActionSignalURL = new SignalURL();
				assessmentActionSignalURL.setDescription(url.getDescription());
				assessmentActionSignalURL.setTopicId(signalAction.getId());
				assessmentActionSignalURL.setUrl(url.getUrl());
				assessmentActionSignalURL.setModifiedDate(new Date());
				assessmentActionSignalURLs.add(assessmentActionSignalURL);
			}
			signalURLRepository.save(assessmentActionSignalURLs);
		}
	}

	/**
	 * @param sort
	 * @param action
	 * @param signalAction
	 */
	private void associateTemplateAttachments(Sort sort, SignalAction action, SignalAction signalAction) {
		List<Attachment> attachments = attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(action.getId(), AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, sort);
		if(!CollectionUtils.isEmpty(attachments)){
			List<Attachment> assessmentActionAttachments = new ArrayList<>();
			for(Attachment attachment:attachments){
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
			attachmentRepository.save(assessmentActionAttachments);
		}
	}

	public List<Topic> findTopicsByRunInstanceId(Long runInstanceId) {
		List<Topic> topics = topicRepository.findTopicByRunInstanceIdOrderByCreatedDateAsc(runInstanceId);
		if (!CollectionUtils.isEmpty(topics)) {
			for(Topic topic:topics){
				Ingredient ingredient = ingredientRepository.findByTopicId(topic.getId());
		        List<Product> products = productRepository.findByTopicId(topic.getId());
		        List<License> licenses = licenseRepository.findByTopicId(topic.getId());
		        if(ingredient != null) {
		            ingredient.setProducts(products);
		            ingredient.setLicenses(licenses);
		            topic.setIngredient(ingredient);
		        }
		        List<Soc> socs  = socRepository.findByTopicId(topic.getId());
		        if(!CollectionUtils.isEmpty(socs)) {
		        	for(Soc soc:socs){
		        		soc.setHlgts(hlgtRepository.findBySocId(soc.getId()));
		        		soc.setHlts(hltRepository.findBySocId(soc.getId()));
		        		soc.setPts(ptRepository.findBySocId(soc.getId()));
		        	}
		        }
		        topic.setSocs(socs);
			}
		}
		return topics;
	}

	public void deleteSignalURL(Long signalUrlId) throws EntityNotFoundException {
		SignalURL signalURL = signalURLRepository.findOne(signalUrlId);
        if(signalURL == null) {
            throw new EntityNotFoundException("Risk Plan Action Type not found with the given Id : "+signalUrlId);
        }
        signalURLRepository.delete(signalURL);
	}
	
}
