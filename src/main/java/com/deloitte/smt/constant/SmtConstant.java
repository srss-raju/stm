package com.deloitte.smt.constant;

public enum SmtConstant {
	ASSESSMENT_PLAN("assessmentPlan"),
	RISK_PLAN("riskPlan"),
	ASSESSMENT_PLAN_STATUS("assessmentPlanStatus"),
	ASSIGN_TO("assignTo"),
    TOPIC_ID("topicId"),
    ASSESSMENT_DUE_DATE("assessmentDueDate"),
    CREATED_DATE("createdDate"),
    ASSESSMENT_TASK_STATUS("assessmentTaskStatus");
    private String description;

    SmtConstant(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
