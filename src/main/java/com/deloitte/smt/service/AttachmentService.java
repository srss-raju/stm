package com.deloitte.smt.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AttachmentRepository;

/**
 * Created by RajeshKumar B on 06-04-2017.
 */
@Service
public class AttachmentService {
	private static final Logger LOG = Logger.getLogger(AttachmentService.class);

    @Autowired
    AttachmentRepository attachmentRepository;

    public void save(Attachment attachment) {
        attachmentRepository.save(attachment);
    }

    public void delete(Long attachmentId) throws ApplicationException {
        Attachment a = attachmentRepository.findOne(attachmentId);
        if(a == null) {
            throw new ApplicationException("Delete failed for Attachment with given Id: "+attachmentId);
        }
        attachmentRepository.delete(a);
    }

    public List<Attachment> findByResourceIdAndAttachmentType(Long resourceId, AttachmentType attachmentType) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(resourceId, attachmentType, sort);
    }

    public Attachment findById(Long attachmentId) throws ApplicationException {
        Attachment a = attachmentRepository.findOne(attachmentId);
        if(a == null) {
            throw new ApplicationException("Attachment not found with the given Id : "+attachmentId);
        }
        return a;
    }

    public void addAttachments(Long attachmentResourceId, MultipartFile[] attachments, AttachmentType attachmentType, List<Long> deletedAttachmentIds, Map<String, Attachment> metaData)  {
	    boolean flag = false;
    	
    	deleteAttachments(deletedAttachmentIds);
        if(attachments != null) {
            for (MultipartFile attachment : attachments) {
            	flag = true;
                if(!attachment.isEmpty() || StringUtils.isNotBlank(attachment.getOriginalFilename())) {
                    Attachment a = new Attachment();
                    a.setAttachmentType(attachmentType);
                    addDescriptionAndUrl(metaData, attachment, a);
                    a.setAttachmentResourceId(attachmentResourceId);
                    a.setContentType(attachment.getContentType());
                    readBytes(attachment, a);
                    a.setFileName(attachment.getOriginalFilename());
                    a.setCreatedDate(new Date());
                    attachmentRepository.save(a);
                }
            }
        }
        if(!flag && !CollectionUtils.isEmpty(metaData)){
        		metaData.forEach((k,v) -> attachmentRepository.updateDescriptionAndAttachmentsURL(k, v.getDescription(), v.getAttachmentsURL()));
        }
    }

	/**
	 * @param attachment
	 * @param a
	 */
	private void readBytes(MultipartFile attachment, Attachment a) {
		try {
			a.setContent(attachment.getBytes());
		} catch (IOException e) {
			LOG.info("Exception occured while reading bytes "+e);
		}
	}

	/**
	 * @param metaData
	 * @param attachment
	 * @param a
	 */
	private void addDescriptionAndUrl(Map<String, Attachment> metaData,
			MultipartFile attachment, Attachment a) {
		if(metaData != null && StringUtils.isNotBlank(metaData.get(attachment.getOriginalFilename()).getDescription())) {
		    a.setDescription(metaData.get(attachment.getOriginalFilename()).getDescription());
		    a.setAttachmentsURL(metaData.get(attachment.getOriginalFilename()).getAttachmentsURL());
		}
	}

	/**
	 * @param deletedAttachmentIds
	 */
	private void deleteAttachments(List<Long> deletedAttachmentIds) {
		if(!CollectionUtils.isEmpty(deletedAttachmentIds)) {
	        for(Long attachmentId : deletedAttachmentIds) {
	            attachmentRepository.delete(attachmentId);
	        }
	    }
	}
}
