package com.deloitte.smt.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.SocHierarchyDto;

public class PtLltMapper implements RowMapper<SocHierarchyDto> {

	@Override
	public SocHierarchyDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SocHierarchyDto socHierarchyDto=new SocHierarchyDto();
		socHierarchyDto.setPt_desc(rs.getString("PT_DESC"));
		socHierarchyDto.setLlt_desc(rs.getString("LLT_DESC"));
		return socHierarchyDto;
	}

}
