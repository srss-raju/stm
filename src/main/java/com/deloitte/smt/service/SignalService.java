package com.deloitte.smt.service;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalStatistics;
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
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.SignalUtil;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private IngredientRepository ingredientRepository;
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private LicenseRepository licenseRepository;
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
        topic.setSocs(socs);
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
        topic = topicRepository.save(topic);

        Ingredient ingredient = topic.getIngredient();
        if(ingredient != null) {
            AssignmentConfiguration assignmentConfiguration = null;
            if(!StringUtils.isEmpty(topic.getSourceName())) {
                assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSource(ingredient.getIngredientName(), topic.getSourceName());
            } 
            // If Source is not null and combination not available we have to fetch with Ingredient
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
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, null, topic.getFileMetadata());
        return topic;
    }

    public String updateTopic(Topic topic, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(topic.getId() == null) {
            throw new UpdateFailedException("Update failed for Topic, since it does not have any valid Id field.");
        }
        topic.setLastModifiedDate(new Date());
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, topic.getDeletedAttachmentIds(), topic.getFileMetadata());
        topicRepository.save(topic);
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
        AssignmentConfiguration assignmentConfiguration;
        if(!StringUtils.isEmpty(assessmentPlan.getSource())) {
            assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSource(assessmentPlan.getIngrediantName(), assessmentPlan.getSource());
        } else {
            assignmentConfiguration = assignmentConfigurationRepository.findByIngredient(assessmentPlan.getIngrediantName());
        }
        if(assignmentConfiguration != null){
            assessmentPlan.setAssignTo(assignmentConfiguration.getAssessmentAssignmentUser());
        }
        assessmentPlan.setCaseInstanceId(instance.getCaseInstanceId());
        topic.setAssessmentPlan(assessmentPlanRepository.save(assessmentPlan));
        topic.setSignalStatus("Completed");
        topic.setSignalValidation("Completed");
        topic.setLastModifiedDate(new Date());
        topicRepository.save(topic);

        return assessmentPlan;
    }

    public List<Topic> findAllForSearch(SearchDto searchDto) {
        List<Topic> topics;
        List<Long> topicIds = new ArrayList<>();
        searchService.getSignalIdsForSearch(searchDto, topicIds, true);
        StringBuilder queryString = new StringBuilder("SELECT o FROM Topic o WHERE 1=1 ");
        if(!CollectionUtils.isEmpty(searchDto.getProducts()) || !CollectionUtils.isEmpty(searchDto.getLicenses()) || !CollectionUtils.isEmpty(searchDto.getIngredients())){
            queryString.append(" AND id IN :ids ");
        }
        if(null != searchDto.getCreatedDate()){
            queryString.append(" AND DATE(createdDate) = :createdDate ");
        }
        if(!CollectionUtils.isEmpty(searchDto.getStatuses())){
            queryString.append(" AND signalStatus IN :signalStatus ");
        }

        if(!CollectionUtils.isEmpty(searchDto.getSignalNames())){
            queryString.append(" AND name IN :signalName ");
        }
        if(!CollectionUtils.isEmpty(searchDto.getSignalConfirmations())){
            queryString.append(" AND signalConfirmation IN :signalConfirmation ");
        }
        queryString.append(" ORDER BY createdDate DESC");
        Query q = entityManager.createQuery(queryString.toString(), Topic.class);

        if(queryString.toString().contains(":ids")){
            if(CollectionUtils.isEmpty(topicIds)) {
                q.setParameter("ids", null);
            } else {
                q.setParameter("ids", topicIds);
            }
        }
        if(queryString.toString().contains(":createdDate")){
            q.setParameter("createdDate", searchDto.getCreatedDate());
        }
        if(queryString.toString().contains(":signalStatus")){
            q.setParameter("signalStatus", searchDto.getStatuses());
        }

        if(queryString.toString().contains(":signalName")){
            q.setParameter("signalName", searchDto.getSignalNames());
        }
        if(queryString.toString().contains(":signalConfirmation")){
            q.setParameter("signalConfirmation", searchDto.getSignalConfirmations());
        }

        topics = q.getResultList();
        topics.stream().forEach(topic->{
            if(null == topic.getSignalValidation()) {
                topic.setSignalValidation("In Progress");
            }
        });
        return topics;
    }

    public Long getValidateAndPrioritizeCount(){
        return topicRepository.countBySignalStatusNotLikeIgnoreCase("Completed");
    	//return taskInstRepository.countByTaskDefKeyInAndDeleteReasonNot(Arrays.asList("validateTopic"), "completed");
    }
    
    public Long getAssessmentCount(){
    	///return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("assessment"));
        return assessmentPlanRepository.countByAssessmentPlanStatusNotLikeIgnoreCase("Completed");
    }
    
    public Long getRiskCount(){
    	return riskPlanRepository.countByStatusNotLikeIgnoreCase("Completed");
    	//return taskInstRepository.countByTaskDefKeyIn(Arrays.asList("risk"));
    }

    public String getCountsByFilter(String ingredientName) {
        List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameIn(Arrays.asList(ingredientName));
        List<Long> topicIds = new ArrayList<>();
        ingredients.parallelStream().forEach(ingredient -> topicIds.add(ingredient.getTopicId()));
        List<Topic> signals = topicRepository.findAllByIdInAndSignalStatusNotLikeOrderByCreatedDateDesc(topicIds, "Completed");
        List<AssessmentPlan> assessmentPlanList = signals.stream()
                .map(signal -> signal.getAssessmentPlan())
                .filter(e -> e != null && !"Completed".equalsIgnoreCase(e.getAssessmentPlanStatus()))
                .collect(Collectors.toList());
        List<RiskPlan> riskPlanList = assessmentPlanList.stream()
                .map(assessmentPlan -> assessmentPlan.getRiskPlan())
                .filter(e -> e != null && !"Completed".equalsIgnoreCase(e.getStatus()))
                .collect(Collectors.toList());
        return SignalUtil.getCounts(Long.valueOf(ingredients.size()), Long.valueOf(assessmentPlanList.size()), Long.valueOf(riskPlanList.size()));
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
		List<SignalAction> signalActionListCreated = new ArrayList<>();
		List<SignalAction> signalActionList;
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
						signalActionList.add(signalAction);
					}
				}
				if (!CollectionUtils.isEmpty(signalActionList)) {
					signalActionListCreated.addAll(assessmentActionRepository.save(signalActionList));
				}
			}
		}
		return signalActionListCreated;
	}

	public List<Topic> findTopicsByRunInstanceId(Long runInstanceId) {
		List<Topic> topics = topicRepository.findTopicByRunInstanceId(runInstanceId);
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
}
