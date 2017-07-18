package com.deloitte.smt.repository;

import com.deloitte.smt.entity.RiskTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTaskRepository extends JpaRepository<RiskTask, Long> {

	List<RiskTask> findAllByRiskIdAndStatusOrderByCreatedDateDesc(String riskId, String status);
	List<RiskTask> findAllByRiskIdOrderByCreatedDateDesc(String riskId);
	
	Long countByNameIgnoreCaseAndRiskId(String risName,String riskId);
	
}
