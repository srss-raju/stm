package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Hlt;

public interface HltRepository  extends JpaRepository<Hlt, Long> {

    List<Hlt> findAllByhltNameIn(List<String> hlts);
    List<Hlt> findByTopicId(Long topicId);
	List<Hlt> findBySocId(Long socId);
	
}
