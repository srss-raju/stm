package com.deloitte.smt.entity;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum AttachmentType {

    TOPIC_ATTACHMENT("Topic Attachment"),
    VALIDATE_ATTACHMENT("Validate And Prioritize Attachment"),
    ASSESSMENT_ATTACHMENT("Assessment Attachment");

    private String description;

    AttachmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
