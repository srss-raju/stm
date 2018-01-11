package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.DenominatorForPoission;

public interface DenominatorForPoissionRepository extends JpaRepository<DenominatorForPoission, Long> {
	
	List<DenominatorForPoission> findByDetectionIdIsNullOrderByName();
}
