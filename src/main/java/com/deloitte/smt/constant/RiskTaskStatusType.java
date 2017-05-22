package com.deloitte.smt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum RiskTaskStatusType {
    COMPLETED("Completed"),
    NOT_COMPLETED("Not Completed");
    

    private String description;

    RiskTaskStatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getAll(){
        List<String> descriptionList = new ArrayList<>();
        for(RiskTaskStatusType actionType : RiskTaskStatusType.values()) {
            descriptionList.add(actionType.getDescription());
        }
        return descriptionList;
    }
}
