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
import com.deloitte.smt.entity.Topic;

public interface AssessmentPlanRepository extends JpaRepository<AssessmentPlan, Long> {

	List<AssessmentPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);
	
	@Query("SELECT DISTINCT(o.assessmentRiskStatus) FROM AssessmentPlan o WHERE o.assessmentRiskStatus IS NOT NULL")
	List<String> getAssessmentRiskStatus();
	
	@Transactional
	@Modifying(clearAutomatically=true)
	@Query(value = "UPDATE AssessmentPlan o SET o.assessmentTaskStatus=:assessmentTaskStatus WHERE id= :id")
	void updateAssessmentTaskStatus(@Param(value="assessmentTaskStatus") String assessmentTaskStatus, @Param(value="id") Long id);
	
	Long countByAssessmentNameIgnoreCase(String assessmentName);
	
	@Query("SELECT o.assessmentName from AssessmentPlan o where o.assessmentName not in (select a.assessmentName from AssessmentPlan a where a.assessmentName= :assessmentName AND a.id=:id)")
	List<String> findByAssessmentName(@Param(value = "assessmentName") String assessmentName,@Param(value = "id") Long id);
	
}
