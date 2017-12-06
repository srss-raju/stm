package com.deloitte.smt.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicAssignmentProduct;

/**
 * Created by Rajesh on 07-12-2017.
 */
@Repository
public interface TopicAssignmentProductRepository  extends JpaRepository<TopicAssignmentProduct, Long> {

	List<TopicAssignmentProduct> findByTopicProductAssignmentConfigurationId(Long topicProductAssignmentConfigurationId);
	
	@Transactional
	Long deleteByTopicProductAssignmentConfigurationId(Long topicProductAssignmentConfigurationId);

}
