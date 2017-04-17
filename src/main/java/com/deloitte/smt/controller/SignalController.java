package com.deloitte.smt.controller;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.exception.TopicNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class SignalController {

	@Autowired
	SignalService signalService;

	@PostMapping(value = "/createTopic")
	public String createTopic(@RequestParam(value = "data") String topicString,
							  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
        Topic topic = new ObjectMapper().readValue(topicString, Topic.class);
		return signalService.createTopic(topic, attachments);
	}

	@PostMapping(value = "/updateTopic")
	public ResponseEntity<Void> updateTopic(@RequestParam(value = "data") String topicString,
							  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException, UpdateFailedException {
		Topic topic = new ObjectMapper().readValue(topicString, Topic.class);
		signalService.updateTopic(topic, attachments);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/{topicId}")
    public Topic getTopicById(@PathVariable Long topicId) {
        return signalService.findById(topicId);
    }

	@PutMapping(value = "/{topicId}/validateAndPrioritize")
	public AssessmentPlan validateAndPrioritizeTopic(
	        @PathVariable Long topicId,
			@RequestBody AssessmentPlan assessmentPlan) throws TaskNotFoundException, TopicNotFoundException {
		assessmentPlan = signalService.validateAndPrioritize(topicId, assessmentPlan);
		return assessmentPlan;
	}

	@GetMapping(value = "/all")
	public List<Topic> getAllByStatus(@RequestParam(name = "status", required = false) String statuses,
									  @RequestParam(name = "createdDate", required = false) Date createdDate,
									  @RequestParam(name = "ingredient", required = false) String ingredients,
									  @RequestParam(name = "product", required = false) String products,
									  @RequestParam(name = "license", required = false) String licenses) {

		SearchDto dto = new SearchDto();
		if(StringUtils.isNotBlank(statuses)) {
			dto.setStatuses(Arrays.asList(statuses.split(",")));
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
		if(null != createdDate){
			dto.setCreatedDate(createdDate);
		}
		return signalService.findAllForSearch(dto);
	}

	@GetMapping(value = "/getCounts")
	public String getCounts() {
		return SignalUtil.getCounts(signalService.getValidateAndPrioritizeCount(),signalService.getAssessmentCount(),signalService.getRiskCount());
	}
}
