package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.deloitte.smt.entity.Llt;

public interface LltRepository extends JpaRepository<Llt, Long> {
	
	//@Query(value="SELECT distinct o.lltName FROM Llt o WHERE o.lltName LIKE :searchText||'%'")
    List<Llt> findByLltNameContainingIgnoreCase(String searchText);
    
    @Query(value="SELECT DISTINCT(o.lltName) FROM Llt o ")
	List<String> findDistinctLltNames();
}
