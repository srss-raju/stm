package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.Llt;

public interface LltRepository extends JpaRepository<Llt, Long> {
	
	@Query(value="SELECT distinct o.lltName FROM Llt o WHERE o.lltName LIKE concat('%',(:lltName),'%')")
    Set<String> findByLltNameContainingIgnoreCase(@Param(value = "lltName") String searchText);
    
    @Query(value="SELECT DISTINCT(o.lltName) FROM Llt o ")
	List<String> findDistinctLltNames();
}
