package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicAssessmentAssignmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface TopicAssessmentAssignmentAssigneesRepository extends JpaRepository<TopicAssessmentAssignmentAssignees, Long> {
	List<TopicAssessmentAssignmentAssignees> findByAssessmentId(Long topicId);
	
	@Query("SELECT DISTINCT NEW TopicAssessmentAssignmentAssignees(o.userKey, o.userGroupKey) FROM TopicAssessmentAssignmentAssignees o")
	List<TopicAssessmentAssignmentAssignees> getAssessmentAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userKey) FROM TopicAssessmentAssignmentAssignees o WHERE o.userKey IS NOT NULL")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM TopicAssessmentAssignmentAssignees o WHERE o.userGroupKey IS NOT NULL")
	List<Long> getAssignedGroups();
}
