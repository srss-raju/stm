package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface TopicRiskPlanAssignmentAssigneesRepository extends JpaRepository<TopicRiskPlanAssignmentAssignees, Long> {
	List<TopicRiskPlanAssignmentAssignees> findByRiskId(Long topicId);
}
