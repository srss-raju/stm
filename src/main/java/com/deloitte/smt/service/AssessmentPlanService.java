package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public List<AssessmentPlan> findAllAssessmentPlansForSearch(String assessmentPlanStatuses, Date createdDate) {
        if(!StringUtils.isEmpty(assessmentPlanStatuses) && null != createdDate) {
            return assessmentPlanRepository.findAllByAssessmentPlanStatusAndCreatedDate(Arrays.asList(assessmentPlanStatuses.split(",")), createdDate);
        }
        if(!StringUtils.isEmpty(assessmentPlanStatuses)) {
            return assessmentPlanRepository.findAllByAssessmentPlanStatusInOrderByCreatedDateDesc(Arrays.asList(assessmentPlanStatuses.split(",")));
        }
        if(null != createdDate) {
            return assessmentPlanRepository.findAllByCreatedDateOrderByCreatedDateDesc(createdDate);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return assessmentPlanRepository.findAll(sort);
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
