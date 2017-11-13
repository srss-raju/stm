package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
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
	
	private static final Logger LOG = Logger.getLogger(MedraBrowserService.class);

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
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
				medraBrowserDTO.setSocs(searchTextBySocs(searchText));
				medraBrowserDTO.setHlgts(searchTextByHlgts(searchText));
				medraBrowserDTO.setHlts(searchTextByHlts(searchText));
				medraBrowserDTO.setPts(searchTextByPts(searchText));
				medraBrowserDTO.setLlts(searchTextByLlts(searchText));
			}
			else{
				getAllData(medraBrowserDTO);
			}
			break;

		case "SOC":
			if(!searchText.isEmpty()){
				medraBrowserDTO.setSocs(searchTextBySocs(searchText));
			}
			else{
				medraBrowserDTO.setSocs(socRepository.findDistinctSocNames());
			}
			break;

		case "HLGT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setHlgts(searchTextByHlgts(searchText));
			}
			else{
				medraBrowserDTO.setHlgts(hlgtRepository.findDistinctHlgtNames());
			}
			break;

		case "HLT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setHlts(searchTextByHlts(searchText));
			}
			else{
				medraBrowserDTO.setHlts(hltRepository.findDistinctHltNames());
			}
			break;

		case "PT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setPts(searchTextByPts(searchText));
			}
			else{
				medraBrowserDTO.setPts(ptRepository.findDistinctPtNames());
			}
			break;

		case "LLT":
			if(!searchText.isEmpty()){
			medraBrowserDTO.setLlts(searchTextByLlts(searchText));
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
	
	private List<String> searchTextBySocs(String searchText){
		
		String queryStr=" select distinct upper(o.soc_name) from sm_soc o where o.soc_name ilike lower(:socName)";
		
		Query query = entityManager.createNativeQuery(queryStr);
		if(queryStr.contains(":socName")){
			query.setParameter("socName", "%"+searchText+"%");
		}
		@SuppressWarnings("unchecked")
		List<String> socs=query.getResultList();
		
		return socs;
		
	}
	
	private List<String> searchTextByHlgts(String searchText){
		
		String queryStr=" select distinct upper(o.hlgt_name) from sm_hlgt o where o.hlgt_name ilike lower(:hlgtName)";
		
		Query query = entityManager.createNativeQuery(queryStr);
		if(queryStr.contains(":hlgtName")){
			query.setParameter("hlgtName", "%"+searchText+"%");
		}
		@SuppressWarnings("unchecked")
		List<String> hlgts=query.getResultList();
		
		return hlgts;
		
	}
	
	private List<String> searchTextByHlts(String searchText){
		
		String queryStr=" select distinct upper(o.hlt_name) from sm_hlt o where o.hlt_name ilike lower(:hltName)";
		
		Query query = entityManager.createNativeQuery(queryStr);
		if(queryStr.contains(":hltName")){
			query.setParameter("hltName", "%"+searchText+"%");
		}
		@SuppressWarnings("unchecked")
		List<String> hlts=query.getResultList();
		
		
		return hlts;
		
	}
	private List<String> searchTextByPts(String searchText){
		
		String queryStr=" select distinct upper(o.pt_name) from sm_pt o where o.pt_name ilike lower(:ptName)";
		
		Query query = entityManager.createNativeQuery(queryStr);
		if(queryStr.contains(":ptName")){
			query.setParameter("ptName", "%"+searchText+"%");
		}
		@SuppressWarnings("unchecked")
		List<String> pts=query.getResultList();
		
		
		return pts;
		
	}
	private List<String> searchTextByLlts(String searchText){
		
		String queryStr=" select distinct upper(o.llt_name) from sm_llt o where o.llt_name ilike lower(:lltName)";
		
		Query query = entityManager.createNativeQuery(queryStr);
		if(queryStr.contains(":lltName")){
			query.setParameter("lltName", "%"+searchText+"%");
		}
		@SuppressWarnings("unchecked")
		List<String> llts=query.getResultList();
		
		return llts;
		
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
