package com.deloitte.smt.repository;

import com.deloitte.smt.entity.DetectionRun;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetectionRunRepository extends JpaRepository<DetectionRun, Long> {

    List<DetectionRun> findByDetectionId(Long detectionId, Sort sort);

}
