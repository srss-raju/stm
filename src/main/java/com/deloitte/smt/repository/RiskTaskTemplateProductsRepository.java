package com.deloitte.smt.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.RiskTaskTemplateProducts;

@Repository
public interface RiskTaskTemplateProductsRepository  extends JpaRepository<RiskTaskTemplateProducts, Long> {
	
	RiskTaskTemplateProducts findByRecordKey(String recordKey);
	
	@Query(value = "SELECT DISTINCT risk_task_template_id FROM sm_risk_task_template_products WHERE id=:riskTaskTemplateProductId", nativeQuery=true)
	Long findTemplateId(@Param(value = "riskTaskTemplateProductId") Long riskTaskTemplateProductId);
	
	@Transactional
	void deleteById(Long id);
}
