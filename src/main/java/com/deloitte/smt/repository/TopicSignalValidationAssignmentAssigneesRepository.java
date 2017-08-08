package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicSignalValidationAssignmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface TopicSignalValidationAssignmentAssigneesRepository extends JpaRepository<TopicSignalValidationAssignmentAssignees, Long> {
	
	List<TopicSignalValidationAssignmentAssignees> findByTopicId(Long topicId);

}
