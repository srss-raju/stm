package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.QueryBuilder;

public interface QueryBuilderRepository extends JpaRepository<QueryBuilder, Long> {
	
	List<QueryBuilder> findByDetectionId(Long detectionId);
	@Transactional
	Long deleteByDetectionId(Long detectionId);

}
