package com.deloitte.smt.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.ProductFieldsDTO;

public class ProductFieldsMapper implements RowMapper<ProductFieldsDTO> {

	@Override
	public ProductFieldsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ProductFieldsDTO productFieldsDTO = new ProductFieldsDTO();
		productFieldsDTO.setProductName(rs.getString("d_product_name_display"));
		productFieldsDTO.setProductKey(rs.getString("product_key"));
		productFieldsDTO.setIngredientName(rs.getString("family_desc"));
		return productFieldsDTO;
	}
	
}
