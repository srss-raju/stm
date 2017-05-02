package com.deloitte.smt.repository;

import com.deloitte.smt.entity.FinalDispositions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Repository
public interface FinalDispositionRepository extends JpaRepository<FinalDispositions, Long> {
}
