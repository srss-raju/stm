package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Pt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PtRepository  extends JpaRepository<Pt, Long> {

    List<Pt> findAllByPtNameIn(List<String> pts);
    List<Pt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.ptName) from Pt o where o.ptName is not null AND o.topicId IS not null")
    List<String> findDistinctPtNameForSignal();

    @Query(value = "SELECT distinct (o.ptName) from Pt o where o.ptName is not null AND o.detectionId IS not null")
    List<String> findDistinctPtNameForSignalDetection();

	List<Pt> findBySocId(Long socId);
    @Transactional
    Long deleteByDetectionId(Long detectionId);
}
