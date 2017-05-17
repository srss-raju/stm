package com.deloitte.smt.service;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.AttachmentRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(resourceId, attachmentType, sort);
    }

    public Attachment findById(Long attachmentId) throws EntityNotFoundException {
        Attachment a = attachmentRepository.findOne(attachmentId);
        if(a == null) {
            throw new EntityNotFoundException("Attachment not found with the given Id : "+attachmentId);
        }
        return a;
    }

    public void addAttachments(Long attachmentResourceId, MultipartFile[] attachments, AttachmentType attachmentType, List<Long> deletedAttachmentIds, Map<String, Attachment> metaData) throws IOException {
	    boolean flag = false;
    	
    	if(!CollectionUtils.isEmpty(deletedAttachmentIds)) {
	        for(Long attachmentId : deletedAttachmentIds) {
	            attachmentRepository.delete(attachmentId);
	        }
	    }
        if(attachments != null) {
            for (MultipartFile attachment : attachments) {
            	flag = true;
                if(!attachment.isEmpty() || StringUtils.isNotBlank(attachment.getOriginalFilename())) {
                    Attachment a = new Attachment();
                    a.setAttachmentType(attachmentType);
                    if(metaData != null && StringUtils.isNotBlank(metaData.get(attachment.getOriginalFilename()).getFileName())) {
                        a.setDescription(metaData.get(attachment.getOriginalFilename()).getDescription());
                        a.setAttachmentsURL(metaData.get(attachment.getOriginalFilename()).getAttachmentsURL());
                    }
                    a.setAttachmentResourceId(attachmentResourceId);
                    a.setContentType(attachment.getContentType());
                    a.setContent(attachment.getBytes());
                    a.setFileName(attachment.getOriginalFilename());
                    a.setCreatedDate(new Date());
                    attachmentRepository.save(a);
                }
            }
        }
        if(!flag && !CollectionUtils.isEmpty(metaData)){
        		metaData.forEach((k,v) -> attachmentRepository.updateDescription(k, v.getDescription()));
        }
    }
}
