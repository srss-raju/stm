package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.SignalAudit;

public interface SignalAuditRepository extends JpaRepository<SignalAudit, Long> {
	
}
