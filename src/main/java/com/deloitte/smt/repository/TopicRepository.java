package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.Topic;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query(value = "SELECT DISTINCT (o.name) FROM Topic o WHERE o.name IS NOT NULL ")
    List<String> findDistinctSignalName();

    @Query(value = "SELECT DISTINCT (o.signalConfirmation) FROM Topic o WHERE o.signalConfirmation IS NOT NULL ")
    List<String> findDistinctSignalConfirmationNames();
	List<Topic> findTopicByRunInstanceIdOrderByCreatedDateAsc(Long runInstanceId);
	
	Long countByNameIgnoreCase(String topicName);
	
	@Query("SELECT DISTINCT(o.sourceName) FROM Topic o WHERE o.sourceName IS NOT NULL")
	List<String> getSourceNames();
	
	@Query("SELECT DISTINCT(o.owner) FROM Topic o WHERE o.owner IS NOT NULL ")
	List<String> findDistinctOwnerNames();
	@Query("select pas.recordKey from TopicProduct pas, Topic t where t.id = pas.topicId")
	List<String> getProductFilterValues();
	@Query("select pas.recordKey from TopicCondition pas, Topic t where t.id = pas.topicId")
	List<String> getConditionFilterValues();
	
	@Query("select pas.recordKey from TopicProduct pas where pas.topicId in :ids")
	List<String> getListOfProductRecordKeys(@Param("ids") Set<Long> riskAssSingnalIds);
	
	@Query("select pas.recordKey from TopicCondition pas where pas.topicId in :ids")
	List<String> getListOfConditionRecordKeys(@Param("ids") Set<Long> riskAssSingnalIds);
	
}

