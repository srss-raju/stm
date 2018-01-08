package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssignmentSignalAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface AssignmentSignalAssigneesRepository extends JpaRepository<AssignmentSignalAssignees, Long> {
	List<AssignmentSignalAssignees> findByAssignmentConfigurationId(Long assignmentConfigurationId);
	@Transactional
	Long deleteByAssignmentConfigurationId(Long assignmentConfigurationId);
}
