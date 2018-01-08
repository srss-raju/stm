package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentAssessmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface AssignmentAssessmentAssigneesRepository extends JpaRepository<AssignmentAssessmentAssignees, Long> {
	List<AssignmentAssessmentAssignees> findByAssignmentConfigurationId(Long assignmentConfigurationId);
	@Transactional
	Long deleteByAssignmentConfigurationId(Long assignmentConfigurationId);
}
