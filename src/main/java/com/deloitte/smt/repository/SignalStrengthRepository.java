package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.SignalStrength;

public interface SignalStrengthRepository extends JpaRepository<SignalStrength, Long> {
	List<SignalStrength> findByTopicId(Long topicId);
}
