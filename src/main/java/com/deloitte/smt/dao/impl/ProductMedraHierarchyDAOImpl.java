package com.deloitte.smt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.dto.ProductSearchDTO;
import com.deloitte.smt.util.ProductHierarchyMapper;
import com.deloitte.smt.util.ProductSearchMapper;

@Service
public class ProductMedraHierarchyDAOImpl implements ProductMedraHierarchyDAO {
	
	private String query;
	
	private static final String OFFSET=" offset ";
	
	private static final String SEARCHVALUE="searchText";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<ProductHierarchyDto> findAllByActLvel(List<String> codes,String columnName,int scrollOffset,int scrollCount) {
		
			query = "SELECT distinct ATC_LVL_1,ATC_LVL_1_DESC,ATC_LVL_2,ATC_LVL_2_DESC,ATC_LVL_3,ATC_LVL_3_DESC,ATC_LVL_4,ATC_LVL_4_DESC,ATC_LVL_5,ATC_LVL_5_DESC, 'A' as FAMILY_DESC "
					+ "FROM product_meddra_hierarchy WHERE "+ columnName+" IN (:codes)" +" limit "+ scrollCount+ OFFSET +scrollOffset;
		Map<String, Object> params = new HashMap<>();
		params.put("codes", codes);
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

	@Override
	public List<Integer> getProducEventKeys(String query) {
		return jdbcTemplate.queryForList(query, Integer.class);
	}
	
	@Override
	public List<ProductHierarchyDto> findActLevelsByIngredient(List<String> ingredientNames) {
		query = "SELECT ATC_LVL_1,ATC_LVL_1_DESC,ATC_LVL_2,ATC_LVL_2_DESC,ATC_LVL_3,ATC_LVL_3_DESC,ATC_LVL_4,ATC_LVL_4_DESC,ATC_LVL_5,ATC_LVL_5_DESC,RXNORM,RXNORM_DESC,FAMILY_DESC FROM VW_ATC_PROD_DIM WHERE FAMILY_DESC IN (:ingredientNames)" ;
		Map<String, Object> params = new HashMap<>();
		params.put("ingredientNames", ingredientNames);
		return namedParameterJdbcTemplate.query(query, params, new ProductHierarchyMapper());
	}
	
	@Override
	public List<String> getProductKey(String query) {
		return jdbcTemplate.queryForList(query, String.class);
	}
	
	@Override
	public List<ProductHierarchyDto> findActLevelsByProductKey(String productKey) {
		StringBuilder queryBuilder = new StringBuilder("SELECT ATC_LVL_1,ATC_LVL_1_DESC,ATC_LVL_2,ATC_LVL_2_DESC,ATC_LVL_3,ATC_LVL_3_DESC,ATC_LVL_4,ATC_LVL_4_DESC,ATC_LVL_5,ATC_LVL_5_DESC,RXNORM,RXNORM_DESC,FAMILY_DESC FROM VW_ATC_PROD_DIM WHERE PRODUCT_KEY=") ;
		queryBuilder.append("'").append(productKey).append("'");
		return jdbcTemplate.query(queryBuilder.toString(), new ProductHierarchyMapper());
	}

	@Override
	public List<String> findAtcDescByAtcLevelCode(MedraBrowserDTO medraBrowserDto) {
		String atcDescColumnName = medraBrowserDto.getSelectLevel().concat("_desc");
		StringBuilder atcDescQuery = new StringBuilder("SELECT distinct ");
		atcDescQuery.append(atcDescColumnName);
		atcDescQuery.append(" FROM  product_meddra_hierarchy WHERE ");
		atcDescQuery.append(medraBrowserDto.getSelectLevel()).append("='").append(medraBrowserDto.getSearchValue()).append("'");
		return jdbcTemplate.queryForList(atcDescQuery.toString(), String.class);
	}
	
	@Override
	public List<String> findAtcCodesByAtcLevelDesc(List<String> desc, String descColumnName, String codeColumnName) {
		
		StringBuilder atcDescQuery = new StringBuilder("SELECT distinct ").append(codeColumnName);
		atcDescQuery.append(" FROM  product_meddra_hierarchy WHERE ");
		atcDescQuery.append(descColumnName).append(" IN (:codes)");
		Map<String, Object> params = new HashMap<>();
		params.put("codes", desc);
		return namedParameterJdbcTemplate.queryForList(atcDescQuery.toString(), params, String.class);
	}
	
}
