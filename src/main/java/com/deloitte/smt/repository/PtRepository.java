package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Pt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface PtRepository  extends JpaRepository<Pt, Long> {

    List<Pt> findAllByPtNameIn(List<String> pts);
    List<Pt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.ptName) from Pt o ,TopicSignalValidationAssignmentAssignees ta,Topic t WHERE ta.topicId=o.topicId AND ta.userGroupKey =:userGroupKey OR t.owner=:owner AND o.ptName is not null AND o.topicId IS not null")
    List<String> findDistinctPtNameForSignal(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value = "SELECT distinct (o.ptName) from Pt o , TopicSignalDetectionAssignmentAssignees ta,SignalDetection t WHERE o.detectionId=ta.detectionId AND ta.userGroupKey =:userGroupKey OR t.owner=:owner AND o.detectionId IS not null")
    List<String> findDistinctPtNameForSignalDetection(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value = "SELECT DISTINCT(o.ptName) FROM Pt o WHERE o.ptName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctPtNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

	List<Pt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
    
    List<Pt> findBySmqId(Long smqId);
}
