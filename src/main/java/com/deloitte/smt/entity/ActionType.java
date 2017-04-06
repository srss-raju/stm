package com.deloitte.smt.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum ActionType {
    LABEL_CHANGE("Label Change"),
    COMMUNICATION("Communication"),
    TRAINING("Training");

    private String description;

    ActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getAll(){
        List<String> descriptionList = new ArrayList<>();
        for(ActionType actionType : ActionType.values()) {
            descriptionList.add(actionType.getDescription());
        }
        return descriptionList;
    }
}
