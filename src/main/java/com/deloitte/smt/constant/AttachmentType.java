package com.deloitte.smt.constant;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum AttachmentType {

    TOPIC_ATTACHMENT("Topic Attachment"),
    VALIDATE_ATTACHMENT("Validate And Prioritize Attachment"),
    ASSESSMENT_ATTACHMENT("Assessment Attachment"),
    ASSESSMENT_ACTION_ATTACHMENT("Assessment Action Attachment"),
    MEETING_ATTACHMENT("Meeting Attachment"),
    FINAL_ASSESSMENT("Final Assessment"),
    RISK_ASSESSMENT("Risk Assessment"),
    RISK_TASK_ASSESSMENT("Risk Action Assessment");
//Signal, AssessmentPlan-FinalAssessment, AssessmentPlanAction, RiskPlanAction
    private String description;

    AttachmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
