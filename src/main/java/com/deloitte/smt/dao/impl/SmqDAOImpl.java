package com.deloitte.smt.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.deloitte.smt.dao.SmqDAO;
import com.deloitte.smt.dto.SmqDTO;
import com.deloitte.smt.util.SmqMapper;

public class SmqDAOImpl implements SmqDAO{

private JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<SmqDTO> findAllSmqs() {
    	return jdbcTemplate.query("SELECT distinct smq_cui_key, smq_name, valid_start_date, valid_end_date FROM smq_meddra_mapping order by smq_cui_key",new SmqMapper());
    } 
}
