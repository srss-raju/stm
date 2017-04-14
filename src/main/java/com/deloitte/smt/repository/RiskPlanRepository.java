package com.deloitte.smt.repository;

import com.deloitte.smt.entity.RiskPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Repository
public interface RiskPlanRepository extends JpaRepository<RiskPlan, Long> {
	
	List<RiskPlan> findAllByStatusInOrderByCreatedDateDesc(List<String> status);

	List<RiskPlan> findAllByCreatedDateOrderByCreatedDateDesc(Date createdDate);

	@Query("SELECT o FROM RiskPlan o WHERE o.status IN (:status) AND o.createdDate = :createdDate ORDER BY o.createdDate DESC")
	List<RiskPlan> findAllByStatusAndCreatedDate(@Param("status") List<String> status, @Param("createdDate") Date createdDate);
}
