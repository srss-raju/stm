package com.deloitte.smt.constant;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
public enum MeetingType {

    SIGNAL_MEETING("Signal Meeting"),
    ASSESSMENT_MEETING("Assessment Meeting"),
    FINAL_ASSESSMENT_MEETING("Final Assessment Meeting"),
    RISK_MEETING("Risk Meeting");

    private String description;

    MeetingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static MeetingType getByDescription(String description) {
        for(MeetingType meetingType :  MeetingType.values()) {
            if(meetingType.getDescription().equalsIgnoreCase(description)) {
                return meetingType;
            }
        }
        return MeetingType.SIGNAL_MEETING;
    }
}
