package com.deloitte.smt.repository;

import com.deloitte.smt.entity.DenominatorForPoisson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenominatorForPoissonRepository extends JpaRepository<DenominatorForPoisson, Long> {
	List<DenominatorForPoisson> findByDetectionId(Long detectionId);
	void deleteAllByDetectionId(Long detectionId);
}
