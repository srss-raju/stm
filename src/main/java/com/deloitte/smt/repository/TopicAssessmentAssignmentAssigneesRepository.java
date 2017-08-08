package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicAssessmentAssignmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface TopicAssessmentAssignmentAssigneesRepository extends JpaRepository<TopicAssessmentAssignmentAssignees, Long> {
	List<TopicAssessmentAssignmentAssignees> findByAssessmentId(Long topicId);
}
