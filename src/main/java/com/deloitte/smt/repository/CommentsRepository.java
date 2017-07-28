package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
	List<Comments> findByAssessmentId(Long assessmentId);
	List<Comments> findByRiskPlanId(Long riskPlanId);
	List<Comments> findByTopicId(Long topicId);
}
