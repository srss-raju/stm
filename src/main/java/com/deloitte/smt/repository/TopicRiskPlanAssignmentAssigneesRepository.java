package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface TopicRiskPlanAssignmentAssigneesRepository extends JpaRepository<TopicRiskPlanAssignmentAssignees, Long> {
	List<TopicRiskPlanAssignmentAssignees> findByRiskId(Long topicId);
	
	@Query("SELECT DISTINCT NEW TopicRiskPlanAssignmentAssignees(o.userKey, o.userGroupKey) FROM TopicRiskPlanAssignmentAssignees o")
	List<TopicRiskPlanAssignmentAssignees> getRiskAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userKey) FROM TopicRiskPlanAssignmentAssignees o WHERE o.userKey IS NOT NULL")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM TopicRiskPlanAssignmentAssignees o WHERE o.userGroupKey IS NOT NULL")
	List<Long> getAssignedGroups();
}
