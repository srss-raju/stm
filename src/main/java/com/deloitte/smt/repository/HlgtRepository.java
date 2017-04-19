package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Hlgt;

public interface HlgtRepository  extends JpaRepository<Hlgt, Long> {

    List<Hlgt> findAllByhlgtNameIn(List<String> hlgts);
    List<Hlgt> findByTopicId(Long topicId);
	List<Hlgt> findBySocId(Long socId);
	
}
