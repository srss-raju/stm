package com.deloitte.smt.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deloitte.smt.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	List<Ingredient> findAllByIngredientNameIn(List<String> ingredients);

	Ingredient findByTopicId(Long topicId);

	Ingredient findByDetectionId(Long detectionId);

	Ingredient findByDetectionIdAndIngredientNameIn(Long detectionId, List<String> ingredients);

	@Transactional
	Long deleteByDetectionId(Long detectionId);

	@Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL")
	List<String> findDistinctIngredientNames();

	@Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o,TopicSignalValidationAssignmentAssignees ta,Topic t WHERE ta.topicId=o.topicId AND ta.userGroupKey= :userGroupKey OR t.owner=:owner")
	List<String> findDistinctIngredientNamesForSignal(@Param("userGroupKey")Long userGroupKey);
	
	@Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o ,TopicSignalDetectionAssignmentAssignees ta,SignalDetection t WHERE o.detectionId=ta.detectionId AND ta.userGroupKey= :userGroupKey OR t.owner=:owner AND o.ingredientName IS NOT NULL")
	List<String> findDistinctIngredientNamesForSignalDetection(@Param("owner")String owner,@Param("userGroupKey")Long userGroupKey);

	@Query(value = "SELECT DISTINCT(o.ingredientName) FROM Ingredient o WHERE o.ingredientName IS NOT NULL and o.topicId is not null and o.topicId in :topicIds")
	List<String> findDistinctIngredientNamesTopicIdsIn(@Param("topicIds") Set<Long> topicIds);
}
