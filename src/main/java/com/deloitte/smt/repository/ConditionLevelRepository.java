package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deloitte.smt.entity.ConditionLevels;

public interface ConditionLevelRepository extends JpaRepository<ConditionLevels, Long> {
	
	@Query(value="select distinct(o.versions) from ConditionLevels o where o.versions is not null")
	List<String> findByVersions();
	
	List<ConditionLevels> findAllByOrderByIdAsc();
	
}
