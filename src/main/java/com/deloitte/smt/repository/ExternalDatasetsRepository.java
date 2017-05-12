package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.ExternalDatasets;

public interface ExternalDatasetsRepository extends JpaRepository<ExternalDatasets, Long> {
	
}
