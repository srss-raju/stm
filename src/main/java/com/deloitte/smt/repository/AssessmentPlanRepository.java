package com.deloitte.smt.repository;

import com.deloitte.smt.entity.AssessmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {
	List<AssessmentPlan> findAllByCaseInstanceIdIn(List<String> caseInstanceId);
	Long countByAssessmentPlanStatusNotLikeIgnoreCase(String assessmentPlanStatus);

	List<AssessmentPlan> findAllByAssessmentPlanStatusInOrderByCreatedDateDesc(List<String> assessmentPlanStatus);

	List<AssessmentPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);

	@Query("SELECT o FROM AssessmentPlan o WHERE o.assessmentPlanStatus IN (:assessmentPlanStatus) AND o.createdDate = :createdDate ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssessmentPlanStatusAndCreatedDate(@Param("assessmentPlanStatus") List<String> assessmentPlanStatus, @Param("createdDate") Date createdDate);
}
