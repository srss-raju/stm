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

	Long countByAssignToAndStatusNotLikeIgnoreCase(String assignTo, String status);

	@Query(value = "SELECT t.id FROM RiskPlan o inner join o.assessmentPlan a inner join a.topics t")
	Set<Long> findAllSignalIds();
	
	@Query("SELECT DISTINCT(o.assignTo) FROM RiskPlan o WHERE o.assignTo IS NOT NULL")
	List<String> getAssignedUsers();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE RiskPlan o SET o.riskTaskStatus=:riskTaskStatus WHERE id= :id")
	void updateRiskTaskStatus(@Param("riskTaskStatus") String riskTaskStatus, @Param("id") Long id);
	
	Long countByNameIgnoreCase(String topicName);
}
