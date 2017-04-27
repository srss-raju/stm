package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Hlt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface HltRepository  extends JpaRepository<Hlt, Long> {

    List<Hlt> findAllByHltNameIn(List<String> hlts);
    List<Hlt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.hltName) from Hlt o where o.hltName is not null AND o.topicId IS not null")
    List<String> findDistinctHltNameForSignal();

    @Query(value = "SELECT distinct (o.hltName) from Hlt o where o.hltName is not null AND o.detectionId IS not null")
    List<String> findDistinctHltNameForSignalDetection();

	List<Hlt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
}
