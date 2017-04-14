package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Product;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findAllByProductNameIn(List<String> productName);
}
