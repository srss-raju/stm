package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.DectectionDataExport;
@Repository
public interface DectectionDataExportRepository extends JpaRepository<DectectionDataExport, Long> {

}
