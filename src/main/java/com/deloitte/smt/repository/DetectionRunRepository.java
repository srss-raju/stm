package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.DetectionRun;

public interface DetectionRunRepository extends JpaRepository<DetectionRun, Long> {

    List<DetectionRun> findByDetectionIdAndMessageIsNullOrderByRunDateDesc(Long detectionId, Sort sort);

}
