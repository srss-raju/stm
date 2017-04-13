package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.RiskPlan;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Repository
public interface RiskPlanRepository extends JpaRepository<RiskPlan, Long> {
	
	List<RiskPlan> findAllByStatus(String status);
}
