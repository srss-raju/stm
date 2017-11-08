package com.deloitte.smt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.service.MedraBrowserService;

@RestController
@RequestMapping("/camunda/api/signal")
public class MedraBrowserController {
	
	@Autowired
	MedraBrowserService medraBrowserService;
	
	@PostMapping(value="/search/{level}/{searchText}")
	public MedraBrowserDTO searchByCondition(@PathVariable String level,@PathVariable String searchText){
		
		return medraBrowserService.getDetailsBySearchText(level, searchText);
		
	}
	
	

}
