package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.License;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {

    List<License> findAllByLicenseNameIn(List<String> licenses);
    
    List<License> findByTopicId(Long topicId);
    
}
