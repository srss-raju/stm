package com.deloitte.smt.controller;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SignalDetectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/camunda/api/signal/detect")
public class SignalDetectionController {
	
	@Autowired
	SignalDetectionService signalDetectionService;

	@PostMapping(value = "/createSignalDetection")
	public SignalDetection  createSignalDetection(@RequestParam(value = "data") String topicString) throws IOException {
		SignalDetection signalDetection = new ObjectMapper().readValue(topicString, SignalDetection.class);
		return signalDetectionService.createOrUpdateSignalDetection(signalDetection);
	}
	
	@GetMapping(value = "/{signalDetectionId}")
    public SignalDetection getSignalDetectionById(@PathVariable Long signalDetectionId) throws EntityNotFoundException {
        return signalDetectionService.findById(signalDetectionId);
    }
	
	@PostMapping(value = "/updateSignalDetection")
	public ResponseEntity<Void> updateTopic(@RequestParam(value = "data") String topicString) throws IOException, UpdateFailedException {
		SignalDetection signalDetection = new ObjectMapper().readValue(topicString, SignalDetection.class);
		signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{signalDetectionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long signalDetectionId) throws DeleteFailedException {
		signalDetectionService.delete(signalDetectionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(value = "/all")
	public List<SignalDetection> getAllByStatus(@RequestParam(name = "socs", required = false) String socs,
									  @RequestParam(name = "ingredient", required = false) String ingredients,
									  @RequestParam(name = "product", required = false) String products,
									  @RequestParam(name = "license", required = false) String licenses,
									  @RequestParam(name = "hlgts", required = false) String hlgts,
									  @RequestParam(name = "hlts", required = false) String hlts,
									  @RequestParam(name = "pts", required = false) String pts,
												@RequestParam(name = "description", required = false) String description) {

		SearchDto dto = new SearchDto();
		if(StringUtils.isNotBlank(socs)) {
			dto.setSocs(Arrays.asList(socs.split(",")));
		}
		if(StringUtils.isNotBlank(ingredients)) {
			dto.setIngredients(Arrays.asList(ingredients.split(",")));
		}
		if(StringUtils.isNotBlank(products)) {
			dto.setProducts(Arrays.asList(products.split(",")));
		}
		if(StringUtils.isNotBlank(licenses)) {
			dto.setLicenses(Arrays.asList(licenses.split(",")));
		}
		if(StringUtils.isNotBlank(hlgts)) {
			dto.setHlgts(Arrays.asList(hlgts.split(",")));
		}
		if(StringUtils.isNotBlank(hlts)) {
			dto.setHlts(Arrays.asList(hlts.split(",")));
		}
		if(StringUtils.isNotBlank(pts)) {
			dto.setPts(Arrays.asList(pts.split(",")));
		}
		if(StringUtils.isNotBlank(description)) {
			dto.setDescription(description);
		}
		return signalDetectionService.findAllForSearch(dto);
	}

}
