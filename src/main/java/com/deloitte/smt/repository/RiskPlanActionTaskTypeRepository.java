package com.deloitte.smt.repository;

import com.deloitte.smt.entity.RiskPlanActionTaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Repository
public interface RiskPlanActionTaskTypeRepository extends JpaRepository<RiskPlanActionTaskType, Long> {
}
