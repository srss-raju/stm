package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deloitte.smt.entity.SignalDetection;

public interface SignalDetectionRepository extends JpaRepository<SignalDetection, Long> {
	
	Long countByNameIgnoreCase(String detectionName);
	
	@Query("SELECT DISTINCT(o.owner) FROM SignalDetection o WHERE o.owner IS NOT NULL")
	List<String> findDistinctOwnerOnDetection();
	
	
	@Query("select pas.recordKey from TopicProductAssignmentConfiguration pas, SignalDetection d where d.id = pas.detectionId")
	List<String> getProductFilterValues();
	
	@Query("select pas.recordKey from TopicSocAssignmentConfiguration pas, SignalDetection d where d.id = pas.detectionId")
	List<String> getConditionFilterValues();
	
		
	
	
}
