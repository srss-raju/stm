package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentConditionValues;

/**
 * Created by Rajesh on 17-11-2017.
 */
@Repository
public interface AssignmentConditionValuesRepository  extends JpaRepository<AssignmentConditionValues, Long> {

	List<AssignmentConditionValues> findBySocAssignmentConfigurationId(Long socAssignmentConfigurationId);
	
	@Transactional
	Long deleteBySocAssignmentConfigurationId(Long socAssignmentConfigurationId);
	
	@Query("select distinct p.categoryCode, p.categoryDesc,category FROM AssignmentConditionValues p WHERE p.categoryCode IN ?1")
	List<Object[]> findDistinctByCategoryCodeIn(Set<String> recValues);
}
