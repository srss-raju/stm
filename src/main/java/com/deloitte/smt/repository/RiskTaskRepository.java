package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.RiskTask;

public interface RiskTaskRepository extends JpaRepository<RiskTask, Long> {

	List<RiskTask> findAllByRiskIdAndStatusOrderByCreatedDateDesc(String riskId, String status);
	List<RiskTask> findAllByRiskIdOrderByCreatedDateDesc(String riskId);
	
	Long countByNameIgnoreCaseAndRiskId(String risName,String riskId);
	List<RiskTask> findAllByTemplateId(Long templateId);
	
	RiskTask findByNameIgnoreCaseAndTemplateId(String name,Long templateId);
	
}
