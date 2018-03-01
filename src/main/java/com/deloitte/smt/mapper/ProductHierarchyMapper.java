package com.deloitte.smt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.ProductHierarchyDto;

public class ProductHierarchyMapper implements RowMapper<ProductHierarchyDto> {

	@Override
	public ProductHierarchyDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ProductHierarchyDto productHierarchyDto = new ProductHierarchyDto();
		productHierarchyDto.setActLevelOneCode(rs.getString("ATC_LVL_1"));
		productHierarchyDto.setActLevelOneDesc(rs.getString("ATC_LVL_1_DESC"));
		
		productHierarchyDto.setActLevelTwoCode(rs.getString("ATC_LVL_2"));
		productHierarchyDto.setActLevelTwoDesc(rs.getString("ATC_LVL_2_DESC"));
		
		productHierarchyDto.setActLevelThreeCode(rs.getString("ATC_LVL_3"));
		productHierarchyDto.setActLevelThreeDesc(rs.getString("ATC_LVL_3_DESC"));
		
		productHierarchyDto.setActLevelFourCode(rs.getString("ATC_LVL_4"));
		productHierarchyDto.setActLevelFourDesc(rs.getString("ATC_LVL_4_DESC"));
		
		productHierarchyDto.setActLevelFiveCode(rs.getString("ATC_LVL_5"));
		productHierarchyDto.setActLevelFiveDesc(rs.getString("ATC_LVL_5_DESC"));
		
		productHierarchyDto.setActRxNormCode(rs.getString("RXNORM"));
		productHierarchyDto.setRxNormDesc(rs.getString("RXNORM_DESC"));
		
		return productHierarchyDto;
	}
	
	
}
