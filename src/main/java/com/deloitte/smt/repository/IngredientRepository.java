package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Ingredient;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository  extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByIngredientNameIn(List<String> ingredients);
    
    Ingredient findByTopicId(Long topicId);

    Ingredient findByDetectionId(Long detectionId);

    Ingredient findByDetectionIdAndIngredientNameIn(Long detectionId, List<String> ingredients);

    void deleteAllByDetectionId(Long detectionId);

    @Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL")
    List<String> findDistinctIngredientNames();
}
