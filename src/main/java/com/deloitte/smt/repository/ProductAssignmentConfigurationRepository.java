package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.ProductAssignmentConfiguration;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface ProductAssignmentConfigurationRepository  extends JpaRepository< ProductAssignmentConfiguration, Long> {

	List<ProductAssignmentConfiguration> findByAssignmentConfigurationId(Long assignmentConfigurationId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long assignmentConfigurationId);

	ProductAssignmentConfiguration findByRecordKey(String productKey);
	
}
