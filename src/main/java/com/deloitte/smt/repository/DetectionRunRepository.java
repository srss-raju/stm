package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.DetectionRun;

public interface DetectionRunRepository extends JpaRepository<DetectionRun, Long> {

}
