package com.deloitte.smt.entity;

import com.deloitte.smt.constant.AttachmentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Entity
@Table(name = "sm_attachment")
public class Attachment implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5681759063819490433L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    private byte[] content;
    private String fileName;
    private String contentType;
    private String description;
    private AttachmentType attachmentType;
    private String attachmentsURL;

    private Long attachmentResourceId;

    private Date createdDate;
    private Date lastModifiedDate;
    private String modifiedBy;
    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Long getAttachmentResourceId() {
        return attachmentResourceId;
    }

    public void setAttachmentResourceId(Long attachmentResourceId) {
        this.attachmentResourceId = attachmentResourceId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

	public String getAttachmentsURL() {
		return attachmentsURL;
	}

	public void setAttachmentsURL(String attachmentsURL) {
		this.attachmentsURL = attachmentsURL;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
