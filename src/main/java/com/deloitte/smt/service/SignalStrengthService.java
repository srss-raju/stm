package com.deloitte.smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.StrengthOfEvidence;
import com.deloitte.smt.repository.StrengthOfEvidenceRepository;

@Service
public class SignalStrengthService {
	
	@Autowired
	StrengthOfEvidenceRepository strengthOfEvidenceRepository;
	
	/**
	 * This method returns the list of Strength of Evidence Considerations
	 * @return
	 */
	public List<StrengthOfEvidence> getStrengthOfEvidenceAttributes(){
		
		return strengthOfEvidenceRepository.findAll();
	}
	

}
