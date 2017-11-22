package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.dto.SocSearchDTO;

/**
 * 
 * @author shbondada
 *
 */
@Service
public class SocHierarchyService {

	@Autowired
	SocMedraHierarchyDAO socMedraHierarchyDAO;

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
		String level=getLevel(medraBrowserDto);
		
		socHierarchyList = socMedraHierarchyDAO.findAllByConditionName(medraBrowserDto.getSearchValue(),
				level, medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		return responseMapper(socHierarchyList);
	}

	private List<SocSearchDTO> responseMapper(List<SocHierarchyDto> socHierarchyList) {

		List<SocSearchDTO> socDtoList = new ArrayList<>();

		if (!CollectionUtils.isEmpty(socHierarchyList)) {
			for (SocHierarchyDto socHierarchyDto : socHierarchyList) {
				SocSearchDTO socSearchdto = new SocSearchDTO();
				if (null != socHierarchyDto.getSoc_code()) {
					socSearchdto.setCategory(SmtConstant.SOC_CODE.getDescription());
					socSearchdto.setCategoryCode(socHierarchyDto.getSoc_code());
					socSearchdto.setCategoryDesc(socHierarchyDto.getSoc_desc());
				}
				socDtoList.add(socSearchdto);
				SocSearchDTO hlgtSearchdto = new SocSearchDTO();
				if (null != socHierarchyDto.getHlgt_code()) {
					hlgtSearchdto.setCategory(SmtConstant.HLGT_CODE.getDescription());
					hlgtSearchdto.setCategoryCode(socHierarchyDto.getHlgt_code());
					hlgtSearchdto.setCategoryDesc(socHierarchyDto.getHlgt_desc());
				}
				socDtoList.add(hlgtSearchdto);
				SocSearchDTO hltSearchdto = new SocSearchDTO();
				if (null != socHierarchyDto.getHlt_code()) {
					hltSearchdto.setCategory(SmtConstant.HLT_CODE.getDescription());
					hltSearchdto.setCategoryCode(socHierarchyDto.getHlt_code());
					hltSearchdto.setCategoryDesc(socHierarchyDto.getHlt_desc());
				}
				socDtoList.add(hltSearchdto);
				SocSearchDTO ptSearchdto = new SocSearchDTO();
				if (null != socHierarchyDto.getPt_code()) {
					ptSearchdto.setCategory(SmtConstant.PT_CODE.getDescription());
					ptSearchdto.setCategoryCode(socHierarchyDto.getPt_code());
					ptSearchdto.setCategoryDesc(socHierarchyDto.getPt_desc());
				}
				socDtoList.add(ptSearchdto);
				SocSearchDTO lltSearchdto = new SocSearchDTO();
				if (null != socHierarchyDto.getLlt_code()) {
					lltSearchdto.setCategory(SmtConstant.LLT_CODE.getDescription());
					lltSearchdto.setCategoryCode(socHierarchyDto.getLlt_code());
					lltSearchdto.setCategoryDesc(socHierarchyDto.getLlt_desc());
				}
				socDtoList.add(lltSearchdto);
				
			}
		}
		return socDtoList;

	}

	/**
	 * This method is invoked medra browser search by keyword
	 * 
	 * @param level
	 * @param searchText
	 * @return
	 */
	public List<SocSearchDTO> getDetailsBySearchText(MedraBrowserDTO medraBrowserDto) {
		List<SocSearchDTO> socSearchDtoList=new ArrayList<>();
		String level=getLevel(medraBrowserDto);
		if(null!=level){
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
			socSearchDtoList = searchBySocName(medraBrowserDto);
			break;
		}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchBySocName(MedraBrowserDTO medraBrowserDto) {

		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByMatchingSocName(medraBrowserDto.getSearchValue(),
												medraBrowserDto.getScrollOffset(),medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.SOC_CODE.getDescription());
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchByHlgtName(MedraBrowserDTO medraBrowserDto) {

		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByHlgtName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(),medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.HLGT_CODE.getDescription());
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchByHltName(MedraBrowserDTO medraBrowserDto) {

		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByHltName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(),medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.HLT_CODE.getDescription());
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchBylltName(MedraBrowserDTO medraBrowserDto) {

		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByLltName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(),medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.LLT_CODE.getDescription());
			}
		}
		return socSearchDtoList;
	}

	private List<SocSearchDTO> searchByPtName(MedraBrowserDTO medraBrowserDto) {

		List<SocSearchDTO> socSearchDtoList = socMedraHierarchyDAO.findByPtName(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(),medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(socSearchDtoList)) {
			for (SocSearchDTO searchDto : socSearchDtoList) {
				searchDto.setCategory(SmtConstant.PT_CODE.getDescription());
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
	private String getLevel(MedraBrowserDTO medraBrowserDto){
		String level=null;
		if(null!=medraBrowserDto.getScrollColumn()){
			level=medraBrowserDto.getScrollColumn();
		}else{
			if(null!=medraBrowserDto.getSearchLevel()){
			level=medraBrowserDto.getSearchLevel();
			}
			else if(null!=medraBrowserDto.getSelectLevel()){
				level=medraBrowserDto.getSelectLevel();
			}
		}
		return level;
	}

}
