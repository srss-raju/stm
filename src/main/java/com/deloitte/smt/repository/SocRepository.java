package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Soc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SocRepository  extends JpaRepository<Soc, Long> {

	List<Soc> findAllBySocNameIn(List<String> socs);

	List<Soc> findByTopicId(Long topicId);

	List<Soc> findByDetectionId(Long detectionId);

	@Query(value = "SELECT distinct (o.socName) FROM Soc o WHERE o.socName is not null")
	List<String> findDistinctSocName();

	void deleteAllByDetectionId(Long detectionId);
}
