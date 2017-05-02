package com.deloitte.smt.repository;

import com.deloitte.smt.entity.SignalConfirmations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by myelleswarapu on 02-05-2017.
 */
@Repository
public interface SignalConfirmationRespository extends JpaRepository<SignalConfirmations, Long> {
}
