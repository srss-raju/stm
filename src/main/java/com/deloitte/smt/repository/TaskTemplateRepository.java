package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.TaskTemplate;

@Repository
public interface TaskTemplateRepository  extends JpaRepository<TaskTemplate, Long> {
	
	/*TaskTemplate findByIngrediantName(String ingrediantName);
	
	@Transactional
	void deleteByName(String name);
	
	@Transactional
	@Query(value="delete from sm_task_template where ingrediant_name = ?1 and name = ?2")
	void deleteByIngrediantNames(String name, String templateName);*/

}
