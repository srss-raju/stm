package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.SignalStatistics;

public interface SignalStatisticsRepository extends JpaRepository<SignalStatistics, Long> {
	List<SignalStatistics> findByRunInstanceId(Long runInstanceId);

	@Query(value = "select stat  from SignalStatistics stat where stat.topic.id in   :topicIds")
	List<SignalStatistics> findStatisticsByTopicsIds(@Param("topicIds") Set<Long> topicIds);

}
