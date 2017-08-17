package com.deloitte.smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.PtDAO;
import com.deloitte.smt.dao.SmqDAO;
import com.deloitte.smt.dto.PtDTO;
import com.deloitte.smt.dto.SmqDTO;

@Service
public class SmqService {
	
	@Autowired
    SmqDAO smqDAO;
	
	@Autowired
    PtDAO ptDAO;
	
	public List<SmqDTO> findAllSmqs() {
		return smqDAO.findAllSmqs();
	}
	
	public List<PtDTO> findPtsBySmqId(List<Integer> ids) {
		return ptDAO.findPtsBySmqId(ids);
	}

}
