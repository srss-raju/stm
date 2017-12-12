package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.FiltersStatus;
@Repository
public interface FilterStatusRepository extends JpaRepository<FiltersStatus, Long> {
	@Query( "select o from FiltersStatus o where visible=true order by id asc" )
	List<FiltersStatus> findByFiltersStatus();

}
