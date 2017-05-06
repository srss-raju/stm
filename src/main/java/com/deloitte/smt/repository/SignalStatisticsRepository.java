package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.SignalStatistics;

public interface SignalStatisticsRepository extends JpaRepository<SignalStatistics, Long> {
	List<SignalStatistics> findByRunInstanceId(Long runInstanceId);
}
