package com.deloitte.smt.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.AssessmentPlan;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {

	@Query(value = "SELECT t.id FROM AssessmentPlan o inner join o.topics t,TopicAssessmentAssignmentAssignees ta WHERE o.id=ta.assessmentId AND ta.userGroupKey=:userGroupKey AND t.owner=:owner")
	Set<Long> findAllSignalIds(@Param("owner") String owner,@Param("userGroupKey") Long userGroupKey);

	List<AssessmentPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);
	
	@Query(value = "SELECT o FROM AssessmentPlan o WHERE o.assignTo IN (:assignes)  ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssignToInOrderByCreatedDateDesc(@Param("assignes") List<String> assignes);

	@Query("SELECT o FROM AssessmentPlan o WHERE o.assessmentPlanStatus IN (:assessmentPlanStatus) AND o.createdDate = :createdDate ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssessmentPlanStatusAndCreatedDate(@Param("assessmentPlanStatus") List<String> assessmentPlanStatus, @Param("createdDate") Date createdDate);
	
	@Query("SELECT DISTINCT(o.assessmentRiskStatus) FROM AssessmentPlan o WHERE o.assessmentRiskStatus IS NOT NULL")
	List<String> getAssessmentRiskStatus();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE AssessmentPlan o SET o.assessmentTaskStatus=:assessmentTaskStatus WHERE id= :id")
	void updateAssessmentTaskStatus(@Param("assessmentTaskStatus") String assessmentTaskStatus, @Param("id") Long id);
	
	Long countByAssessmentNameIgnoreCase(String assessmentName);
	
	
}
