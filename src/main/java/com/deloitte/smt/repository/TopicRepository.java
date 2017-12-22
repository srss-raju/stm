package com.deloitte.smt.repository;

import com.deloitte.smt.dto.TopicDTO;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;

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

    @Query(value = "SELECT DISTINCT (o.name) FROM Topic o WHERE o.name IS NOT NULL ")
    List<String> findDistinctSignalName();

    @Query(value = "SELECT DISTINCT (o.signalConfirmation) FROM Topic o WHERE o.signalConfirmation IS NOT NULL ")
    List<String> findDistinctSignalConfirmationNames();
	List<Topic> findTopicByRunInstanceIdOrderByCreatedDateAsc(Long runInstanceId);
	
	@Query(value="select NEW com.deloitte.smt.dto.TopicDTO(t.id,i.ingredientName,t.name,t.signalStatus) from Topic  t , Ingredient  i where t.id=i.topicId order by i.ingredientName", nativeQuery=false)
	List<TopicDTO> findByIngredientName();

	Long countByNameIgnoreCase(String topicName);
	
	@Query("SELECT DISTINCT(o.sourceName) FROM Topic o WHERE o.sourceName IS NOT NULL")
	List<String> getSourceNames();
	
	@Query("SELECT DISTINCT(o.owner) FROM Topic o WHERE o.owner IS NOT NULL ")
	List<String> findDistinctOwnerNames();
	@Query("select pas.recordKey from TopicProductAssignmentConfiguration pas, Topic t where t.id = pas.topicId")
	List<String> getProductFilterValues();
	@Query("select pas.recordKey from TopicSocAssignmentConfiguration pas, Topic t where t.id = pas.topicId")
	List<String> getConditionFilterValues();
	
}

