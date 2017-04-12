package com.deloitte.smt.entity;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
public enum MeetingType {

    SIGNAL_MEETING("Signal Meeting"),
    ASSESSMENT_MEETING("Assessment Meeting");

    private String description;

    MeetingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
