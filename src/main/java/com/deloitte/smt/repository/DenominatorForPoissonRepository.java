package com.deloitte.smt.repository;

import com.deloitte.smt.entity.DenominatorForPoisson;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface DenominatorForPoissonRepository extends JpaRepository<DenominatorForPoisson, Long> {
	List<DenominatorForPoisson> findByDetectionId(Long detectionId);
	@Transactional
	Long deleteByDetectionId(Long detectionId);
}
