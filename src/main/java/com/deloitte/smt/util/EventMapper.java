package com.deloitte.smt.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.deloitte.smt.dto.DetectionRunDTO;

public class EventMapper implements RowMapper<DetectionRunDTO> {

	@Override
	public DetectionRunDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DetectionRunDTO detectionRunDTO=new DetectionRunDTO();
		detectionRunDTO.setPrimaryEventKey(rs.getString(""));
		return detectionRunDTO;
	}

}
