package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.PtDTO;

@FunctionalInterface
public interface PtDAO {

	List<PtDTO> findPtsBySmqId(List<Integer> ids);

}
