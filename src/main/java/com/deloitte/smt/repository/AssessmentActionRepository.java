package com.deloitte.smt.repository;

import com.deloitte.smt.entity.SignalAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentActionRepository extends JpaRepository<SignalAction, Long> {

	List<SignalAction> findAllByAssessmentIdAndActionStatus(String assessmentId, String actionStatus);
	List<SignalAction> findAllByAssessmentId(String assessmentId);
	List<SignalAction> findAllByTemplateId(Long templateId);
	
	Long countByActionNameIgnoreCaseAndAssessmentId(String actionName,String assessmentId);
	SignalAction findByActionNameIgnoreCaseAndAssessmentId(String actionName,String assessmentId);
	SignalAction findByActionNameIgnoreCaseAndTemplateId(String actionName,Long templateId);
}
