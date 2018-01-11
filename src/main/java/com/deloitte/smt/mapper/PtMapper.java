package com.deloitte.smt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.PtDTO;

public class PtMapper implements RowMapper<PtDTO> {
	
	@Override
    public PtDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PtDTO ptDTO = new PtDTO();
		ptDTO.setPtId(rs.getInt("meddra_pt_cui_key"));
		ptDTO.setPtName(rs.getString("pt_name"));
		ptDTO.setSmqId(rs.getInt("smq_cui_key"));
		return ptDTO;
	}
}
