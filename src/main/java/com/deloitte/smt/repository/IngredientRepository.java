package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Ingredient;

import java.util.List;

public interface IngredientRepository  extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByIngredientNameIn(List<String> ingredients);
    
    Ingredient findByTopicId(Long topicId);
}
