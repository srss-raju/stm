package com.deloitte.smt.repository;

import com.deloitte.smt.entity.AssessmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {
	List<AssessmentPlan> findAllByCaseInstanceIdIn(List<String> caseInstanceId);
	Long countByCaseInstanceIdIn(List<String> caseInstanceId);

	List<AssessmentPlan> findAllByAssessmentPlanStatus(String assessmentPlanStatus);
}
