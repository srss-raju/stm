package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Smq;

public interface SmqRepository  extends JpaRepository<Smq, Long> {

	List<Smq> findAllBySmqNameIn(List<String> socs);

	List<Smq> findByDetectionId(Long detectionId);

	@Transactional
	Long deleteByDetectionId(Long detectionId);
}
