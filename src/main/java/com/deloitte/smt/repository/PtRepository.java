package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Pt;

public interface PtRepository  extends JpaRepository<Pt, Long> {

    List<Pt> findAllByPtNameIn(List<String> pts);
    List<Pt> findByTopicId(Long topicId);
	List<Pt> findBySocId(Long socId);
	
}
