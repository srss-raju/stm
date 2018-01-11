package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentProductValues;

/**
 * Created by Rajesh on 17-11-2017.
 */
@Repository
public interface AssignmentProductValuesRepository  extends JpaRepository<AssignmentProductValues, Long> {

	@Query("select distinct p.categoryCode, p.categoryDesc,category FROM AssignmentProductValues p WHERE p.categoryCode IN ?1")
	List<Object[]> findDistinctByCategoryCodeIn(Set<String> recValues);
	
}
