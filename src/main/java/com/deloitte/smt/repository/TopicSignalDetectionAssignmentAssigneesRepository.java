package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicSignalDetectionAssignmentAssignees;

/**
 * Created by Rajesh on 01-08-2017.
 */
@Repository
public interface TopicSignalDetectionAssignmentAssigneesRepository extends JpaRepository<TopicSignalDetectionAssignmentAssignees, Long> {
	
	List<TopicSignalDetectionAssignmentAssignees> findByDetectionId(Long detectionId);
	
	//@Query("SELECT DISTINCT(o.userKey) FROM TopicSignalDetectionAssignmentAssignees o WHERE o.userKey IS NOT NULL")
	@Query("SELECT DISTINCT(o.userKey) FROM TopicSignalDetectionAssignmentAssignees o , SignalDetection sd WHERE sd.id=o.detectionId AND o.userKey IS NOT NULL ")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM TopicSignalDetectionAssignmentAssignees o, SignalDetection sd WHERE sd.id=o.detectionId AND o.userGroupKey IS NOT NULL ")
	List<Long> getAssignedGroups();

}

