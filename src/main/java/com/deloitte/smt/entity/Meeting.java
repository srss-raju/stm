package com.deloitte.smt.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Entity
@Table(name = "sm_meeting")
public class Meeting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Date meetingDate;
    private Long meetingResourceId;
    private MeetingType meetingType;
    @Transient
    private List<Long> deletedAttachmentIds;
    @Embedded
    private Audit audit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Long getMeetingResourceId() {
        return meetingResourceId;
    }

    public void setMeetingResourceId(Long meetingResourceId) {
        this.meetingResourceId = meetingResourceId;
    }

    public MeetingType getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
    }

    public List<Long> getDeletedAttachmentIds() {
        return deletedAttachmentIds;
    }

    public void setDeletedAttachmentIds(List<Long> deletedAttachmentIds) {
        this.deletedAttachmentIds = deletedAttachmentIds;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }
}
