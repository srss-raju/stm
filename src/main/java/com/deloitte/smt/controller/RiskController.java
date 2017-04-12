package com.deloitte.smt.controller;

import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.service.RiskPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/risk")
public class RiskController {

    @Autowired
    RiskPlanService riskPlanService;

    @PostMapping()
    public String createRiskPlan(@RequestBody RiskPlan riskPlan) {
        riskPlanService.insert(riskPlan);
        return "Successfully Created";
    }
}
