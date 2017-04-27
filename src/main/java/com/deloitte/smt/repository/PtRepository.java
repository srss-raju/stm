package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Pt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PtRepository  extends JpaRepository<Pt, Long> {

    List<Pt> findAllByPtNameIn(List<String> pts);
    List<Pt> findByDetectionId(Long topicId);

    @Query(value = "SELECT distinct (o.ptName) from Pt o where o.ptName is not null")
    List<String> findDistinctPtName();
	List<Pt> findBySocId(Long socId);
    void deleteAllByDetectionId(Long detectionId);
}
