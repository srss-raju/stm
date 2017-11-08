package com.deloitte.smt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.LltRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.SocRepository;

@Service
public class MedraBrowserService {
	
	@Autowired
	SocRepository socRepository;
	@Autowired
	HlgtRepository hlgtRepository;
	@Autowired
	HltRepository hltRepository;
	@Autowired
	PtRepository ptRepository;
	@Autowired
	LltRepository lltRepository;

	public MedraBrowserDTO getDetailsBySearchText(String level,String searchText){
		MedraBrowserDTO medraBrowserDTO=new MedraBrowserDTO();
		
		switch(level){
		
		case "ALL":
			medraBrowserDTO.setSocs(socRepository.findBySocNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setHlgts(hlgtRepository.findByHlgtNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setHlts(hltRepository.findByHltNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setPts(ptRepository.findByPtNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setLlts(lltRepository.findByLltNameContainingIgnoreCase(searchText));
			break;
			
		case "SOC":
			medraBrowserDTO.setSocs(socRepository.findBySocNameContainingIgnoreCase(searchText));
			break;
		
		case "HLGT":
			medraBrowserDTO.setHlgts(hlgtRepository.findByHlgtNameContainingIgnoreCase(searchText));
			break;
			
		case "HLT":
			medraBrowserDTO.setHlts(hltRepository.findByHltNameContainingIgnoreCase(searchText));
			break;
			
		case "PT":
			medraBrowserDTO.setPts(ptRepository.findByPtNameContainingIgnoreCase(searchText));
			break;
			
		case "LLT":
			medraBrowserDTO.setLlts(lltRepository.findByLltNameContainingIgnoreCase(searchText));
			break;
			
		default:
			medraBrowserDTO.setSocs(socRepository.findBySocNameContainingIgnoreCase(searchText));
			break;
		}
		
		return medraBrowserDTO;
	}
	
}
