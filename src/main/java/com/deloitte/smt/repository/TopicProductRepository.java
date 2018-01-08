package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicProduct;

/**
 * Created by Rajesh on 07-12-2017.
 */
@Repository
public interface TopicProductRepository  extends JpaRepository<TopicProduct, Long> {

	List<TopicProduct> findByTopicId(Long topicId);
	
	@Transactional
	Long deleteByAssignmentConfigurationId(Long topicId);

	TopicProduct findByRecordKey(String conditionKey);
	
	List<TopicProduct> findDistinctByRecordKey(String conditionKey);

	List<TopicProduct> findByDetectionId(Long id);
	
	
}
