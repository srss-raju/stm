package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.SignalAction;

public interface AssessmentActionRepository extends JpaRepository<SignalAction, Long> {

	List<SignalAction> findAllByAssessmentId(String assessmentId);
	
}
