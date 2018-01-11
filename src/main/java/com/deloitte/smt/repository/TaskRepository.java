package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.Task;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	List<Task> findAllByAssessmentPlanIdAndStatus(String assessmentId, String actionStatus);
	List<Task> findAllByAssessmentPlanId(String assessmentId);
	List<Task> findAllByTemplateId(Long templateId);
	
	Long countByNameIgnoreCaseAndAssessmentPlanId(String actionName,String assessmentId);
	Task findByNameIgnoreCaseAndAssessmentPlanId(String actionName,String assessmentId);
	Task findByNameIgnoreCaseAndTemplateId(String actionName,Long templateId);
}