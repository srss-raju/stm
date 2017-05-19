package com.deloitte.smt.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum AssessmentTaskStatusType {
    COMPLETED("Completed"),
    NOT_COMPLETED("Not Completed");
    

    private String description;

    AssessmentTaskStatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getAll(){
        List<String> descriptionList = new ArrayList<>();
        for(AssessmentTaskStatusType actionType : AssessmentTaskStatusType.values()) {
            descriptionList.add(actionType.getDescription());
        }
        return descriptionList;
    }
}
