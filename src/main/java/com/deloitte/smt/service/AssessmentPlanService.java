package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@Service
public class AssessmentPlanService {

    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;

    public AssessmentPlan findById(Long assessmentId){
        return assessmentPlanRepository.findOne(assessmentId);
    }

    public List<AssessmentPlan> findAllAssessmentPlansByStatus(String assessmentPlanStatus) {
        return assessmentPlanRepository.findAllByAssessmentPlanStatus(assessmentPlanStatus);
    }
}
