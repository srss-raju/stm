package com.deloitte.smt.service;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.exception.TopicNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Topic findById(Long topicId){
        Topic topic = topicRepository.findOne(topicId);
        if(null == topic.getSignalConfirmation()) {
            topic.setSignalConfirmation("Validated Signal");
        }
        /*if(null == topic.getSignalValidation()) {
            topic.setSignalValidation("In Progress");
        }*/
        if("New".equalsIgnoreCase(topic.getSignalStatus())) {
            topic.setSignalStatus("In Progress");
            topic = topicRepository.save(topic);
        }
        Ingredient ingredient = ingredientRepository.findByTopicId(topic.getId());
        List<Product> products = productRepository.findByTopicId(topic.getId());
        List<License> licenses = licenseRepository.findByTopicId(topic.getId());
        ingredient.setProducts(products);
        ingredient.setLicenses(licenses);
        topic.setIngredient(ingredient);
        return topic;
    }

    public String createTopic(Topic topic, MultipartFile[] attachments) throws IOException {
    	Ingredient ingredient = topic.getIngredient();
    	List<Product> products = ingredient.getProducts();
    	List<License> licenses = ingredient.getLicenses();
    	String processInstanceId = runtimeService.startProcessInstanceByKey("topicProcess").getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        taskService.delegateTask(task.getId(), "Demo Demo");
        if(topic.getId() != null) {
            topic.setId(null);
        }
        if(null == topic.getSignalConfirmation()) {
            topic.setSignalConfirmation("Validated Signal");
        }
        if(null == topic.getSignalValidation()) {
            topic.setSignalValidation("In Progress");
        }
        Date d = new Date();
        topic.setCreatedDate(d);
        topic.setLastModifiedDate(d);
        topic.setSignalStatus("New");
        topic.setProcessId(processInstanceId);
        topic = topicRepository.save(topic);
        ingredient.setTopicId(topic.getId());
        ingredient = ingredientRepository.save(ingredient);
        
        for(Product singleProduct:products){
        	singleProduct.setIngredientId(ingredient.getId());
        	singleProduct.setTopicId(topic.getId());
        }
        
        for(License singleLicense:licenses){
        	singleLicense.setIngredientId(ingredient.getId());
        	singleLicense.setTopicId(topic.getId());
        }
        
        productRepository.save(products);
        licenseRepository.save(licenses);
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, null);
        return processInstanceId;
    }

    public String updateTopic(Topic topic, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(topic.getId() == null) {
            throw new UpdateFailedException("Update failed for Topic, since it does not have any valid Id field.");
        }
        topic.setLastModifiedDate(new Date());
        attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, topic.getDeletedAttachmentIds());
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
        assessmentPlan.setCaseInstanceId(instance.getCaseInstanceId());
        assessmentPlan = assessmentPlanRepository.save(assessmentPlan);
        topic.setAssessmentPlan(assessmentPlan);
        topic.setSignalStatus("Completed");
        topic.setSignalValidation("Completed");
        topic.setLastModifiedDate(new Date());
        topicRepository.save(topic);

        return assessmentPlan;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Topic> findAllForSearch(SearchDto searchDto) {
        List<Topic> topics;
        Set<Long> topicIds = new HashSet<>();
        if(!CollectionUtils.isEmpty(searchDto.getProducts())) {
            List<Product> products = productRepository.findAllByProductNameIn(searchDto.getProducts());
            products.parallelStream().forEach(product -> topicIds.add(product.getTopicId()));
        }
        if(!CollectionUtils.isEmpty(searchDto.getLicenses())) {
            List<License> licenses = licenseRepository.findAllByLicenseNameIn(searchDto.getLicenses());
            licenses.parallelStream().forEach(product -> topicIds.add(product.getTopicId()));
        }
        if(!CollectionUtils.isEmpty(searchDto.getIngredients())) {
            List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameIn(searchDto.getIngredients());
            ingredients.parallelStream().forEach(product -> topicIds.add(product.getTopicId()));
        }

        StringBuilder queryString = new StringBuilder("SELECT o FROM Topic o WHERE 1=1 ");
        if(!CollectionUtils.isEmpty(searchDto.getProducts()) || !CollectionUtils.isEmpty(searchDto.getLicenses())
                || !CollectionUtils.isEmpty(searchDto.getIngredients())){
            queryString.append(" AND id IN :ids ");
        }
        if(null != searchDto.getCreatedDate()){
            queryString.append(" AND createdDate = :createdDate ");
        }
        if(!CollectionUtils.isEmpty(searchDto.getStatuses())){
            queryString.append(" AND signalStatus IN :signalStatus ");
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

        topics = q.getResultList();
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
}
