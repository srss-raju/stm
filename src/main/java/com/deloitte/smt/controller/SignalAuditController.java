/*package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.SignalAudit;
import com.deloitte.smt.service.SignalAuditService;

*//**
 * 
 * @author rbongurala
 *
 *//*
@RestController
@RequestMapping(value = "/camunda/api/signal/audit")
public class SignalAuditController {
	
	@Autowired
	SignalAuditService signalAuditService;
	
	@GetMapping
	public List<SignalAudit> getAuditDetails(){
		return signalAuditService.getAuditDetails();
	}

}
*/