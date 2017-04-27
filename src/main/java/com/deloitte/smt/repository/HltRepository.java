package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Hlt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HltRepository  extends JpaRepository<Hlt, Long> {

    List<Hlt> findAllByHltNameIn(List<String> hlts);
    List<Hlt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.hltName) from Hlt o where o.hltName is not null")
    List<String> findDistinctHltName();
	List<Hlt> findBySocId(Long socId);
    void deleteAllByDetectionId(Long detectionId);
}
