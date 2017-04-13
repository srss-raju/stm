package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.RiskTask;

public interface RiskTaskRepository extends JpaRepository<RiskTask, Long> {

	List<RiskTask> findAllByRiskIdAndStatus(String riskId, String status);
	List<RiskTask> findAllByRiskId(String riskId);
}
