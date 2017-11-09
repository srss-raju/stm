package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.Soc;

public interface SocRepository  extends JpaRepository<Soc, Long> {
	
	@Query(value="SELECT DISTINCT(o.socName) FROM Soc o ")
	Set<String> findDistinctSocNames();

	List<Soc> findAllBySocNameIn(List<String> socs);

	List<Soc> findByTopicId(Long topicId);

	List<Soc> findByDetectionId(Long detectionId);

	@Query(value = "SELECT distinct (o.socName) FROM Soc o WHERE o.socName is not null AND o.topicId IS not null")
	List<String> findDistinctSocNameForSignal();

	@Query(value = "SELECT distinct (o.socName) FROM Soc o WHERE o.socName is not null AND o.detectionId IS not null")
	List<String> findDistinctSocNameForSignalDetection();

	@Query(value = "SELECT DISTINCT(o.socName) FROM Soc o WHERE o.socName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
	List<String> findDistinctSocNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

	@Transactional
	Long deleteByDetectionId(Long detectionId);
	
	@Query(value="SELECT distinct o.socName FROM Soc o WHERE o.socName LIKE concat('%',(:socName),'%')")
	Set<String> findBySocNameContainingIgnoreCase(@Param(value = "socName") String searchText);
	

	List<Soc> findBySocName(String socName);
}
