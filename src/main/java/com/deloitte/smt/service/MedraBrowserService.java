package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Llt;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.Soc;
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
	
	/**
	 * 
	 * @param level
	 * @param searchText
	 * @return
	 */
	public MedraBrowserDTO getDetailsBySearchText(String level, String searchText) {
		MedraBrowserDTO medraBrowserDTO = new MedraBrowserDTO();
		

		switch (level) {

		case "ALL":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setSocs(socRepository.findBySocNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setHlgts(hlgtRepository.findByHlgtNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setHlts(hltRepository.findByHltNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setPts(ptRepository.findByPtNameContainingIgnoreCase(searchText));
			medraBrowserDTO.setLlts(lltRepository.findByLltNameContainingIgnoreCase(searchText));
			}
			else{
				getAllData(medraBrowserDTO);
			}
			break;

		case "SOC":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setSocs(socRepository.findBySocNameContainingIgnoreCase(searchText));
			}
			else{
				medraBrowserDTO.setSocs(socRepository.findDistinctSocNames());
			}
			break;

		case "HLGT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setHlgts(hlgtRepository.findByHlgtNameContainingIgnoreCase(searchText));
			}
			else{
				medraBrowserDTO.setHlgts(hlgtRepository.findDistinctHlgtNames());
			}
			break;

		case "HLT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setHlts(hltRepository.findByHltNameContainingIgnoreCase(searchText));
			}
			else{
				medraBrowserDTO.setHlts(hltRepository.findDistinctHltNames());
			}
			break;

		case "PT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setPts(ptRepository.findByPtNameContainingIgnoreCase(searchText));
			}
			else{
				medraBrowserDTO.setPts(ptRepository.findDistinctPtNames());
			}
			break;

		case "LLT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setLlts(lltRepository.findByLltNameContainingIgnoreCase(searchText));
			}
			else{
				medraBrowserDTO.setLlts(lltRepository.findDistinctLltNames());
			}
			break;
		
		default:
			getAllData(medraBrowserDTO);
			break;
		}

		return medraBrowserDTO;
	}
	
	private void getAllData(MedraBrowserDTO medraBrowserDTO){
		medraBrowserDTO.setSocs(socRepository.findDistinctSocNames());
		medraBrowserDTO.setHlgts(hlgtRepository.findDistinctHlgtNames());
		medraBrowserDTO.setHlts(hltRepository.findDistinctHltNames());
		medraBrowserDTO.setPts(ptRepository.findDistinctPtNames());
		medraBrowserDTO.setLlts(lltRepository.findDistinctLltNames());
	}

	public List<String> getSocsList(List<Soc> socs) {

		List<String> socNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				String socName = soc.getSocName();
				socNames.add(socName);
			}
		}
		return socNames;
	}

	public List<String> getHlgtsList(List<Hlgt> hlgts) {

		List<String> hlgtNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hlgts)) {
			for (Hlgt hlgt : hlgts) {
				String hlgtName = hlgt.getHlgtName();
				hlgtNames.add(hlgtName);
			}
		}
		return hlgtNames;
	}

	public List<String> getHltList(List<Hlt> hlts) {

		List<String> hltNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hlts)) {
			for (Hlt hlt : hlts) {
				String hltName = hlt.getHltName() ;
				hltNames.add(hltName);
			}
		}
		return hltNames;
	}

	public List<String> getPtList(List<Pt> pts) {

		List<String> ptNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(pts)) {
			for (Pt pt : pts) {
				String ptName = pt.getPtName();
				ptNames.add(ptName);
			}
		}
		return ptNames;
	}
	public List<String> getLltList(List<Llt> llts) {

		List<String> lltNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(llts)) {
			for (Llt llt : llts) {
				String lltName = llt.getLltName();
				lltNames.add(lltName);
			}
		}
		return lltNames;
	}

}
