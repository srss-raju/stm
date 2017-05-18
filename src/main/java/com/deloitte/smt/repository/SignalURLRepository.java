package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.SignalURL;

public interface SignalURLRepository extends JpaRepository<SignalURL, Long> {
	List<SignalURL> findByTopicId(Long topicId);
}
