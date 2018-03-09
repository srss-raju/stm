package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.dto.SocSearchDTO;

public interface SocMedraHierarchyDAO {
	
	List<SocHierarchyDto> findAllByConditionName(List<String> codes,String columnName,int scrollOffset,int scrollCount);
	
	List<SocSearchDTO> findByMatchingSocName(String searchText,int scrollOffset,int scrollCount);
	
	List<SocSearchDTO> findByHlgtName(String searchText,int scrollOffset,int scrollCount);
	
	List<SocSearchDTO> findByHltName(String searchText,int scrollOffset,int scrollCount);
	
	List<SocSearchDTO> findByPtName(String searchText,int scrollOffset,int scrollCount);
	
	List<SocSearchDTO> findByLltName(String searchText,int scrollOffset,int scrollCount);

	List<String> getEventKey(String query);

	List<SocHierarchyDto> findPtsAndLlts(String query);
	
	List<String> getPts(String query);
	
	List<String> getLlts(String query);

	List<SocHierarchyDto> findActLevelsByPtDesc(String ptQueryBuilder);

	List<String> findconditionDescByCode(MedraBrowserDTO medraBrowserDto);

	List<String> findConditionCodesByConditionDesc(List<String> conditionDesc, String conditionDescColumnName, String selectLevel);
	
	
	

}
