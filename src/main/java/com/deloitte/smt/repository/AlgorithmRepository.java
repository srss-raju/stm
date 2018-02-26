package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.Algorithm;

/**
 * Created by Rajesh on 02-05-2017.
 */
@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
}
