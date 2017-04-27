package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Hlgt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HlgtRepository  extends JpaRepository<Hlgt, Long> {

    List<Hlgt> findAllByHlgtNameIn(List<String> hlgts);

    @Query(value = "SELECT distinct (o.hlgtName) from Hlgt o where o.hlgtName is not null")
    List<String> findDistinctHlgtName();
    List<Hlgt> findByDetectionId(Long detectionId);
	List<Hlgt> findBySocId(Long socId);
    void deleteAllByDetectionId(Long detectionId);
}
