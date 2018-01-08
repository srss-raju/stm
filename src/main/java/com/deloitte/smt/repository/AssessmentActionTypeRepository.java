package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.AssessmentActionType;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Repository
public interface AssessmentActionTypeRepository extends JpaRepository<AssessmentActionType, Long> {
}
