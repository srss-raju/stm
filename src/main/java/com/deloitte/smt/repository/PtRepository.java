package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Pt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;

public interface PtRepository  extends JpaRepository<Pt, Long> {
	
	@Query(value="SELECT DISTINCT(o.ptName) FROM Pt o ")
	List<String> findDistinctPtNames();

    List<Pt> findAllByPtNameIn(List<String> pts);
    List<Pt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.ptName) from Pt o where o.ptName is not null AND o.topicId IS not null")
    List<String> findDistinctPtNameForSignal();

    @Query(value = "SELECT distinct (o.ptName) from Pt o where o.ptName is not null AND o.detectionId IS not null")
    List<String> findDistinctPtNameForSignalDetection();

    @Query(value = "SELECT DISTINCT(o.ptName) FROM Pt o WHERE o.ptName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctPtNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

	List<Pt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
    
    List<Pt> findBySmqId(Long smqId);
    
    //@Query(value="SELECT distinct o.ptName FROM Pt o WHERE o.ptName LIKE :searchText||'%'")
    List<Pt> findByPtNameContainingIgnoreCase(String searchText);
}
