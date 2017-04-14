package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findAllByIdInAndCreatedDateAndSignalStatusInOrderByCreatedDateDesc(List<Long> ids, Date createdDate, List<String> status);
    Long countByProcessIdIn(List<String> processId);
}
