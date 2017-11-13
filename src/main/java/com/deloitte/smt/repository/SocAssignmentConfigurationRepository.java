package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.SocAssignmentConfiguration;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface SocAssignmentConfigurationRepository  extends JpaRepository< SocAssignmentConfiguration, Long> {

	List<SocAssignmentConfiguration> findByAssignmentConfigurationId(Long assignmentConfigurationId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long assignmentConfigurationId);
	
}
