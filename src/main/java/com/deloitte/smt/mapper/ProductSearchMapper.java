package com.deloitte.smt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.ProductSearchDTO;

public class ProductSearchMapper implements RowMapper<ProductSearchDTO> {

	@Override
	public ProductSearchDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ProductSearchDTO productSearchDto=new ProductSearchDTO();
		productSearchDto.setCategoryCode(rs.getString(1).trim());
		productSearchDto.setCategoryDesc(rs.getString(2).trim());
		return productSearchDto;
	}

}
