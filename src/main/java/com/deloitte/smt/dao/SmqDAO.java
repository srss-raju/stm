package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.SmqDTO;
@FunctionalInterface
public interface SmqDAO {

	List<SmqDTO> findAllSmqs();

}
