package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.RiskPlanAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface RiskPlanAssigneesRepository extends JpaRepository<RiskPlanAssignees, Long> {
	List<RiskPlanAssignees> findByRiskId(Long topicId);
	
	@Query("SELECT DISTINCT NEW RiskPlanAssignees(o.userGroupKey, o.userKey) FROM RiskPlanAssignees o")
	List<RiskPlanAssignees> getRiskAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userKey) FROM RiskPlanAssignees o WHERE o.userKey IS NOT NULL")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM RiskPlanAssignees o WHERE o.userGroupKey IS NOT NULL")
	List<Long> getAssignedGroups();
	
	@Transactional
	Long deleteByRiskId(Long riskId);
}
