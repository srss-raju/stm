package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Topic;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {
	List<Topic> findAllByCaseInstanceIdIn(List<String> caseInstanceId);
	Long countByCaseInstanceIdIn(List<String> caseInstanceId);
}
