package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Product;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findAllByProductNameIn(List<String> productName);
    List<Product> findByTopicId(Long topicId);
    List<Product> findByDetectionId(Long detectionId);

    @Query(value = "SELECT distinct (o.productName) FROM Product o WHERE o.productName IS NOT NULL AND o.topicId IS not null")
    List<String> findDistinctProductNameForSignal();

    @Query(value = "SELECT distinct (o.productName) FROM Product o WHERE o.productName IS NOT NULL AND o.detectionId IS not null")
    List<String> findDistinctProductNameForSignalDetection();
    @Transactional
	Long deleteByDetectionId(Long detectionId);
}
