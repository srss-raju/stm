package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicConditionValues;

/**
 * Created by Rajesh on 07-12-2017.
 */
@Repository
public interface TopicConditionValuesRepository  extends JpaRepository<TopicConditionValues, Long> {

	List<TopicConditionValues> findByTopicSocAssignmentConfigurationId(Long topicSocAssignmentConfigurationId);
	
	@Transactional
	Long deleteByTopicSocAssignmentConfigurationId(Long topicSocAssignmentConfigurationId);

}
