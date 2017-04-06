package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.AssessmentPlan;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {
	List<AssessmentPlan> findAllByCaseInstanceIdIn(List<String> caseInstanceId);
	Long countByCaseInstanceIdIn(List<String> caseInstanceId);
}
