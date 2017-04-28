package com.deloitte.smt.controller;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.ActionStatus;
import com.deloitte.smt.entity.ActionType;
import com.deloitte.smt.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
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

   @Autowired
   SearchService searchService;

    @GetMapping(value = "/actionTypes")
    public List<String> getAllActionTypes(){
        return ActionType.getAll();
    }

    @GetMapping(value = "/actionStatus")
    public List<String> getAllActionStatuses(){
        return ActionStatus.getAll();
    }

    @GetMapping(value = "/filters/signal")
    public SearchDto getFiltersForSignal() {
        return searchService.getFiltersForSignal();
    }

    @GetMapping(value = "/filters/signalDetection")
    public SearchDto getFiltersForSignalDetection() {
        return searchService.getFiltersForSignalDetection();
    }

    @GetMapping(value = "/filters/assessmentPlan")
    public SearchDto getFiltersForAssessmentPlan() {
        return searchService.getAllFiltersForAssessmentPlan();
    }

    @GetMapping(value = "/filters/riskPlan")
    public SearchDto getFiltersForRiskPlan() {
        return searchService.getAllFiltersForRiskPlan();
    }
}
