package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.License;

public interface LicenseRepository extends JpaRepository<License, Long> {

}
