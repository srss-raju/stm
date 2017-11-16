package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentCondition;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface AssignmentConditionRepository  extends JpaRepository< AssignmentCondition, Long> {

	
	
}
