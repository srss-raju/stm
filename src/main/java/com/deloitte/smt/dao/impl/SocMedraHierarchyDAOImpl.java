package com.deloitte.smt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.util.PtLltMapper;
import com.deloitte.smt.util.SocHierarchyMapper;
import com.deloitte.smt.util.SocHierarchyMapper2;
import com.deloitte.smt.util.SocSearchMapper;

@Service
public class SocMedraHierarchyDAOImpl implements SocMedraHierarchyDAO {
	
	private String query;
	
	private static final String OFFSET=" offset ";
	
	private static final String SEARCHVALUE="searchText";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<SocHierarchyDto> findAllByConditionName(List<String> codes,String columnName,int scrollOffset,int scrollCount) {
		
			query = "SELECT SOC_CODE,SOC_DESC,HLGT_CODE,HLGT_DESC,HLT_CODE,HLT_DESC,LLT_CODE,LLT_DESC,PT_CODE,PT_DESC,MEDDRA_VERSION_NUMBER,INTERNATIONAL_SOC_ORDER,PRIMARY_SOC_FLAG "
					+ "FROM SOC_MEDDRA_HIERARCHY WHERE "+ columnName+" IN (:code)" +" limit "+ scrollCount+ OFFSET +scrollOffset;
		Map<String, Object> params = new HashMap<>();
		params.put("code", codes);
		return namedParameterJdbcTemplate.query(query, params, new SocHierarchyMapper());
	}

	@Override
	public List<SocSearchDTO> findByMatchingSocName(String searchText,int scrollOffset,int scrollCount) {

		query = " select distinct soc_code,soc_desc from soc_meddra_hierarchy  where soc_desc ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");

		return namedParameterJdbcTemplate.query(query, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByHlgtName(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct hlgt_code,hlgt_desc from soc_meddra_hierarchy  where hlgt_desc ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByHltName(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct hlt_code,hlt_desc from soc_meddra_hierarchy  where hlt_desc ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByPtName(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct pt_code,pt_desc from soc_meddra_hierarchy  where pt_desc ilike lower(:searchText)  limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new SocSearchMapper());
	}

	@Override
	public List<SocSearchDTO> findByLltName(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct llt_code,llt_desc from soc_meddra_hierarchy  where llt_desc ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new SocSearchMapper());
	}
	
	@Override
	public List<String> getEventKey(String query) {
		return jdbcTemplate.queryForList(query, String.class);
	}
	
	@Override
	public List<SocHierarchyDto> findPtsAndLlts(String query) {
		return jdbcTemplate.query(query, new PtLltMapper());
	}

	@Override
	public List<String> getPts(String query) {
		return jdbcTemplate.queryForList(query, String.class);
	}

	@Override
	public List<String> getLlts(String query) {
		return jdbcTemplate.queryForList(query, String.class);
	}
	
	@Override
	public List<SocHierarchyDto> findActLevelsByPtDesc(String ptQueryBuilder) {
		StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT SOC_CODE,SOC_DESC,HLGT_CODE,HLGT_DESC,HLT_CODE,HLT_DESC,PT_CODE,PT_DESC,MEDDRA_VERSION_NUMBER,INTERNATIONAL_SOC_ORDER,PRIMARY_SOC_FLAG FROM SOC_MEDDRA_HIERARCHY WHERE ");
		queryBuilder.append(ptQueryBuilder);
		return jdbcTemplate.query(queryBuilder.toString(), new SocHierarchyMapper2());
	}

	@Override
	public String findconditionDescByCode(MedraBrowserDTO medraBrowserDto) {
		String conditionColumnName = medraBrowserDto.getSelectLevel();
		String conditionDescColumnName = conditionColumnName.replace("CODE","DESC");
		StringBuilder atcDescQuery = new StringBuilder("SELECT distinct ");
		atcDescQuery.append(conditionDescColumnName);
		atcDescQuery.append(" FROM  SOC_MEDDRA_HIERARCHY WHERE ");
		atcDescQuery.append(medraBrowserDto.getSelectLevel()).append("='").append(medraBrowserDto.getSearchValue()).append("'");
		return jdbcTemplate.queryForObject(atcDescQuery.toString(), String.class);
	}

	@Override
	public List<String> findConditionCodesByConditionDesc(String conditionDesc, String conditionDescColumnName, String codeColumnName) {
		StringBuilder atcDescQuery = new StringBuilder("SELECT distinct ").append(codeColumnName);
		atcDescQuery.append(" FROM  SOC_MEDDRA_HIERARCHY WHERE ");
		atcDescQuery.append(conditionDescColumnName).append("='").append(conditionDesc).append("'");
		return jdbcTemplate.queryForList(atcDescQuery.toString(), String.class);
	}

}
