package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Task;

public interface RiskTaskRepository extends JpaRepository<Task, Long> {

	List<Task> findAllByRiskIdAndStatusOrderByCreatedDateDesc(String riskId, String status);
	List<Task> findAllByRiskIdOrderByCreatedDateDesc(String riskId);
	
	Long countByNameIgnoreCaseAndRiskId(String risName,String riskId);
	List<Task> findAllByTemplateId(Long templateId);
	
	Task findByNameIgnoreCaseAndRiskId(String risName, String riskId);
	Task findByNameIgnoreCaseAndTemplateId(String name,Long templateId);
	
}
