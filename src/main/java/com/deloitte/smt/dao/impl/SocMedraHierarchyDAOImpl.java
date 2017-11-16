package com.deloitte.smt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.util.SocHierarchyMapper;
import com.deloitte.smt.util.SocSearchMapper;

@Service
public class SocMedraHierarchyDAOImpl implements SocMedraHierarchyDAO {
	
	private String QUERY="query";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<SocHierarchyDto> findAllByConditionName(String code,String columnName) {
		QUERY = "SELECT SOC_CODE,SOC_DESC,HLGT_CODE,HLGT_DESC,HLT_CODE,HLT_DESC,LLT_CODE,LLT_DESC,PT_CODE,PT_DESC,MEDDRA_VERSION_NUMBER,INTERNATIONAL_SOC_ORDER,PRIMARY_SOC_FLAG "
				+ "FROM SOC_MEDDRA_HIERARCHY WHERE "+ columnName+" =:code";
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		return namedParameterJdbcTemplate.query(QUERY, params, new SocHierarchyMapper());
	}

	@Override
	public List<SocSearchDTO> findByMatchingSocName(String searchText) {

		QUERY = " select distinct soc_code,soc_desc from soc_meddra_hierarchy  where soc_desc ilike lower(:searchText)";

		Map<String, Object> params = new HashMap<>();
		params.put("searchText", "%" + searchText + "%");

		return namedParameterJdbcTemplate.query(QUERY, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByHlgtName(String searchText) {
		QUERY = " select distinct hlgt_code,hlgt_desc from soc_meddra_hierarchy  where hlgt_desc ilike lower(:searchText)";

		Map<String, Object> params = new HashMap<>();
		params.put("searchText", "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(QUERY, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByHltName(String searchText) {
		QUERY = " select distinct hlt_code,hlt_desc from soc_meddra_hierarchy  where hlt_desc ilike lower(:searchText)";

		Map<String, Object> params = new HashMap<>();
		params.put("searchText", "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(QUERY, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByPtName(String searchText) {
		QUERY = " select distinct pt_code,pt_desc from soc_meddra_hierarchy  where pt_desc ilike lower(:searchText)";

		Map<String, Object> params = new HashMap<>();
		params.put("searchText", "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(QUERY, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByLltName(String searchText) {
		QUERY = " select distinct llt_code,llt_desc from soc_meddra_hierarchy  where llt_desc ilike lower(:searchText)";

		Map<String, Object> params = new HashMap<>();
		params.put("searchText", "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(QUERY, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByAll(String searchText) {
	
	return null;
	}

}
