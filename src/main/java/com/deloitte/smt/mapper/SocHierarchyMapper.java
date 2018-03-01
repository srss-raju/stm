package com.deloitte.smt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.SocHierarchyDto;

public class SocHierarchyMapper implements RowMapper<SocHierarchyDto> {

	@Override
	public SocHierarchyDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SocHierarchyDto socHierarchyDto=new SocHierarchyDto();
		socHierarchyDto.setSoc_code(rs.getString("soc_code"));
		socHierarchyDto.setSoc_desc(rs.getString("soc_desc"));
		
		socHierarchyDto.setHlgt_code(rs.getString("hlgt_code"));
		socHierarchyDto.setHlgt_desc(rs.getString("hlgt_desc"));
		
		socHierarchyDto.setHlt_code(rs.getString("hlt_code"));
		socHierarchyDto.setHlt_desc(rs.getString("hlt_desc"));
		
		socHierarchyDto.setLlt_code(rs.getString("llt_code"));
		socHierarchyDto.setLlt_desc(rs.getString("llt_desc"));
		
		socHierarchyDto.setPt_code(rs.getString("pt_code"));
		socHierarchyDto.setPt_desc(rs.getString("pt_desc"));
		
		socHierarchyDto.setMedra_version_number(rs.getString("meddra_version_number"));
		socHierarchyDto.setIntl_soc_order(rs.getLong("international_soc_order"));
		socHierarchyDto.setPrimary_soc_flag(rs.getString("primary_soc_flag"));
		
		
		return socHierarchyDto;
	}
	
	
}
