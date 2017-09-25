package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.Product;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findAllByProductNameIn(List<String> productName);
    List<Product> findByTopicId(Long topicId);
    List<Product> findByDetectionId(Long detectionId);
    
    List<Product> findByIngredientId(Long ingredientId);

    @Query(value = "SELECT distinct (o.productName) FROM Product o WHERE o.productName IS NOT NULL AND o.topicId IS not null")
    List<String> findDistinctProductNameForSignal();

    @Query(value = "SELECT distinct (o.productName) FROM Product o WHERE o.productName IS NOT NULL AND o.detectionId IS not null")
    List<String> findDistinctProductNameForSignalDetection();

    @Query(value = "SELECT DISTINCT(o.productName) FROM Product o WHERE o.productName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctProductNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);

    @Transactional
	Long deleteByDetectionId(Long detectionId);
}
