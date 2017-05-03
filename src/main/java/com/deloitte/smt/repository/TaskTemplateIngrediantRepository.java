package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TaskTemplateIngrediant;

@Repository
public interface TaskTemplateIngrediantRepository  extends JpaRepository<TaskTemplateIngrediant, Long> {
	TaskTemplateIngrediant findByName(String ingrediantName);
}
