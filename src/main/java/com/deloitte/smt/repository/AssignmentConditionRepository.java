package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentCondition;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface AssignmentConditionRepository  extends JpaRepository<AssignmentCondition, Long> {

	AssignmentCondition findByRecordKey(String conditionKey);
	
	List<AssignmentCondition> findDistinctByRecordKey(String conditionKey);
	
	@Query("select distinct p.categoryCode, p.categoryDesc,category FROM AssignmentCondition p WHERE p.categoryCode IN ?1")
	List<Object[]> findDistinctByCategoryCodeIn(Set<String> recValues);

	
}
