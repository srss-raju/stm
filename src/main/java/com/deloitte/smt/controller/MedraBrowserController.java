package com.deloitte.smt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.service.MedraBrowserService;

@RestController
@RequestMapping("/camunda/api/signal")
public class MedraBrowserController {
	
	@Autowired
	MedraBrowserService medraBrowserService;
	
	@GetMapping(value="/search/{level}")
	public MedraBrowserDTO searchByCondition(@PathVariable String level,@RequestParam(value="searchText") String searchText){
		
		return medraBrowserService.getDetailsBySearchText(level, searchText);
		
	}
	
	

}
