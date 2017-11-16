package com.deloitte.smt.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.SocSearchDTO;

public class SocSearchMapper implements RowMapper<SocSearchDTO> {

	@Override
	public SocSearchDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SocSearchDTO socSearchDto=new SocSearchDTO();
		socSearchDto.setCategoryCode(rs.getString(1));
		socSearchDto.setCategoryDesc(rs.getString(2));
		return socSearchDto;
	}

}
