package com.deloitte.smt.repository;

import com.deloitte.smt.entity.TaskInst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
public interface TaskInstRepository extends JpaRepository<TaskInst, String> {

    /**
     * taskDefKeys are nothing but statuses
     *
     * @param taskDefKeys
     * @param deletedReason
     * @return
     */
    List<TaskInst> findAllByTaskDefKeyInAndProcInstIdNotNullAndDeleteReasonNotOrDeleteReasonNull(List<String> taskDefKeys, String deletedReason);

    /**
     * taskDefKeys are nothing but statuses
     *
     * @param taskDefKeys
     * @param deleteReason
     * @return
     */
    @Query("SELECT COUNT(o.id) FROM TaskInst o WHERE o.taskDefKey IN :taskDefKeys AND (o.deleteReason <> :deleteReason OR o.deleteReason IS NULL)")
    Long countByTaskDefKeyInAndDeleteReasonNot(@Param("taskDefKeys") List<String> taskDefKeys, @Param("deleteReason") String deleteReason);

    /**
     * taskDefKeys are nothing but statuses
     *
     * @param taskDefKeys
     * @return
     */
    List<TaskInst> findAllByTaskDefKeyIn(List<String> taskDefKeys);

    /**
     * taskDefKeys are nothing but statuses
     *
     * @param taskDefKeys
     * @return
     */
    Long countByTaskDefKeyIn(List<String> taskDefKeys);

    /**
     *
     * @param deleteReason
     * @return
     */
    List<TaskInst> findAllByDeleteReason(String deleteReason);

    /**
     *
     * @param deleteReason
     * @return
     */
    Long countByDeleteReason(String deleteReason);
}
