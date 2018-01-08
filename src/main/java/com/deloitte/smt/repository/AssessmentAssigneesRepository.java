package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssessmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface AssessmentAssigneesRepository extends JpaRepository<AssessmentAssignees, Long> {
	List<AssessmentAssignees> findByAssessmentId(Long topicId);
	
	@Query("SELECT DISTINCT NEW AssessmentAssignees(o.userGroupKey, o.userKey) FROM AssessmentAssignees o")
	List<AssessmentAssignees> getAssessmentAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userKey) FROM AssessmentAssignees o WHERE o.userKey IS NOT NULL")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM AssessmentAssignees o WHERE o.userGroupKey IS NOT NULL")
	List<Long> getAssignedGroups();
}
