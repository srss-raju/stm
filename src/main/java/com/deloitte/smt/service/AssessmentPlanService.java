package com.deloitte.smt.service;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Set<Long> topicIds = new HashSet<>();
        boolean searchAll = true;
        if(!CollectionUtils.isEmpty(searchDto.getStatuses())) {
            searchAll = false;
        }
        searchAll = searchService.getSignalIdsForSearch(searchDto, topicIds, searchAll);
        if(searchAll) {
            Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
            return assessmentPlanRepository.findAll(sort);
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
