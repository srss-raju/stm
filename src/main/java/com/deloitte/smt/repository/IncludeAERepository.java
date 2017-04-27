package com.deloitte.smt.repository;

import com.deloitte.smt.entity.IncludeAE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncludeAERepository extends JpaRepository<IncludeAE, Long> {
	List<IncludeAE> findByDetectionId(Long detectionId);
	void deleteAllByDetectionId(Long detectionId);
}
