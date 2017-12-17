package com.deloitte.smt.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TaskTemplateProducts;

@Repository
public interface TaskTemplateProductsRepository  extends JpaRepository<TaskTemplateProducts, Long> {
	
	@Transactional
	void deleteById(Long id);
}
