package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import com.deloitte.smt.constant.AttachmentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Data
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

}
