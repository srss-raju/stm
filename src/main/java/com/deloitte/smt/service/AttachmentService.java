package com.deloitte.smt.service;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public void delete(Long attachmentId) throws DeleteFailedException {
        Attachment a = attachmentRepository.findOne(attachmentId);
        if(a == null) {
            throw new DeleteFailedException("Delete failed for Attachment with given Id: "+attachmentId);
        }
        attachmentRepository.delete(a);
    }

    public List<Attachment> findByResourceIdAndAttachmentType(Long resourceId, AttachmentType attachmentType) {
        return attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(resourceId, attachmentType);
    }

    public void addAttachments(Long attachmentResourceId, MultipartFile[] attachments, AttachmentType attachmentType, List<Long> deletedAttachmentIds) throws IOException {
    if(!CollectionUtils.isEmpty(deletedAttachmentIds)) {
        for(Long attachmentId : deletedAttachmentIds) {
            attachmentRepository.delete(attachmentId);
        }
    }
        if(attachments != null) {
            for (MultipartFile attachment : attachments) {
                Attachment a = new Attachment();
                a.setAttachmentType(attachmentType);
                a.setAttachmentResourceId(attachmentResourceId);
                a.setContent(attachment.getBytes());
                a.setFileName(attachment.getOriginalFilename());
                attachmentRepository.save(a);
            }
        }
    }
}
