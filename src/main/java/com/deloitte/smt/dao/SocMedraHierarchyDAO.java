package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.dto.SocSearchDTO;

public interface SocMedraHierarchyDAO {
	
	List<SocHierarchyDto> findAllByConditionName(String code,String columnName);
	
	List<SocSearchDTO> findByMatchingSocName(String searchText);
	
	List<SocSearchDTO> findByHlgtName(String searchText);
	
	List<SocSearchDTO> findByHltName(String searchText);
	
	List<SocSearchDTO> findByPtName(String searchText);
	
	List<SocSearchDTO> findByLltName(String searchText);
	
	List<SocSearchDTO> findByAll(String searchText);
	
	

}
