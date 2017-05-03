package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TaskTemplate;

@Repository
public interface TaskTemplateRepository  extends JpaRepository<TaskTemplate, Long> {
	
	List<TaskTemplate> findAllByOrderByCreatedDateDesc();
	
	List<TaskTemplate> findByIdIn(List<Long> ids);

}
