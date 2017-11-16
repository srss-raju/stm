package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.service.SocHierarchyService;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * O
 * @author shbondada
 *
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class MedraBrowserController {
	private static final Logger LOG = Logger.getLogger(MedraBrowserController.class);
	
	@Autowired
	SocHierarchyService socHierarchyService;
	
	
	/**
	 * This method is invoked search by keyword in medra browser
	 * @param level
	 * @param searchText
	 * @return
	 */
	@PostMapping(value="/condition")
	public List<SocSearchDTO> searchByCondition(@RequestParam(value = "data") String searchBy){
		LOG.info("Entry : MedraBrowserController : searchByCondition() ::  search by the text and level");
		MedraBrowserDTO medraBrowserDto;
		 List<SocSearchDTO> socSearchList= new ArrayList<>();
		try {
			medraBrowserDto = new ObjectMapper().readValue(searchBy, MedraBrowserDTO.class);
			
			socSearchList =	socHierarchyService.getDetailsBySearchText(medraBrowserDto.getSearchLevel(),medraBrowserDto.getSearchValue());
			}
			catch (IOException e) {
				LOG.info("Exception occured while updating "+e);
			}
		LOG.info("Exit : MedraBrowserController : searchByCondition() ::  search by the text and level");
	return	socSearchList;
	}
	
	
	
	/**
	 * This method is invoked when soc desc is cliked in medra browser
	 * @param socCode
	 * @param socName
	 * @return
	 */
	@PostMapping(value="/condition/byConditionName")
	public List<SocSearchDTO> getAllbySocName(@RequestParam(value = "data") String searchBy){
		LOG.info("Entry: MedraBrowserController : getAllbySocName() :: hierarchy on level and code ");
		 MedraBrowserDTO medraBrowserDto;
		 List<SocSearchDTO> socHierarchyList= new ArrayList<>();
		try {
			medraBrowserDto = new ObjectMapper().readValue(searchBy, MedraBrowserDTO.class);
			
			socHierarchyList =	socHierarchyService.getHierarchyByCode(medraBrowserDto);
		} catch (IOException e) {
			LOG.info("Exception occured while updating "+e);
		}
		LOG.info("Exit: MedraBrowserController : getAllbySocName() :: hierarchy on level and code ");
		return socHierarchyList;
	}

}
