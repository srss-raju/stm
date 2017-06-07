package com.deloitte.smt.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.dto.AssessmentPlanDTO;
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
	
//	@Query(value="select NEW com.deloitte.smt.dto.AssessmentPlanDTO(i.ingredientName,a.assessmentName.a.assessmentPlanStatus) from Topic t , AssessmentPlan a , Ingredient i where t.assessmentPlan.id=a.id and  i.topicId=t.id order by i.ingredientName",nativeQuery=false)
//	List<AssessmentPlanDTO> getAssessmentPlansByIngredientName();
//	
}
