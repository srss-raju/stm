package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.Hlgt;

public interface HlgtRepository  extends JpaRepository<Hlgt, Long> {
	
	@Query(value="SELECT DISTINCT(o.hlgtName) FROM Hlgt o ")
	List<String> findDistinctHlgtNames();

    List<Hlgt> findAllByHlgtNameIn(List<String> hlgts);

    @Query(value = "SELECT distinct (o.hlgtName) from Hlgt o where o.hlgtName is not null AND o.topicId IS not null")
    List<String> findDistinctHlgtNameForSignal();

    @Query(value = "SELECT distinct (o.hlgtName) from Hlgt o where o.hlgtName is not null AND o.detectionId IS not null")
    List<String> findDistinctHlgtNameForSignalDetection();

    @Query(value = "SELECT DISTINCT(o.hlgtName) FROM Hlgt o WHERE o.hlgtName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctHlgtNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

    List<Hlgt> findByDetectionId(Long detectionId);
	List<Hlgt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
    
    @Query(value="SELECT distinct UPPER(o.hlgtName) FROM Hlgt o WHERE o.hlgtName LIKE concat('%',LOWER(:hlgtName),'%') ")
    Set<String> findByHlgtNameContainingIgnoreCase(@Param(value = "hlgtName") String searchText);
}
