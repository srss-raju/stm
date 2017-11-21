package com.deloitte.smt.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.ConditionResponse;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.service.ConditonService;
import com.deloitte.smt.service.SocHierarchyService;

/**
 * O
 * 
 * @author shbondada
 *
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class MedraBrowserController {
	private static final Logger LOG = Logger.getLogger(MedraBrowserController.class);

	@Autowired
	SocHierarchyService socHierarchyService;
	@Autowired
	ConditonService condtionService;

	
	/**
	 * This method is invoked when soc desc is click in medra browser 
	 * And when condition level is searched by text
	 * 
	 * @param socCode
	 * @param socName
	 * @return
	 */
	@PostMapping(value = "/condition")
	public List<SocSearchDTO> getAllbySocName(@RequestBody MedraBrowserDTO medraBrowserDto) {
		LOG.info("Entry: MedraBrowserController : getAllbySocName() :: hierarchy on level and code ");
		List<SocSearchDTO> socList = null;
		if (null != medraBrowserDto.getSelectLevel()) {
			//This method is invoked for getting Hierarchy by socname
			socList = socHierarchyService.getHierarchyByCode(medraBrowserDto);
		}
		else{
			//This method is invoked for search by text in medra browser
			socList=	socHierarchyService.getDetailsBySearchText(medraBrowserDto);
		}

		LOG.info("Exit: MedraBrowserController : getAllbySocName() :: hierarchy on level and code ");
		return socList;
	}

	@GetMapping(value = "/condition/product")
	public ConditionResponse getAllConditionsLevel() {
		return condtionService.getAllConditionLevels();

	}

}
