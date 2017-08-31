package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Hlt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;

public interface HltRepository  extends JpaRepository<Hlt, Long> {

    List<Hlt> findAllByHltNameIn(List<String> hlts);
    List<Hlt> findByDetectionId(Long topicId);

    @Query(value = "SELECT DISTINCT(o.hltName) from Hlt o,TopicSignalValidationAssignmentAssignees ta,Topic t WHERE ta.topicId=o.topicId AND ta.userGroupKey =:userGroupKey OR t.owner=:owner AND o.hltName is not null AND o.topicId IS not null")
    List<String> findDistinctHltNameForSignal(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    //@Query(value = "SELECT distinct (o.hltName) from Hlt o where o.hltName is not null AND o.detectionId IS not null")
    //@Query(value = "SELECT distinct (o.hltName) from Hlt o left outer join SignalDetection sd on sd.id=o.detectionId and left outer join Soc soc on soc.id= o.socId")
    @Query(value = "SELECT DISTINCT(o.hltName) from Hlt o, TopicSignalDetectionAssignmentAssignees ta,SignalDetection t WHERE o.detectionId=ta.detectionId AND ta.userGroupKey =:userGroupKey OR t.owner=:owner AND o.hltName is not null AND o.detectionId IS not null")
    List<String> findDistinctHltNameForSignalDetection(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value = "SELECT DISTINCT(o.hltName) FROM Hlt o WHERE o.hltName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctHltNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

	List<Hlt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
}
