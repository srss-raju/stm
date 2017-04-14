package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Product;

public interface ProductRepository  extends JpaRepository<Product, Long> {

}
