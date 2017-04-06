package com.deloitte.smt.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum ActionStatus {
    UNASSIGNED("Unassigned"),
    IN_PROGRESS("In Progress"),
    APPROVED("Approved"),
    COMPLETED("Completed");

    private String description;

    ActionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getAll(){
        List<String> descriptionList = new ArrayList<>();
        for(ActionStatus actionStatus : ActionStatus.values()) {
            descriptionList.add(actionStatus.getDescription());
        }
        return descriptionList;
    }
}
