package com.deloitte.smt.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.SmqDTO;

public class SmqMapper implements RowMapper<SmqDTO> {
	
	@Override
    public SmqDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		SmqDTO smqDTO = new SmqDTO();
		smqDTO.setSmqId(rs.getInt("smq_cui_key"));
		smqDTO.setSmqName(rs.getString("smq_name"));
		smqDTO.setValidEndDate(rs.getDate("valid_end_date"));
		smqDTO.setValidStartDate(rs.getDate("valid_start_date"));
		return smqDTO;
	}

}
