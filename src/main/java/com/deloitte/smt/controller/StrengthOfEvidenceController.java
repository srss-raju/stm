package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.StrengthOfEvidence;
import com.deloitte.smt.service.SignalStrengthService;
/**
 * 
 * @author shbondada
 *
 */
@RestController
@RequestMapping(value = "/camunda/api/signal")
public class StrengthOfEvidenceController {
	
	@Autowired
	SignalStrengthService signalStrengthService;
	
	/**
	 * This API returns the one time inserted data about Strength Of Evidence
	 * @return
	 */
	@GetMapping(value="/strengthOfEvidence")
	public List<StrengthOfEvidence> getStrengthOfEvidenceAtrributes(){
		
		return signalStrengthService.getStrengthOfEvidenceAttributes();
	}
}
