package com.deloitte.smt.repository;

import com.deloitte.smt.entity.SMUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Repository
public interface SMUserRepository extends JpaRepository<SMUser, Long> {
}
