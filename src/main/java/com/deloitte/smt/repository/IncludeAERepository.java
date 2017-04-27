package com.deloitte.smt.repository;

import com.deloitte.smt.entity.IncludeAE;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface IncludeAERepository extends JpaRepository<IncludeAE, Long> {
	List<IncludeAE> findByDetectionId(Long detectionId);
	@Transactional
	Long deleteByDetectionId(Long detectionId);
}
