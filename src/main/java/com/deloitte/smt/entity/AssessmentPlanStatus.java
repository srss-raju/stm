package com.deloitte.smt.entity;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum AssessmentPlanStatus {
    ACTION_PLAN("ActionPlan");

    private String description;

    AssessmentPlanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
