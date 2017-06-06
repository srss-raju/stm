package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findAllByIdInAndAssignToAndSignalStatusNotLikeOrderByCreatedDateDesc(List<Long> ids,String assignTo, String signalStatus);
    Long countByAssignToAndSignalStatusNotLikeIgnoreCase(String assignTo, String signalStatus);

    @Query(value = "SELECT DISTINCT (o.name) FROM Topic o WHERE o.name IS NOT NULL ")
    List<String> findDistinctSignalName();

    @Query(value = "SELECT DISTINCT (o.signalConfirmation) FROM Topic o WHERE o.signalConfirmation IS NOT NULL ")
    List<String> findDistinctSignalConfirmationNames();
	List<Topic> findTopicByRunInstanceIdOrderByCreatedDateAsc(Long runInstanceId);
	
	@Query("SELECT DISTINCT(o.assignTo) FROM Topic o WHERE o.assignTo IS NOT NULL")
	List<String> getAssignedUsers();
	
	@Query(value="select t.* from sm_topic t inner join sm_ingredient i on t.id=i.topic_id where i.ingredient_name=?1", nativeQuery=true)
	List<Topic> findByIngredientName(String ingredientName);
}
