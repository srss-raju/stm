package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TaskTemplateProducts;

@Repository
public interface TaskTemplateProductsRepository  extends JpaRepository<TaskTemplateProducts, Long> {
	
	TaskTemplateProducts findByRecordKey(String recordKey);
	
	@Query(value = "SELECT DISTINCT task_template_id FROM sm_task_template_products WHERE id=:templateProductId", nativeQuery=true)
	Long findTemplateId(@Param(value = "templateProductId") Long templateProductId);
}
