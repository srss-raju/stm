package com.deloitte.smt.repository;

import com.deloitte.smt.entity.DenominatorForPoission;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface DenominatorForPoissionRepository extends JpaRepository<DenominatorForPoission, Long> {
	List<DenominatorForPoission> findByDetectionId(Long detectionId);
	@Transactional
	Long deleteByDetectionId(Long detectionId);
	
	@Transactional
	void deleteByDetectionIdIsNull();
	
	List<DenominatorForPoission> findByDetectionIdIsNullOrderByName();
}
