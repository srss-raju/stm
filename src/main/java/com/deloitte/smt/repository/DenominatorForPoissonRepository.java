package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.DenominatorForPoisson;

public interface DenominatorForPoissonRepository extends JpaRepository<DenominatorForPoisson, Long> {
	List<DenominatorForPoisson> findByDetectionId(Long detectionId);
}
