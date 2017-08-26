package com.deloitte.smt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.deloitte.smt.dao.PtDAO;
import com.deloitte.smt.dto.PtDTO;
import com.deloitte.smt.util.PtMapper;

public class PtDAOImpl implements PtDAO{

private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

	@Override
	public List<PtDTO> findPtsBySmqId(List<Integer> ids) {
		String query = "SELECT smq_cui_key, meddra_pt_cui_key, pt_name FROM pfizer.smq_meddra_mapping where smq_cui_key IN (:ids) order by smq_cui_key";
		Map<String, Object> params = new HashMap<>();
		params.put("ids", ids);
		return namedParameterJdbcTemplate.query(query, params, new PtMapper());
	}
}
