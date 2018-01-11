package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.deloitte.smt.constant.MeetingType;

import lombok.Data;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Data
@Entity
@Table(name = "sm_meeting")
public class Meeting implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7342659915978492209L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Date meetingDate;
    private Long meetingResourceId;
    private MeetingType meetingType;
    
    @OneToMany
	@JoinColumn(name = "meetingId")
	private List<Attachment> attachments;
	

    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;

    
}
