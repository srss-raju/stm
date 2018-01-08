package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.SignalConfidence;


public interface SignalConfigurationRepository extends JpaRepository<SignalConfidence, Long> {
	
	@Query(value = "SELECT c FROM SignalConfiguration c WHERE c.configName = :configName")
	SignalConfidence  findByConfigName(@Param("configName") String configName);
	
}
