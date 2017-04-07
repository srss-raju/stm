package com.deloitte.smt.controller;

import com.deloitte.smt.entity.ActionStatus;
import com.deloitte.smt.entity.ActionType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
@RestController
@RequestMapping(value = "/camunda/api/signal/utils")
public class MasterDataController {

    @GetMapping(value = "/actionTypes")
    public List<String> getAllActionTypes(){
        return ActionType.getAll();
    }

    @GetMapping(value = "/actionStatus")
    public List<String> getAllActionStatuses(){
        return ActionStatus.getAll();
    }
}
