package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.RiskTaskTemplate;

@Repository
public interface RiskTaskTemplateRepository  extends JpaRepository<RiskTaskTemplate, Long> {
	
	List<RiskTaskTemplate> findAllByOrderByCreatedDateDesc();
	
	List<RiskTaskTemplate> findByIdIn(List<Long> ids);
	
	Long countRiskTaskTemplateByNameIgnoreCase(String name);
	
	@Query("SELECT o.name from RiskTaskTemplate o where o.name not in (select a.name from RiskTaskTemplate a where a.name= :name AND a.id=:id)")
	List<String> findByName(@Param(value = "name") String name,@Param(value = "id") Long id);

	RiskTaskTemplate findByNameIgnoreCase(String name);

}
