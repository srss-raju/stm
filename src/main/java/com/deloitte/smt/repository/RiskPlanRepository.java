package com.deloitte.smt.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.RiskPlan;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Repository
public interface RiskPlanRepository extends JpaRepository<RiskPlan, Long> {
	
	List<RiskPlan> findAllByStatusInOrderByCreatedDateDesc(List<String> status);

	List<RiskPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);
	
	@Query(value = "SELECT o FROM RiskPlan o WHERE o.assignTo IN (:assignes)  ORDER BY o.createdDate DESC")
	List<RiskPlan> findAllByAssignToInOrderByCreatedDateDesc(@Param("assignes") List<String> assignes);

	@Query("SELECT o FROM RiskPlan o WHERE o.status IN (:status) AND o.createdDate = :createdDate ORDER BY o.createdDate DESC")
	List<RiskPlan> findAllByStatusAndCreatedDate(@Param("status") List<String> status, @Param("createdDate") Date createdDate);

	@Query(value = "SELECT t.id FROM RiskPlan o inner join o.assessmentPlan a inner join a.topics t")
	Set<Long> findAllSignalIds();
	
	@Transactional
	@Modifying(clearAutomatically=true)
	@Query(value = "UPDATE RiskPlan o SET o.riskTaskStatus=:riskTaskStatus WHERE id= :id")
	void updateRiskTaskStatus(@Param(value="riskTaskStatus") String riskTaskStatus, @Param(value="id") Long id);
	
	Long countByNameIgnoreCase(String topicName);
	
	@Query("SELECT DISTINCT(o.owner) FROM  RiskPlan o WHERE o.owner IS NOT NULL")
	List<String> findOwnersOnRiskPlan();
	
	@Query("SELECT o.name from RiskPlan o where o.name not in (select a.name from RiskPlan a where a.name= :name AND a.id=:id)")
	List<String> findByRiskName(@Param(value = "name") String name,@Param(value = "id") Long id);
	
	RiskPlan findByAssessmentId(Long assessmentId);
	
	@Query(value="SELECT distinct p.productName from Product p, Ingredient i, RiskPlan o, AssessmentPlan a inner join a.topics t  where  t.assessmentPlan.id = a.id and  t.id=i.topic.id and i.id=p.ingredient.id ", nativeQuery=false)
	List<String> findDistinctProductNames();
	
	@Query(value="SELECT distinct i.ingredientName from Ingredient i, RiskPlan o, AssessmentPlan a inner join a.topics t where t.assessmentPlan.id = a.id and t.id=i.topic.id", nativeQuery=false)
	List<String> findDistinctIngredientNames();
	
}
