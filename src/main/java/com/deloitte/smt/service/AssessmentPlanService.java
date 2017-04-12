package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public AssessmentPlan findById(Long assessmentId){
        return assessmentPlanRepository.findOne(assessmentId);
    }

    public List<AssessmentPlan> findAllAssessmentPlansByStatus(String assessmentPlanStatus) {
        if(StringUtils.isEmpty(assessmentPlanStatus)) {
            return assessmentPlanRepository.findAll();
        }
        return assessmentPlanRepository.findAllByAssessmentPlanStatus(assessmentPlanStatus);
    }

    public void updateAssessment(AssessmentPlan assessmentPlan, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(assessmentPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Assessment. Invalid Id received");
        }
        attachmentService.addAttachments(assessmentPlan.getId(), attachments, AttachmentType.ASSESSMENT_ATTACHMENT);
        assessmentPlanRepository.save(assessmentPlan);
    }

    public void finalAssessment(AssessmentPlan assessmentPlan, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(assessmentPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Assessment. Invalid Id received");
        }
        assessmentPlan.setAssessmentPlanStatus(AttachmentType.FINAL_ASSESSMENT.getDescription());
        attachmentService.addAttachments(assessmentPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT);
        assessmentPlanRepository.save(assessmentPlan);
    }
}
