package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.DetectionAssignees;

/**
 * Created by Rajesh on 01-08-2017.
 */
@Repository
public interface DetectionAssigneesRepository extends JpaRepository<DetectionAssignees, Long> {
	
	@Query("SELECT DISTINCT NEW DetectionAssignees(o.userGroupKey, o.userKey) FROM DetectionAssignees o")
	List<DetectionAssignees> getDetectionAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userKey) FROM DetectionAssignees o WHERE o.userKey IS NOT NULL")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM DetectionAssignees o WHERE o.userGroupKey IS NOT NULL")
	List<Long> getAssignedGroups();

}

