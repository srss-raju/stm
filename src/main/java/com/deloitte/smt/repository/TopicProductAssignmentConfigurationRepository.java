package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;

/**
 * Created by Rajesh on 07-12-2017.
 */
@Repository
public interface TopicProductAssignmentConfigurationRepository  extends JpaRepository<TopicProductAssignmentConfiguration, Long> {

	List<TopicProductAssignmentConfiguration> findByTopicId(Long topicId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long topicId);

	TopicProductAssignmentConfiguration findByRecordKey(String conditionKey);
	
	List<TopicProductAssignmentConfiguration> findDistinctByRecordKey(String conditionKey);
	
	
}
