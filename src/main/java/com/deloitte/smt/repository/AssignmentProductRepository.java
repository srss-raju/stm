package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentProduct;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface AssignmentProductRepository  extends JpaRepository< AssignmentProduct, Long> {

	List<AssignmentProduct> findByAssignmentConfigurationId(Long assignmentConfigurationId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long assignmentConfigurationId);

	AssignmentProduct findByRecordKey(String productKey);
	
	List<AssignmentProduct> findDistinctByRecordKey(String productKey);
	
	@Query("select distinct p.categoryCode, p.categoryDesc,category FROM AssignmentProduct p WHERE p.categoryCode IN ?1")
	List<Object[]> findDistinctByCategoryCodeIn(Set<String> recValues);
}
