package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.service.SocService;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class SOCController {
	
	@Autowired
	SocService socSevice;
	
	@GetMapping(value="/condition/all")
	public List<Soc> fetchAllSoc(){
		return socSevice.getAllSocs();
	}
	@PostMapping(value="/condition/bySocName/{socName}")
	public List<Soc> findBySocName(@PathVariable String socName){
		
		return socSevice.getAllSocsByName(socName);
	}
}
