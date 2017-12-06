package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;

/**
 * Created by Rajesh on 10-11-2017.
 */
@Repository
public interface TopicSocAssignmentConfigurationRepository  extends JpaRepository<TopicSocAssignmentConfiguration, Long> {

	List<TopicSocAssignmentConfiguration> findByTopicId(Long topicId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long topicId);

	TopicSocAssignmentConfiguration findByRecordKey(String conditionKey);
	
	List<TopicSocAssignmentConfiguration> findDistinctByRecordKey(String conditionKey);
	
	
}
