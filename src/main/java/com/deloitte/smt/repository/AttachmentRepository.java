package com.deloitte.smt.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByAttachmentResourceIdAndAttachmentType(Long attachmentResourceId, AttachmentType attachmentType, Sort sort);
    
    @Modifying
    @Transactional(readOnly=false)
    @Query("update Attachment a set a.description = ?2 , a.attachmentsURL = ?3 where a.fileName = ?1")
    Integer updateDescriptionAndAttachmentsURL(String fileName, String description, String attachmentsURL);
}
