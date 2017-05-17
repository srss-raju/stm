package com.deloitte.smt.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.SignalDetection;

public interface SignalDetectionRepository extends JpaRepository<SignalDetection, Long> {
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE SignalDetection o SET o.lastRunDate=:lastRunDate WHERE id= :id")
	void updateLastRunDate(@Param("lastRunDate") Date lastRunDate, @Param("id") Long id);

}
