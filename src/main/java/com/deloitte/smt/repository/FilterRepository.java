package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.Filters;
@Repository
public interface FilterRepository extends JpaRepository<Filters, Long> {
	@Query( "select o from Filters o where type in :types and visible=true order by id asc" )
	List<Filters> findByFilterTypes(@Param("types") List<String> types);

}
