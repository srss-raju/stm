package com.deloitte.smt.service;

import java.util.List;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.repository.RiskPlanRepository;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Service
public class RiskPlanService {

    @Autowired
    RiskPlanRepository riskPlanRepository;

    @Autowired
    CaseService caseService;

    public void insert(RiskPlan riskPlan) {
        CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
        riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
        riskPlanRepository.save(riskPlan);
    }
    
    public List<RiskPlan> findAllRiskPlansByStatus(String status) {
        if(StringUtils.isEmpty(status)) {
            return riskPlanRepository.findAll();
        }
        return riskPlanRepository.findAllByStatus(status);
    }
}
