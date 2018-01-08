package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicProductValues;

/**
 * Created by Rajesh on 07-12-2017.
 */
@Repository
public interface TopicProductValuesRepository  extends JpaRepository<TopicProductValues, Long> {

	List<TopicProductValues> findByTopicProductAssignmentConfigurationId(Long topicProductAssignmentConfigurationId);
	
	@Transactional
	Long deleteByTopicProductAssignmentConfigurationId(Long topicProductAssignmentConfigurationId);

}
