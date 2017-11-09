package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Hlt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;

public interface HltRepository  extends JpaRepository<Hlt, Long> {
	
	@Query(value="SELECT DISTINCT(o.hltName) FROM Hlt o ")
	List<String> findDistinctHltNames();

    List<Hlt> findAllByHltNameIn(List<String> hlts);
    List<Hlt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.hltName) from Hlt o where o.hltName is not null AND o.topicId IS not null")
    List<String> findDistinctHltNameForSignal();

    @Query(value = "SELECT distinct (o.hltName) from Hlt o where o.hltName is not null AND o.detectionId IS not null")
    List<String> findDistinctHltNameForSignalDetection();

    @Query(value = "SELECT DISTINCT(o.hltName) FROM Hlt o WHERE o.hltName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctHltNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

	List<Hlt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
    //@Query(value="SELECT distinct o.hltName FROM Hlt o WHERE o.hltName LIKE :searchText||'%'")
    List<Hlt> findByHltNameContainingIgnoreCase(String searchText);
}
