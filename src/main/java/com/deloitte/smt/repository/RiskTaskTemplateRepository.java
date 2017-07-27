package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.RiskTaskTemplate;

@Repository
public interface RiskTaskTemplateRepository  extends JpaRepository<RiskTaskTemplate, Long> {
	
	List<RiskTaskTemplate> findAllByOrderByCreatedDateDesc();
	
	List<RiskTaskTemplate> findByIdIn(List<Long> ids);

}
