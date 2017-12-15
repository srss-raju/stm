package com.deloitte.smt.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.service.FiltersService;
import com.deloitte.smt.util.ServerResponseObject;
/**
 * 
 * @author jshaik
 *
 */
@RestController
@RequestMapping("/camunda/api/signal")
public class FilterController    {
	private static final Logger LOGGER = Logger.getLogger(FilterController.class);
    @Autowired
    private FiltersService  filterService;

    @GetMapping(value="/filter/{type}")
    public List<FilterDTO> getFilters(@PathVariable String type){
    	LOGGER.info("getFilters type...."+type);
		return filterService.getFiltersByType(type);
    }

    @PostMapping(value="/filter/{type}/search")
    public ServerResponseObject getSignalDataByFilter(@PathVariable String type,@RequestBody SearchCriteriaDTO searchCriteria){
		return filterService.getSignalDataByFilter(type,searchCriteria);
    }
}
