package com.deloitte.smt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.dto.ProductSearchDTO;
import com.deloitte.smt.mapper.ProductHierarchyMapper;
import com.deloitte.smt.mapper.ProductSearchMapper;

@Service
public class ProductMedraHierarchyDAOImpl implements ProductMedraHierarchyDAO {
	
	private String query;
	
	private static final String OFFSET=" offset ";
	
	private static final String SEARCHVALUE="searchText";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<ProductHierarchyDto> findAllByActLvel(String code,String columnName,int scrollOffset,int scrollCount) {
		
			query = "SELECT ATC_LVL_1,ATC_LVL_1_DESC,ATC_LVL_2,ATC_LVL_2_DESC,ATC_LVL_3,ATC_LVL_3_DESC,ATC_LVL_4,ATC_LVL_4_DESC,ATC_LVL_5,ATC_LVL_5_DESC,RXNORM, RXNORM_DESC "
					+ "FROM product_meddra_hierarchy WHERE "+ columnName+" =:code" +" limit "+ scrollCount+ OFFSET +scrollOffset;
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		return namedParameterJdbcTemplate.query(query, params, new ProductHierarchyMapper());
	}

	@Override
	public List<ProductSearchDTO> findByActLvelOne(String searchText,int scrollOffset,int scrollCount) {

		query = " select distinct ATC_LVL_1,ATC_LVL_1_DESC from PRODUCT_MEDDRA_HIERARCHY  where ATC_LVL_1_DESC ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");

		return namedParameterJdbcTemplate.query(query, params, new ProductSearchMapper());
	}

	@Override
	public List<ProductSearchDTO> findByActLvelTwo(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct ATC_LVL_2,ATC_LVL_2_DESC from PRODUCT_MEDDRA_HIERARCHY  where ATC_LVL_2_DESC ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new ProductSearchMapper());
	}

	@Override
	public List<ProductSearchDTO> findByActLvelThree(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct ATC_LVL_3,ATC_LVL_3_DESC from PRODUCT_MEDDRA_HIERARCHY  where ATC_LVL_3_DESC ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new ProductSearchMapper());
	}

	@Override
	public List<ProductSearchDTO> findByActLvelFour(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct ATC_LVL_4,ATC_LVL_4_DESC from PRODUCT_MEDDRA_HIERARCHY  where ATC_LVL_4_DESC ilike lower(:searchText)  limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new ProductSearchMapper());
	}

	@Override
	public List<ProductSearchDTO> findByActLvelFive(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct ATC_LVL_5,ATC_LVL_5_DESC from PRODUCT_MEDDRA_HIERARCHY  where ATC_LVL_5_DESC ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new ProductSearchMapper());
	}
	
	@Override
	public List<ProductSearchDTO> findByRxNorm(String searchText,int scrollOffset,int scrollCount) {
		query = " select distinct RXNORM, RXNORM_DESC from PRODUCT_MEDDRA_HIERARCHY  where RXNORM_DESC ilike lower(:searchText) limit "+ scrollCount+ OFFSET +scrollOffset;

		Map<String, Object> params = new HashMap<>();
		params.put(SEARCHVALUE, "%" + searchText + "%");
		return namedParameterJdbcTemplate.query(query, params, new ProductSearchMapper());
	}


}
