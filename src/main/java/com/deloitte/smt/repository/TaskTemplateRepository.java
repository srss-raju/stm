package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.TaskTemplate;

@Repository
public interface TaskTemplateRepository  extends JpaRepository<TaskTemplate, Long> {
	
	TaskTemplate findByIngrediantName(String ingrediantName);
	
	@Transactional
	void deleteByName(String name);

}
