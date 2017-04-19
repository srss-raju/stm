package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.Soc;

public interface SocRepository  extends JpaRepository<Soc, Long> {

	List<Ingredient> findAllBySocNameIn(List<String> socs);
    
	List<Soc> findByTopicId(Long topicId);
	
}
