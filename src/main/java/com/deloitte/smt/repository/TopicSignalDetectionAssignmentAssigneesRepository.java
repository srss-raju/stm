package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicSignalDetectionAssignmentAssignees;

/**
 * Created by Rajesh on 01-08-2017.
 */
@Repository
public interface TopicSignalDetectionAssignmentAssigneesRepository extends JpaRepository<TopicSignalDetectionAssignmentAssignees, Long> {
	
	List<TopicSignalDetectionAssignmentAssignees> findByDetectionId(Long detectionId);

}

