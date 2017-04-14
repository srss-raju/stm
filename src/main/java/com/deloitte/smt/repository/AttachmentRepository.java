package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByAttachmentResourceIdAndAttachmentType(Long attachmentResourceId, AttachmentType attachmentType, Sort sort);
}
