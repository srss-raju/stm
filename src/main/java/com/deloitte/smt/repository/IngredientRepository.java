package com.deloitte.smt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.smt.entity.Ingredient;

public interface IngredientRepository  extends JpaRepository<Ingredient, Long> {

}
