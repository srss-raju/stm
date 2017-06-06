package com.deloitte.smt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.service.DashboardService;
import com.deloitte.smt.dto.DashboardDTO;
import com.deloitte.smt.entity.Topic;

@RestController
@RequestMapping(value = "/camunda/api/charts/signal")
public class DashboardController {
	
	@Autowired
	private DashboardService chartsService;
	
	@GetMapping(value = "/bysmt")
	public String getSmtComplianceDetails() {
		
		return null;
	}
	
	@GetMapping(value="/ingredient/{ingredientName}")
	public DashboardDTO getSignalsByIngredient(@PathVariable String ingredientName){
		return chartsService.getSignalsByIngredient(ingredientName);
	}

}
