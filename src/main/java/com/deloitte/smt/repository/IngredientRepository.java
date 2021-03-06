package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface IngredientRepository  extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByIngredientNameIn(List<String> ingredients);
    
    Ingredient findByTopicId(Long topicId);

    List<Ingredient> findByDetectionId(Long detectionId);

    Ingredient findByDetectionIdAndIngredientNameIn(Long detectionId, List<String> ingredients);

    @Transactional
    Long deleteByDetectionId(Long detectionId);

    @Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL")
    List<String> findDistinctIngredientNames();

    @Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL AND o.topicId IS not null")
    List<String> findDistinctIngredientNamesForSignal();

    @Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL and o.detectionId is not null")
    List<String> findDistinctIngredientNamesForSignalDetection();

    @Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
    List<String> findDistinctIngredientNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);
}
