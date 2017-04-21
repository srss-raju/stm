package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.IncludeAE;

public interface IncludeAERepository extends JpaRepository<IncludeAE, Long> {
	List<IncludeAE> findByDetectionId(Long detectionId);
}
