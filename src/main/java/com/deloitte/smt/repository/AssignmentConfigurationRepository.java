package com.deloitte.smt.repository;

import com.deloitte.smt.entity.AssignmentConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Repository
public interface AssignmentConfigurationRepository extends JpaRepository<AssignmentConfiguration, Long> {

    AssignmentConfiguration findByIngredientAndSignalSource(String ingredient, String signalSource);
}
