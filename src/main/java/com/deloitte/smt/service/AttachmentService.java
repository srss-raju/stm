package com.deloitte.smt.service;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    public void save(Attachment attachment) {
        attachmentRepository.save(attachment);
    }

    public List<Attachment> findByResourceIdAndAttachmentType(Long resourceId, AttachmentType attachmentType) {
        return attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(resourceId, attachmentType);
    }
}
