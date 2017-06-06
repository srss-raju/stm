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

	Long countByAssignToAndAssessmentPlanStatusNotLikeIgnoreCase(String assignTo, String assessmentPlanStatus);

	@Query(value = "SELECT t.id FROM AssessmentPlan o inner join o.topics t")
	Set<Long> findAllSignalIds();

	List<AssessmentPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);
	
	@Query(value = "SELECT o FROM AssessmentPlan o WHERE o.assignTo IN (:assignes)  ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssignToInOrderByCreatedDateDesc(@Param("assignes") List<String> assignes);

	@Query("SELECT o FROM AssessmentPlan o WHERE o.assessmentPlanStatus IN (:assessmentPlanStatus) AND o.createdDate = :createdDate ORDER BY o.createdDate DESC")
	List<AssessmentPlan> findAllByAssessmentPlanStatusAndCreatedDate(@Param("assessmentPlanStatus") List<String> assessmentPlanStatus, @Param("createdDate") Date createdDate);
	
	@Query("SELECT DISTINCT(o.assignTo) FROM AssessmentPlan o WHERE o.assignTo IS NOT NULL")
	List<String> getAssignedUsers();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE AssessmentPlan o SET o.assessmentTaskStatus=:assessmentTaskStatus WHERE id= :id")
	void updateAssessmentTaskStatus(@Param("assessmentTaskStatus") String assessmentTaskStatus, @Param("id") Long id);
	
	@Query(value="select a.* from sm_topic t inner join sm_assessment_plan a on t.assessment_plan_id=a.id inner join sm_ingredient i on i.topic_id=t.id and  i.ingredient_name=?1",nativeQuery=true)
	List<AssessmentPlan> getAssessmentPlansByIngredientName(String ingredientName);
	
}
