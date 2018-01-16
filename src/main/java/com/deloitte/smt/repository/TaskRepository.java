package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.Task;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	List<Task> findAllByAssessmentPlanIdAndStatus(Long assessmentId, String actionStatus);
	List<Task> findAllByAssessmentPlanId(Long assessmentId);
	List<Task> findAllByTemplateId(Long templateId);
	List<Task> findAllByRiskId(Long riskId);
	
	Long countByNameIgnoreCaseAndAssessmentPlanId(String actionName,Long assessmentId);
	Task findByNameIgnoreCaseAndAssessmentPlanId(String actionName,Long assessmentId);
	
	List<Task> findAllByRiskIdAndStatusOrderByCreatedDateDesc(Long riskId, String status);
	List<Task> findAllByRiskIdOrderByCreatedDateDesc(Long riskId);
	
	Long countByNameIgnoreCaseAndRiskId(String risName,Long riskId);
	
	Task findByNameIgnoreCaseAndRiskId(String risName, Long riskId);
	Task findByNameIgnoreCaseAndTemplateId(String name,Long templateId);
}
