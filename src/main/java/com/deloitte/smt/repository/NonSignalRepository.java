package com.deloitte.smt.repository;

import com.deloitte.smt.entity.NonSignal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cavula
 */
@Repository
public interface NonSignalRepository extends JpaRepository<NonSignal, Long> {

	List<NonSignal> findNonSignalByRunInstanceIdOrderByCreatedDateAsc(Long runInstanceId);
 
}
