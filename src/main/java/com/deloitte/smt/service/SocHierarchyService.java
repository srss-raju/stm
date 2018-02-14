package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.DetectionRunDTO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.PtLltDTO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.entity.TopicAssignmentCondition;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;
import com.deloitte.smt.repository.ConditionLevelRepository;

/**
 * 
 * @author shbondada
 *
 */
@Service
public class SocHierarchyService {

	@Autowired
	SocMedraHierarchyDAO socMedraHierarchyDAO;

	@Autowired
	private ConditionLevelRepository conditionLevelRepository;
	/**
	 * This method will fetch all the SOC's and corresponding hlgts hlts pts
	 * llts by soc description
	 * 
	 * @param socCode
	 * @param socName
	 * @return
	 */
	public List<SocSearchDTO> getHierarchyByCode(MedraBrowserDTO medraBrowserDto) {
		List<SocHierarchyDto> socHierarchyList;

		socHierarchyList = socMedraHierarchyDAO.findAllByConditionName(medraBrowserDto.getSearchValue(), medraBrowserDto.getSelectLevel(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		return responseMapper(socHierarchyList);
	}

	private List<SocSearchDTO> responseMapper(List<SocHierarchyDto> socHierarchyList) {

		List<SocSearchDTO> socDtoList = new ArrayList<>();

		if (!CollectionUtils.isEmpty(socHierarchyList)) {
			List<ConditionLevels> cndl= conditionLevelRepository.findAll();
			Map<String,String> levelMap = null;
			if(!CollectionUtils.isEmpty(cndl))
			{
				levelMap = new HashMap<>();
				for (ConditionLevels conditionLevels : cndl) {
					levelMap.put(conditionLevels.getKey(), conditionLevels.getValue());
				}
			}
			for (SocHierarchyDto socHierarchyDto : socHierarchyList) {
				
				SocSearchDTO socSearchdto = addSocs(socHierarchyDto,levelMap);
				socDtoList.add(socSearchdto);
				SocSearchDTO hlgtSearchdto = addHlgts(socHierarchyDto,levelMap);
				socDtoList.add(hlgtSearchdto);
				SocSearchDTO hltSearchdto = addHlts(socHierarchyDto,levelMap);
				socDtoList.add(hltSearchdto);
				SocSearchDTO ptSearchdto = addPts(socHierarchyDto,levelMap);
				socDtoList.add(ptSearchdto);
				SocSearchDTO lltSearchdto = addLlts(socHierarchyDto,levelMap);
				socDtoList.add(lltSearchdto);
			}
		}
		return socDtoList;

	}

	private SocSearchDTO addSocs(SocHierarchyDto socHierarchyDto, Map<String, String> levelMap) {
		SocSearchDTO socSearchdto = new SocSearchDTO();
		socSearchdto.setCategory(SmtConstant.SOC_CODE.getDescription());
		socSearchdto.setCategoryCode(socHierarchyDto.getSoc_code());
		socSearchdto.setCategoryDesc(socHierarchyDto.getSoc_desc());
		socSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.SOC_CODE.getDescription())?levelMap.get(SmtConstant.SOC_CODE.getDescription()):null);
		return socSearchdto;
	}

	private SocSearchDTO addHlgts(SocHierarchyDto socHierarchyDto, Map<String, String> levelMap) {
		SocSearchDTO hlgtSearchdto = new SocSearchDTO();
		hlgtSearchdto.setCategory(SmtConstant.HLGT_CODE.getDescription());
		hlgtSearchdto.setCategoryCode(socHierarchyDto.getHlgt_code());
		hlgtSearchdto.setCategoryDesc(socHierarchyDto.getHlgt_desc());
		hlgtSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.HLGT_CODE.getDescription())?levelMap.get(SmtConstant.HLGT_CODE.getDescription()):null);
		return hlgtSearchdto;
	}

	private SocSearchDTO addHlts(SocHierarchyDto socHierarchyDto, Map<String, String> levelMap) {
		SocSearchDTO hltSearchdto = new SocSearchDTO();
		hltSearchdto.setCategory(SmtConstant.HLT_CODE.getDescription());
		hltSearchdto.setCategoryCode(socHierarchyDto.getHlt_code());
		hltSearchdto.setCategoryDesc(socHierarchyDto.getHlt_desc());
		hltSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.HLT_CODE.getDescription())?levelMap.get(SmtConstant.HLT_CODE.getDescription()):null);
		return hltSearchdto;
	}

	private SocSearchDTO addPts(SocHierarchyDto socHierarchyDto, Map<String, String> levelMap) {
		SocSearchDTO ptSearchdto = new SocSearchDTO();
		ptSearchdto.setCategory(SmtConstant.PT_CODE.getDescription());
		ptSearchdto.setCategoryCode(socHierarchyDto.getPt_code());
		ptSearchdto.setCategoryDesc(socHierarchyDto.getPt_desc());
		ptSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.PT_CODE.getDescription())?levelMap.get(SmtConstant.PT_CODE.getDescription()):null);
		return ptSearchdto;
	}

	private SocSearchDTO addLlts(SocHierarchyDto socHierarchyDto, Map<String, String> levelMap) {
		SocSearchDTO lltSearchdto = new SocSearchDTO();
		lltSearchdto.setCategory(SmtConstant.LLT_CODE.getDescription());
		lltSearchdto.setCategoryCode(socHierarchyDto.getLlt_code());
		lltSearchdto.setCategoryDesc(socHierarchyDto.getLlt_desc());
		lltSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.LLT_CODE.getDescription())?levelMap.get(SmtConstant.LLT_CODE.getDescription()):null);
		return lltSearchdto;
	}

	/**
	 * This method is invoked medra browser search by keyword
	 * 
	 * @param level
	 * @param searchText
	 * @return
	 */
	public List<SocSearchDTO> getDetailsBySearchText(MedraBrowserDTO medraBrowserDto) {
		List<SocSearchDTO> socSearchDtoList = new ArrayList<>();
		
		String level;
		if(null==medraBrowserDto.getScrollColumn()){
			level=medraBrowserDto.getSearchLevel();
		}
		else{
			level=medraBrowserDto.getScrollColumn();
		}
			
		if (null != level) {
			switch (level) {

			case "ALL":
				socSearchDtoList = searchAllByName(medraBrowserDto);
				break;

			case "SOC_CODE":
				socSearchDtoList = searchBySocName(medraBrowserDto);
				break;

			case "HLGT_CODE":
				socSearchDtoList = searchByHlgtName(medraBrowserDto);
				break;

			case "HLT_CODE":
				socSearchDtoList = searchByHltName(medraBrowserDto);
				break;

			case "PT_CODE":
				socSearchDtoList = searchByPtName(medraBrowserDto);
				break;

			case "LLT_CODE":
				socSearchDtoList = searchBylltName(medraBrowserDto);
				break;

			default:
				break;
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchBySocName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getConditionByKeyName(SmtConstant.SOC_CODE.getDescription());
		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByMatchingSocName(
				medraBrowserDto.getSearchValue(), medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.SOC_CODE.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return socSearchDtoList;
	}

	String getConditionByKeyName(String keyName) {
		ConditionLevels crl= conditionLevelRepository.findByKey(keyName);
		if(crl!=null)
			return crl.getValue();
		else
			return null;
	}

	private List<SocSearchDTO> searchByHlgtName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getConditionByKeyName(SmtConstant.HLGT_CODE.getDescription());
		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByHlgtName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.HLGT_CODE.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchByHltName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getConditionByKeyName(SmtConstant.HLT_CODE.getDescription());
		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByHltName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.HLT_CODE.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return socSearchDtoList;
	}

	

	private List<SocSearchDTO> searchByPtName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getConditionByKeyName(SmtConstant.PT_CODE.getDescription());
		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByPtName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.PT_CODE.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return socSearchDtoList;
	}
	
	private List<SocSearchDTO> searchBylltName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getConditionByKeyName(SmtConstant.LLT_CODE.getDescription());
		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByLltName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.LLT_CODE.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchAllByName(MedraBrowserDTO medraBrowserDto) {
		List<SocSearchDTO> socSearchDtoAllList = new ArrayList<>();
		List<SocSearchDTO> socSearchDtoList = searchBySocName(medraBrowserDto);
		for (SocSearchDTO socSearchDto : socSearchDtoList) {
			socSearchDtoAllList.add(socSearchDto);
		}
		List<SocSearchDTO> socSearchDtoHlgtList = searchByHlgtName(medraBrowserDto);
		for (SocSearchDTO socSearchDto : socSearchDtoHlgtList) {
			socSearchDtoAllList.add(socSearchDto);
		}
		List<SocSearchDTO> socSearchDtoHltList = searchByHltName(medraBrowserDto);
		for (SocSearchDTO socSearchDto : socSearchDtoHltList) {
			socSearchDtoAllList.add(socSearchDto);
		}
		List<SocSearchDTO> socSearchDtoPtList = searchByPtName(medraBrowserDto);
		for (SocSearchDTO socSearchDto : socSearchDtoPtList) {
			socSearchDtoAllList.add(socSearchDto);
		}
		List<SocSearchDTO> socSearchDtoLltList = searchBylltName(medraBrowserDto);
		for (SocSearchDTO socSearchDto : socSearchDtoLltList) {
			socSearchDtoAllList.add(socSearchDto);
		}

		return socSearchDtoAllList;
	}
	
	public String getEventKeyOfSelectedLevel(List<TopicAssignmentCondition> recordValues, DetectionRunDTO dto){
		String socLevel = null;
		StringBuilder queryBuider = new StringBuilder("SELECT DISTINCT EVENT_KEY FROM EVENT_DIM WHERE ");
		if(!CollectionUtils.isEmpty(recordValues)){
			for(TopicAssignmentCondition record:recordValues){
				queryBuider.append(record.getCategory()).append("='").append(record.getCategoryCode());
				queryBuider.append("' AND ");
				socLevel = record.getCategory();
			}
			String query = queryBuider.toString().substring(0, queryBuider.lastIndexOf("AND"));
			if(dto.getPrimaryEventLevel()!=null){
				dto.setSecondaryEventLevel(socLevel);
			}else{
				dto.setPrimaryEventLevel(socLevel);
			}
			return socMedraHierarchyDAO.getEventKey(query);
		}else{
			dto.setPrimaryEventLevel("All");
			dto.setSecondaryEventLevel("All");
		}
		return "";
	}

	public PtLltDTO findPtsAndLlts(List<TopicSocAssignmentConfiguration> conditions) {
		List<String> allPts = new ArrayList<>();
		List<String> allLlts = new ArrayList<>();
		Set<String> ptSet = null;
		Set<String> lltSet = null;
		StringBuilder ptQueryBuider = new StringBuilder("SELECT DISTINCT PT_DESC FROM SOC_MEDDRA_HIERARCHY ");
		StringBuilder lltQueryBuider = new StringBuilder("SELECT DISTINCT LLT_DESC FROM SOC_MEDDRA_HIERARCHY ");
		if(!CollectionUtils.isEmpty(conditions)){
			ptQueryBuider.append("WHERE ");
			lltQueryBuider.append("WHERE ");
			for(TopicSocAssignmentConfiguration condition:conditions){
				for(TopicAssignmentCondition record:condition.getRecordValues()){
					ptQueryBuider.append(record.getCategory()).append("='").append(record.getCategoryCode());
					ptQueryBuider.append("' AND ");
					
					lltQueryBuider.append(record.getCategory()).append("='").append(record.getCategoryCode());
					lltQueryBuider.append("' AND ");
					
				}
				String ptQuery = ptQueryBuider.toString().substring(0, ptQueryBuider.lastIndexOf("AND"));
				String lltQuery = lltQueryBuider.toString().substring(0, lltQueryBuider.lastIndexOf("AND"));
				allPts.addAll(socMedraHierarchyDAO.getPts(ptQuery));
				allLlts.addAll(socMedraHierarchyDAO.getLlts(lltQuery));
			}
			ptSet = new HashSet<>(allPts);
			lltSet = new HashSet<>(allLlts);
		}
		PtLltDTO ptLltDTO = new PtLltDTO();
		ptLltDTO.setPts(ptSet);
		ptLltDTO.setLlts(lltSet);
		return ptLltDTO;
	}

}
