package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.SmqDTO;

public interface SmqDAO {

	List<SmqDTO> findAllSmqs();

}
