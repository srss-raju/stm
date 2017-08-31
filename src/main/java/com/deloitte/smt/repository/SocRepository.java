package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Soc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface SocRepository  extends JpaRepository<Soc, Long> {

	List<Soc> findAllBySocNameIn(List<String> socs);

	List<Soc> findByTopicId(Long topicId);

	List<Soc> findByDetectionId(Long detectionId);

	@Query(value = "SELECT distinct (o.socName) FROM Soc o ,TopicSignalValidationAssignmentAssignees ta,Topic t WHERE ta.topicId=o.topicId AND ta.userGroupKey =:userGroupKey OR t.owner= :owner")
	List<String> findDistinctSocNameForSignal(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

	@Query(value="SELECT DISTINCT(o.socName) FROM Soc o ,TopicSignalDetectionAssignmentAssignees ta,SignalDetection t WHERE o.detectionId=ta.detectionId AND ta.userGroupKey =:userGroupKey OR t.owner= :owner AND o.socName is not null AND o.detectionId IS not null")
	List<String> findDistinctSocNameForSignalDetection(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

	@Query(value = "SELECT DISTINCT(o.socName) FROM Soc o WHERE o.socName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
	List<String> findDistinctSocNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

	@Transactional
	Long deleteByDetectionId(Long detectionId);
}
