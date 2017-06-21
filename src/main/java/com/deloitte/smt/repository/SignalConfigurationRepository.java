package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.SignalConfiguration;


public interface SignalConfigurationRepository extends JpaRepository<SignalConfiguration, Long> {
	
	@Query(value = "SELECT c FROM SignalConfiguration c WHERE c.configName = :configName")
	SignalConfiguration  findByConfigName(@Param("configName") String configName);
	
}
