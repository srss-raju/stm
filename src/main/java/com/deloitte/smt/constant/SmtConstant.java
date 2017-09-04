package com.deloitte.smt.constant;

public enum SmtConstant {
	ASSESSMENT_PLAN("assessmentPlan"),
	RISK_PLAN("riskPlan"),
	RISK_TASK_STATUS("riskTaskStatus"),
	RISK_DUE_DATE("riskDueDate"),
	STATUS("status"),
	ASSESSMENT_PLAN_STATUS("assessmentPlanStatus"),
	ASSIGN_TO("assignTo"),
    TOPIC_ID("topicId"),
    DETECTION_ID("detectionId"),
    ASSESSMENT_DUE_DATE("assessmentDueDate"),
    CREATED_DATE("createdDate"),
    COMPLETED("Completed"),
    INGREDIENT_NAME("ingredientName"),
    IN_PROGRESS("In Progress"),
    ASSESSMENT_TASK_STATUS("assessmentTaskStatus"),
    ASSESSMENT_RISK_STATUS("assessmentRiskStatus"),
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    ASSIGNEES_GROUP_KEY("-99"),
    OWNER("owner"),
	USER_KEY("userKey"),
	USER_GROUP_KEY("userGroupKey");
	
    private String description;

    SmtConstant(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
