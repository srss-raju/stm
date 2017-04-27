package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Soc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SocRepository  extends JpaRepository<Soc, Long> {

	List<Soc> findAllBySocNameIn(List<String> socs);

	List<Soc> findByTopicId(Long topicId);

	List<Soc> findByDetectionId(Long detectionId);

	@Query(value = "SELECT distinct (o.socName) FROM Soc o WHERE o.socName is not null AND o.topicId IS not null")
	List<String> findDistinctSocNameForSignal();

	@Query(value = "SELECT distinct (o.socName) FROM Soc o WHERE o.socName is not null AND o.detectionId IS not null")
	List<String> findDistinctSocNameForSignalDetection();

	@Transactional
	Long deleteByDetectionId(Long detectionId);
}
