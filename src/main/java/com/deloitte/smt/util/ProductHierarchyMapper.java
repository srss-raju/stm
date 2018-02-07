package com.deloitte.smt.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.ProductHierarchyDto;

public class ProductHierarchyMapper implements RowMapper<ProductHierarchyDto> {

	@Override
	public ProductHierarchyDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ProductHierarchyDto productHierarchyDto = new ProductHierarchyDto();
		productHierarchyDto.setActLevelOneCode(rs.getString("ATC_LVL_1").trim());
		productHierarchyDto.setActLevelOneDesc(rs.getString("ATC_LVL_1_DESC").trim());
		
		productHierarchyDto.setActLevelTwoCode(rs.getString("ATC_LVL_2").trim());
		productHierarchyDto.setActLevelTwoDesc(rs.getString("ATC_LVL_2_DESC").trim());
		
		productHierarchyDto.setActLevelThreeCode(rs.getString("ATC_LVL_3").trim());
		productHierarchyDto.setActLevelThreeDesc(rs.getString("ATC_LVL_3_DESC").trim());
		
		productHierarchyDto.setActLevelFourCode(rs.getString("ATC_LVL_4").trim());
		productHierarchyDto.setActLevelFourDesc(rs.getString("ATC_LVL_4_DESC").trim());
		
		productHierarchyDto.setActLevelFiveCode(rs.getString("ATC_LVL_5").trim());
		productHierarchyDto.setActLevelFiveDesc(rs.getString("ATC_LVL_5_DESC").trim());
		
		return productHierarchyDto;
	}
	
	
}
