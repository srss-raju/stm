package com.deloitte.smt.service;

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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Comments;
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
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.RiskPlanAssigneesRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.repository.TaskTemplateProductsRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.AssignmentUtil;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@Transactional
@Service
public class AssessmentPlanService {


	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
    AssessmentPlanRepository assessmentPlanRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    CommentsRepository commentsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    AssessmentAssignmentService assessmentAssignmentService;
    
    @Autowired
    SignalURLRepository signalURLRepository;
    
    @Autowired
    RiskPlanService riskPlanService;
    
    @Autowired
    SignalService signalService;
    
    @Autowired
    ExceptionBuilder exceptionBuilder;
    
    @Autowired
    TaskRepository taskRepository;
    
    @Autowired
	RiskPlanAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
    
    @Autowired
    TaskTemplateProductsRepository taskTemplateProductsRepository;
    
    @Autowired
    TaskTemplateRepository taskTemplateRepository;
    
    public AssessmentPlan findById(Long assessmentId) throws ApplicationException {
        AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
        if(assessmentPlan == null) {
            throw new ApplicationException("Assessment Plan not found with given Id :"+assessmentId);
        }
		
        if("New".equalsIgnoreCase(assessmentPlan.getAssessmentPlanStatus())) {
            assessmentPlan.setAssessmentPlanStatus("In Progress");
            assessmentPlan = assessmentPlanRepository.save(assessmentPlan);
        }
        if(assessmentPlan.getAssessmentPlanStatus().equals(SmtConstant.COMPLETED.getDescription()) && StringUtils.isEmpty(assessmentPlan.getFinalAssessmentSummary())){
        	assessmentPlan.setFinalAssessmentSummary(SmtConstant.SUMMARY_COMPLETED.getDescription());
        }
        assessmentPlan.setComments(commentsRepository.findByAssessmentId(assessmentId));
        assessmentPlan.setSignalUrls(signalURLRepository.findByTopicId(assessmentId));
        return assessmentPlan;
    }

    public void unlinkSignalToAssessment(Long assessmentId, Long topicId){
        Topic t = null;
        AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
        if(assessmentPlan != null && !CollectionUtils.isEmpty(assessmentPlan.getTopics())) {
            for(Topic topic : assessmentPlan.getTopics())  {
                if(topic.getId() == topicId) {
                    t = topic;
                    break;
                }
            }
            if(t != null) {
                assessmentPlan.getTopics().remove(t);
                t.setAssessmentPlan(null);
                assessmentPlanRepository.save(assessmentPlan);
                topicRepository.save(t);
            }
        }
    }

    public void updateAssessment(AssessmentPlan assessmentPlan) throws ApplicationException {
        if(assessmentPlan.getId() == null) {
            throw new ApplicationException("Failed to update Assessment. Invalid Id received");
        }
        
        AssessmentPlan assessmentPlanFromDB = assessmentPlanRepository.findOne(assessmentPlan.getId());
        ownerCheck(assessmentPlan, assessmentPlanFromDB);
        
        assessmentPlan.setLastModifiedDate(new Date());
        setAssessmentTaskStatus(assessmentPlan);
        
        if(null==assessmentPlan.getFinalAssessmentSummary()|| assessmentPlan.getFinalAssessmentSummary().isEmpty()){
        assessmentPlan.setFinalAssessmentSummary(SmtConstant.SUMMARY.getDescription());
	        if(assessmentPlan.getAssessmentPlanStatus().equals(SmtConstant.COMPLETED.getDescription())){
	        	 assessmentPlan.setFinalAssessmentSummary(SmtConstant.SUMMARY_COMPLETED.getDescription());
	        }
        }
        else{
        	assessmentPlan.setFinalAssessmentSummary(assessmentPlan.getFinalAssessmentSummary());
        }
        assessmentPlanRepository.save(assessmentPlan);
        List<Comments> list = assessmentPlan.getComments();
        if(!CollectionUtils.isEmpty(list)){
        	for(Comments comment:list){
        		comment.setAssessmentId(assessmentPlan.getId());
        		comment.setModifiedDate(new Date());
        	}
        }
        commentsRepository.save(assessmentPlan.getComments());
        if(!CollectionUtils.isEmpty(assessmentPlan.getSignalUrls())){
        	for(SignalURL url:assessmentPlan.getSignalUrls()){
        		url.setTopicId(assessmentPlan.getId());
        		url.setCreatedDate(assessmentPlan.getCreatedDate());
				url.setCreatedBy(assessmentPlan.getCreatedBy());
				url.setModifiedBy(assessmentPlan.getModifiedBy());
				url.setModifiedDate(assessmentPlan.getLastModifiedDate());
        	}
        	signalURLRepository.save(assessmentPlan.getSignalUrls());
        }
        
    }

	private void ownerCheck(AssessmentPlan assessmentPlan, AssessmentPlan assessmentPlanFromDB) throws ApplicationException {
		if(assessmentPlanFromDB != null && assessmentPlanFromDB.getOwner() != null && (!assessmentPlanFromDB.getOwner().equalsIgnoreCase(assessmentPlan.getOwner()))){
        	throw new ApplicationException("Unable to make you as a Owner");
        }
	}
    /**
     * This method sets the assessment task status as completed 
     * when all tasks are completed else not completed
     * @param assessmentPlan
     */
    private void setAssessmentTaskStatus(AssessmentPlan assessmentPlan){
    	boolean assessmentTaskStatus=false;
       List<Task> signalActionsStatus=taskRepository.findAllByAssessmentPlanId(String.valueOf(assessmentPlan.getId()));
       if(!CollectionUtils.isEmpty(signalActionsStatus)){
    	   for(Task signalAction:signalActionsStatus){
    		   if(!signalAction.getStatus().equals(SmtConstant.COMPLETED.getDescription())){
    			   assessmentTaskStatus=true;
    		   }
    	   }
       }
    	   if(assessmentTaskStatus){
    	   assessmentPlan.setAssessmentTaskStatus(SmtConstant.NOTCOMPLETED.getDescription());
    	   }
    	 
       
    }
 
    public void finalAssessment(AssessmentPlan assessmentPlan) throws ApplicationException {
        if(assessmentPlan.getId() == null) {
            throw new ApplicationException("Failed to update Assessment. Invalid Id received");
        }
        assessmentPlan.setLastModifiedDate(new Date());
        assessmentPlan.setAssessmentPlanStatus("Completed");
        assessmentPlanRepository.save(assessmentPlan);
    }
    
	/**
	 * * @param id
	 * 
	 * @param assessmentName
	 * @throws ApplicationException
	 */
	public void updateAssessmentName(Long id, String assessmentName) throws ApplicationException {

		List<AssessmentPlan> list = assessmentPlanRepository.findAll();
		List<String> assessmentNames = assessmentPlanRepository.findByAssessmentName(assessmentName,id);

		for (AssessmentPlan assessmentPlan : list) {

			if (id.equals(assessmentPlan.getId()) && assessmentNames.contains(assessmentName)) {
				throw exceptionBuilder.buildException(ErrorType.ASSESSMENTPLAN_NAME_DUPLICATE, null);
			} else if (id.equals(assessmentPlan.getId()) && !assessmentNames.contains(assessmentName)) {
				assessmentPlan.setLastModifiedDate(new Date());
				assessmentPlan.setAssessmentName(assessmentName);
				assessmentPlanRepository.save(assessmentPlan);
			}
		}

	}

	public List<TaskTemplate> getTaskTamplatesOfAssessmentProducts(Long assessmentId) throws ApplicationException {
		List<TaskTemplate> taskTemplates = new ArrayList<>();
		AssessmentPlan assessmentPlan = null;
		try{
			assessmentPlan = findById(assessmentId);
		}catch(Exception ex){
			logger.error(ex);
		}
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
		for(TopicProduct record : topicWithConditionsAndProducts.getProducts()){
			String recordKey = record.getRecordKey();
			TaskTemplateProducts taskTemplateProduct = getTaskTemplateProduct(recordKey);
			while(recordKey != null && taskTemplateProduct == null){
				recordKey = AssignmentUtil.getRecordKey(recordKey);
				taskTemplateProduct = getTaskTemplateProduct(recordKey);
			}
			if(taskTemplateProduct != null){
				Long taskTemplateId = taskTemplateProductsRepository.findTemplateId(taskTemplateProduct.getId());
				taskTemplates.add(taskTemplateRepository.findOne(taskTemplateId));
			}
		}
	}

	private TaskTemplateProducts getTaskTemplateProduct(String recordKey) {
		return taskTemplateProductsRepository.findByRecordKey(recordKey);
	}
}
