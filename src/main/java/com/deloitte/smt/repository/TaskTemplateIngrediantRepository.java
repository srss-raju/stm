package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TaskTemplateIngrediant;

@Repository
public interface TaskTemplateIngrediantRepository  extends JpaRepository<TaskTemplateIngrediant, Long> {
	
	TaskTemplateIngrediant findByName(String ingrediantName);//remove this
	
	@Transactional
	@Query(value="select distinct taskTemplateId from TaskTemplateIngrediant where name = ?1")
	List<Long> findByIngrediantName(String name);
	
	List<TaskTemplateIngrediant> findByTaskTemplateId(Long templateId);
	
	@Transactional
	void deleteById(Long id);
}
