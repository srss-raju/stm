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
    NOTCOMPLETED("Not Completed"),
    INGREDIENT_NAME("ingredientName"),
    IN_PROGRESS("In Progress"),
    ASSESSMENT_TASK_STATUS("assessmentTaskStatus"),
    ASSESSMENT_RISK_STATUS("assessmentRiskStatus"),
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    SOC_CODE("SOC_CODE"),
    HLGT_CODE("HLGT_CODE"),
    HLT_CODE("HLT_CODE"),
    LLT_CODE("LLT_CODE"),
    PT_CODE("PT_CODE"),
    USER_KEY("userKey"),
    USER_GROUP_KEY("userGroupKey"),
    SCORE("score"),
    ASSIGNEES_GROUP_KEY("-99"),
    SUMMARY("{\"Heading1 <You can edit this>\":\"<Please enter appropriate description for this section>\",\"Heading2 <You can edit this>\":\"<Please enter appropriate description for this section>\",\"Heading3 <You can edit this>\":\"<Please enter appropriate description for this section>\"}"),
    SUMMARY_COMPLETED("{\"Heading1\":\"NA\",\"Heading2\":\"NA\",\"Heading3\":\"NA\"}"),
    OWNER("owner");
    private String description;

    SmtConstant(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
