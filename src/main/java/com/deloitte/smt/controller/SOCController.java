package com.deloitte.smt.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.service.SocService;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class SOCController {
	
	private static final Logger LOG = Logger.getLogger(SOCController.class);
	
	@Autowired
	SocService socSevice;
	
	@GetMapping(value="/condition/all")
	public List<Soc> fetchAllSoc(){
		return socSevice.getAllSocs();
	}
	@PostMapping(value="/condition/bySocName")
	public List<Soc> findBySocName(List<String> socNames){
		
		return socSevice.getAllSocsByName(socNames);
	}
}
