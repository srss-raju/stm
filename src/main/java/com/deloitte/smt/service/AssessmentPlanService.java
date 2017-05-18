package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.TopicRepository;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@Service
public class AssessmentPlanService {

    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LicenseRepository licenseRepository;

    @Autowired
    IngredientRepository ingredientRepository;
    
    @Autowired
    CommentsRepository commentsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    SearchService searchService;

    public AssessmentPlan findById(Long assessmentId) throws EntityNotFoundException {
        AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
        if(assessmentPlan == null) {
            throw new EntityNotFoundException("Assessment Plan not found with given Id :"+assessmentId);
        }
        if("New".equalsIgnoreCase(assessmentPlan.getAssessmentPlanStatus())) {
            assessmentPlan.setAssessmentPlanStatus("In Progress");
            assessmentPlan = assessmentPlanRepository.save(assessmentPlan);
        }
        assessmentPlan.setComments(commentsRepository.findByAssessmentId(assessmentId));
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

    public List<AssessmentPlan> findAllAssessmentPlansForSearch(SearchDto searchDto) {
        List<Long> topicIds = new ArrayList<>();
        boolean searchAll = true;
        if(!CollectionUtils.isEmpty(searchDto.getStatuses())) {
            searchAll = false;
        }
        searchAll = searchService.getSignalIdsForSearch(searchDto, topicIds, searchAll);
        if(searchAll) {
            Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
           // return assessmentPlanRepository.findAllByAssignToInOrderByCreatedDateDesc(searchDto.getAssignees());
        }
        List<AssessmentPlan> assessmentPlanList = new ArrayList<>();
        boolean executeQuery = false;
        StringBuilder queryString = new StringBuilder("SELECT o FROM AssessmentPlan o ");
        if(!CollectionUtils.isEmpty(topicIds)) {
            if (!CollectionUtils.isEmpty(searchDto.getProducts()) || !CollectionUtils.isEmpty(searchDto.getLicenses()) || !CollectionUtils.isEmpty(searchDto.getIngredients())) {
                executeQuery = true;
                queryString.append("INNER JOIN o.topics t WHERE t.id IN :topicIds ");
            }
        }
            if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
                executeQuery = true;
                if (queryString.toString().contains(":topicIds")) {
                    queryString.append(" AND o.assessmentPlanStatus IN :assessmentPlanStatus ");
                } else {
                    queryString.append(" WHERE o.assessmentPlanStatus IN :assessmentPlanStatus ");
                }
            }
            
            if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
            	executeQuery = true;
            	if (queryString.toString().contains("WHERE")){
            		queryString.append(" AND o.assignTo IN :assignees ");
            	}else{
            		queryString.append(" WHERE o.assignTo IN :assignees ");
            	}
            }
            
            if(null != searchDto.getStartDate()){
            	executeQuery = true;
            	if (queryString.toString().contains("WHERE")){
            		if(searchDto.isDueDate()){
                		queryString.append(" AND assessmentDueDate BETWEEN :startDate and :endDate ");
                	}else{
                		queryString.append(" AND createdDate BETWEEN :startDate and :endDate ");
                	}
            	}else{
            		if(searchDto.isDueDate()){
                		queryString.append(" WHERE assessmentDueDate BETWEEN :startDate and :endDate ");
                	}else{
                		queryString.append(" WHERE createdDate BETWEEN :startDate and :endDate ");
                	}
            	}
           		
            }
            
            if (!CollectionUtils.isEmpty(searchDto.getAssessmentTaskStatus())) {
            	executeQuery = true;
            	if (queryString.toString().contains("WHERE")){
            		queryString.append(" AND o.assessmentTaskStatus IN :assessmentTaskStatus ");
            	}else{
            		queryString.append(" WHERE o.assessmentTaskStatus IN :assessmentTaskStatus ");
            	}
            }
            
            
            queryString.append(" ORDER BY o.createdDate DESC");
            Query q = entityManager.createQuery(queryString.toString(), AssessmentPlan.class);

            if (queryString.toString().contains(":topicIds")) {
                if (CollectionUtils.isEmpty(topicIds)) {
                    q.setParameter("topicIds", null);
                } else {
                    q.setParameter("topicIds", topicIds);
                }
            }
            if (queryString.toString().contains(":assessmentPlanStatus")) {
                q.setParameter("assessmentPlanStatus", searchDto.getStatuses());
            }
            
            if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
	            if (queryString.toString().contains(":assignees")) {
	                q.setParameter("assignees", searchDto.getAssignees());
	            }
            }
            
            if(null != searchDto.getStartDate()){
	            if (queryString.toString().contains(":startDate")) {
	                q.setParameter("startDate", searchDto.getStartDate());
	            }
	            
	            if (queryString.toString().contains(":endDate")) {
	                q.setParameter("endDate", searchDto.getEndDate());
	            }
            }
            
            if (null != searchDto.getAssessmentTaskStatus()) {
            	if (queryString.toString().contains(":assessmentTaskStatus")) {
	                q.setParameter("assessmentTaskStatus", searchDto.getAssessmentTaskStatus());
	            }
            }
            if(executeQuery) {
                assessmentPlanList = q.getResultList();
            }
        return assessmentPlanList;
    }

    public void updateAssessment(AssessmentPlan assessmentPlan, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(assessmentPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Assessment. Invalid Id received");
        }
        assessmentPlan.setLastModifiedDate(new Date());
        attachmentService.addAttachments(assessmentPlan.getId(), attachments, AttachmentType.ASSESSMENT_ATTACHMENT, assessmentPlan.getDeletedAttachmentIds(), assessmentPlan.getFileMetadata());
        assessmentPlanRepository.save(assessmentPlan);
        List<Comments> list = assessmentPlan.getComments();
        if(!CollectionUtils.isEmpty(list)){
        	for(Comments comment:list){
        		comment.setAssessmentId(assessmentPlan.getId());
        		comment.setModifiedDate(new Date());
        	}
        }
        commentsRepository.save(assessmentPlan.getComments());
    }

    public void finalAssessment(AssessmentPlan assessmentPlan, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(assessmentPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Assessment. Invalid Id received");
        }
        assessmentPlan.setLastModifiedDate(new Date());
        assessmentPlan.setAssessmentPlanStatus("Completed");
        attachmentService.addAttachments(assessmentPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT, assessmentPlan.getDeletedAttachmentIds(), assessmentPlan.getFileMetadata());
        assessmentPlanRepository.save(assessmentPlan);
    }
}
