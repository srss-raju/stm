package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicCondition;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface TopicConditionRepository  extends JpaRepository<TopicCondition, Long> {

	List<TopicCondition> findByTopicId(Long topicId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long topicId);

	TopicCondition findByRecordKey(String conditionKey);
	
	List<TopicCondition> findDistinctByRecordKey(String conditionKey);

	List<TopicCondition> findByDetectionId(Long id);
	
	
}
