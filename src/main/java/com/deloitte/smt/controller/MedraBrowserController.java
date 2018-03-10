package com.deloitte.smt.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.ConditionResponse;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductEventDTO;
import com.deloitte.smt.dto.ProductResponse;
import com.deloitte.smt.dto.ProductSearchDTO;
import com.deloitte.smt.dto.PtLltDTO;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;
import com.deloitte.smt.service.ConditonService;
import com.deloitte.smt.service.ProductHierarchyService;
import com.deloitte.smt.service.ProductService;
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
	@Autowired
	ProductHierarchyService productHierarchyService;
	@Autowired
	ProductService productService;

	
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
		List<SocSearchDTO> socList;
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
	
	@PutMapping(value = "/condition/product")
	public void updateShowCodes(@RequestBody ConditionResponse conditionResponse) {
		condtionService.updateShowCodes(conditionResponse);

	}
	@GetMapping(value = "/product/product")
	public ProductResponse getAllProductsLevel() {
		return productService.getAllProductsLevel();

	}
	@PutMapping(value = "/product/product")
	public void updateShowCodes(@RequestBody ProductResponse productResponse) {
		productService.updateShowCodes(productResponse);

	}
	
	@PostMapping(value = "/product")
	public List<ProductSearchDTO> getAllbyProductName(@RequestBody MedraBrowserDTO medraBrowserDto) {
		LOG.info("Entry: MedraBrowserController : getAllbyProductName() :: hierarchy on level and code ");
		List<ProductSearchDTO> socList;
		if (null != medraBrowserDto.getSelectLevel()) {
			//This method is invoked for getting Hierarchy by ProductName
			socList = productHierarchyService.getHierarchyByCode(medraBrowserDto);
		}
		else{
			//This method is invoked for search by text in medra browser
			socList=	productHierarchyService.getDetailsBySearchText(medraBrowserDto);
		}

		LOG.info("Exit: MedraBrowserController : getAllbySocName() :: hierarchy on level and code ");
		return socList;
	}
	
	@PostMapping(value = "/product/getProductEventKeys")
	public Map<String, Set<Integer>> getProductEventKeys(@RequestBody ProductEventDTO productEventDTO) {
		return productHierarchyService.getProductEventKeys(productEventDTO);

	}
	
	@PostMapping(value = "/product/ingredient")
	public List<TopicProductAssignmentConfiguration> findActLevelsByIngredient(@RequestBody List<String> ingredientNames) {
		return productHierarchyService.findActLevelsByIngredient(ingredientNames);

	}
	
	@PostMapping(value = "/condition/getPtsLlts")
	public PtLltDTO findPtsAndLlts(@RequestBody List<TopicSocAssignmentConfiguration> conditions) {
		return socHierarchyService.findPtsAndLlts(conditions);
	}
	
	@PostMapping(value = "/getProductsByAtcLevels")
	public List<String> findProductByAtcLevels(@RequestBody List<ProductSearchDTO> atcLevels) {
		return productHierarchyService.findProductByAtcLevels(atcLevels);

	}

}
