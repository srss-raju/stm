package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.TopicAssignees;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Repository
public interface TopicAssigneesRepository extends JpaRepository<TopicAssignees, Long> {
	
	List<TopicAssignees> findByTopicId(Long topicId);
	
    @Query("SELECT DISTINCT NEW TopicAssignees(o.userGroupKey, o.userKey) FROM TopicAssignees o")
	List<TopicAssignees> getSignalAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userKey) FROM TopicAssignees o WHERE o.userKey IS NOT NULL")
	List<Long> getAssignedUsers();
	
	@Query("SELECT DISTINCT(o.userGroupKey) FROM TopicAssignees o WHERE o.userGroupKey IS NOT NULL")
	List<Long> getAssignedGroups();

}
