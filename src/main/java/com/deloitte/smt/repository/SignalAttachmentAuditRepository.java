package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.smt.entity.SignalAttachmentAudit;

/**
 * Created by RajeshKumar on 06-08-2017.
 */
@Repository
public interface SignalAttachmentAuditRepository extends JpaRepository<SignalAttachmentAudit, Long> {

    List<SignalAttachmentAudit> findAllByAttachmentResourceId(Long attachmentResourceId);
   
}
