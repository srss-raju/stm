package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssessmentAssignmentAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface AssessmentAssignmentAssigneesRepository extends JpaRepository<AssessmentAssignmentAssignees, Long> {
	List<AssessmentAssignmentAssignees> findByAssignmentConfigurationId(Long assignmentConfigurationId);
	@Transactional
	Long deleteByAssignmentConfigurationId(Long assignmentConfigurationId);
}
