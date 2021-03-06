package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.License;

public interface LicenseRepository extends JpaRepository<License, Long> {

    List<License> findAllByLicenseNameIn(List<String> licenses);
    
    List<License> findByTopicId(Long topicId);
    List<License> findByDetectionId(Long detectionId);
    
    List<License> findByIngredientId(Long ingredientId);

    @Query(value = "SELECT DISTINCT (o.licenseName) FROM License o WHERE o.licenseName IS NOT NULL AND o.topicId IS not null")
    List<String> findDistinctLicenseNameForSignal();

    @Query(value = "SELECT DISTINCT (o.licenseName) FROM License o WHERE o.licenseName IS NOT NULL AND o.detectionId IS not null")
    List<String> findDistinctLicenseNameForSignalDetection();

    @Query(value = "SELECT DISTINCT(o.licenseName) FROM License o WHERE o.licenseName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctLicenseNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

    @Transactional
    Long deleteByDetectionId(Long detectionId);
}
