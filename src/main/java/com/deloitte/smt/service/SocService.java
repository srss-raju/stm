package com.deloitte.smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.repository.SocRepository;

@Service
public class SocService {
	
	@Autowired
	SocRepository socRepository;
	
	public List<Soc> getAllSocs(){
		return socRepository.findAll();
		
	}
	
	
	public List<Soc> getAllSocsByName(List<String> socs){
		return socRepository.findAllBySocNameIn(socs);
	}
}
