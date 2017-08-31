package com.deloitte.smt.controller;

import com.deloitte.smt.constant.ActionStatus;
import com.deloitte.smt.constant.ActionType;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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

    @GetMapping(value = "/filters/signal/{owner}/{userGroupKey}")
    public SearchDto getFiltersForSignal(@PathVariable String owner, @PathVariable Long userGroupKey) {
        return searchService.getFiltersForSignal(owner,userGroupKey);
    }

    @GetMapping(value = "/filters/signalDetection/{owner}/{userGroupKey}")
    public SearchDto getFiltersForSignalDetection(@PathVariable String owner, @PathVariable Long userGroupKey) {
        return searchService.getFiltersForSignalDetection(owner,userGroupKey);
    }

    @GetMapping(value = "/filters/assessmentPlan/{owner}/{userGroupKey}")
    public SearchDto getFiltersForAssessmentPlan(@PathVariable String owner, @PathVariable Long userGroupKey) {
        return searchService.getAllFiltersForAssessmentPlan(owner,userGroupKey);
    }

    @GetMapping(value = "/filters/riskPlan/{owner}/{userGroupKey}")
    public SearchDto getFiltersForRiskPlan(@PathVariable String owner, @PathVariable Long userGroupKey) {
        return searchService.getAllFiltersForRiskPlan(owner,userGroupKey);
    }

    @GetMapping(value = "/filters/ingredients")
    public List<String> getIngredientFilters() {
        return searchService.getIngredients();
    }

    @GetMapping(value = "/filters/severity")
    public List<String> getSeverityFilters() {
        return Arrays.asList("Low", "Medium", "High");
    }
}
