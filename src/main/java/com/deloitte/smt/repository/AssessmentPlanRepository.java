package com.deloitte.smt.repository;

import com.deloitte.smt.entity.AssessmentPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {

	Long countByAssignToAndAssessmentPlanStatusNotLikeIgnoreCase(String assignTo, String assessmentPlanStatus);

	@Query(value = "SELECT t.id FROM AssessmentPlan o inner join o.topics t")
	Set<Long> findAllSignalIds();

	List<AssessmentPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);
	
	@Query(value = "SELECT o FROM AssessmentPlan o WHERE o.assignTo IN (:assignes)  ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssignToInOrderByCreatedDateDesc(@Param("assignes") List<String> assignes);

	@Query("SELECT o FROM AssessmentPlan o WHERE o.assessmentPlanStatus IN (:assessmentPlanStatus) AND o.createdDate = :createdDate ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssessmentPlanStatusAndCreatedDate(@Param("assessmentPlanStatus") List<String> assessmentPlanStatus, @Param("createdDate") Date createdDate);
}
