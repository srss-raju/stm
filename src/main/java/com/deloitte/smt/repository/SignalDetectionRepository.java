package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.SignalDetection;

public interface SignalDetectionRepository extends JpaRepository<SignalDetection, Long> {
	
	Long countByNameIgnoreCase(String detectionName);
	
}
