package com.deloitte.smt.repository;

import com.deloitte.smt.entity.RiskPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Repository
public interface RiskPlanRepository extends JpaRepository<RiskPlan, Long> {
}
