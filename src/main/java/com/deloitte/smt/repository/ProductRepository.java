package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findAllByProductNameIn(List<String> productName);
    List<Product> findByTopicId(Long topicId);
    List<Product> findByDetectionId(Long detectionId);

    @Query(value = "SELECT o.productName FROM Product o WHERE o.productName IS NOT NULL")
    List<String> findDistinctProductName();
	void deleteAllByDetectionId(Long detectionId);
}
