package com.deloitte.smt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.SmtDAO;

@Service
public class SmtService {
	
	@Autowired
	SmtDAO smtDAO;

	public String cleanupData() {
		return smtDAO.deleteData();
	}

}
