package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.TaskTemplate;

@Repository
public interface TaskTemplateRepository  extends JpaRepository<TaskTemplate, Long> {
	
	List<TaskTemplate> findAllByOrderByCreatedDateDesc();
	
	List<TaskTemplate> findByIdIn(List<Long> ids);
	
	Long countTaskTemplateByNameIgnoreCase(String name);
	
	@Query("SELECT o.name from TaskTemplate o where o.name not in (select a.name from TaskTemplate a where a.name= :name AND a.id=:id)")
	List<String> findByName(@Param(value = "name") String name,@Param(value = "id") Long id);

	TaskTemplate findByNameIgnoreCase(String name);

	List<Task> findAllByTemplateId(Long templateId);
}
