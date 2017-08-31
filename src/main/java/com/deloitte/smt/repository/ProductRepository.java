package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findAllByProductNameIn(List<String> productName);
    List<Product> findByTopicId(Long topicId);
    List<Product> findByDetectionId(Long detectionId);

    @Query(value = "SELECT DISTINCT (o.productName) FROM Product o, TopicSignalValidationAssignmentAssignees ta,Topic t WHERE ta.topicId=o.topicId AND ta.topicId=t.id AND ta.userGroupKey =:userGroupKey OR t.owner=:owner")
    List<String> findDistinctProductNameForSignal(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value = "SELECT DISTINCT(o.productName) FROM Product o,TopicSignalDetectionAssignmentAssignees ta,SignalDetection t WHERE o.detectionId=ta.detectionId AND o.topicId= t.id AND ta.userGroupKey= :userGroupKey OR t.owner= :owner AND o.productName IS NOT NULL AND o.detectionId IS not null")
    List<String> findDistinctProductNameForSignalDetection(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

    @Query(value = "SELECT DISTINCT(o.productName) FROM Product o WHERE o.productName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctProductNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

    @Transactional
	Long deleteByDetectionId(Long detectionId);
}
