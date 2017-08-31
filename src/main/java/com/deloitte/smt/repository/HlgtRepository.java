package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Hlgt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface HlgtRepository  extends JpaRepository<Hlgt, Long> {

    List<Hlgt> findAllByHlgtNameIn(List<String> hlgts);

    @Query(value = "SELECT distinct (o.hlgtName) from Hlgt o ,TopicSignalValidationAssignmentAssignees ta,Topic t WHERE ta.topicId=o.topicId AND ta.userGroupKey =:userGroupKey OR t.owner =:owner AND o.hlgtName is not null AND o.topicId IS not null")
    List<String> findDistinctHlgtNameForSignal(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value="SELECT distinct (o.hlgtName) from Hlgt o , TopicSignalDetectionAssignmentAssignees ta,SignalDetection t WHERE o.detectionId=ta.detectionId AND ta.userGroupKey =:userGroupKey OR t.owner=:owner AND o.detectionId IS not null")
    List<String> findDistinctHlgtNameForSignalDetection(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value = "SELECT DISTINCT(o.hlgtName) FROM Hlgt o WHERE o.hlgtName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctHlgtNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

    List<Hlgt> findByDetectionId(Long detectionId);
	List<Hlgt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
}
