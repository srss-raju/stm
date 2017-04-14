package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Drug;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.exception.TopicNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	public ResponseEntity<Void> validateAndPrioritizeTopic(
	        @PathVariable Long topicId,
			@RequestBody AssessmentPlan assessmentPlan) throws TaskNotFoundException, TopicNotFoundException {
		signalService.validateAndPrioritize(topicId, assessmentPlan);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/all")
	public List<Topic> getAllByStatus(@RequestParam(name = "status", required = false) String statuses,
									  @RequestParam(name = "deleteReason", required = false) String deleteReason,
									  @RequestParam(name = "createdDate", required = false) Date createdDate,
									  @RequestParam(name = "ingredient", required = false) String ingredient,
									  @RequestParam(name = "product", required = false) String product,
									  @RequestParam(name = "license", required = false) String license) {
		Topic t = null;
		if(createdDate != null || ingredient != null || product != null || license != null) {
			t = new Topic();
			t.setStartDate(null);
			t.setDrug(new Drug());
			if (createdDate != null) {
				t.setCreatedDate(createdDate);
			}
			if (ingredient != null) {
				t.getDrug().setIngredient(ingredient);
			}
			if (product != null) {
				t.getDrug().setProduct(product);
			}
			if (license != null) {
				t.getDrug().setLicense(license);
			}
		}
		return signalService.findAllByStatus(statuses, deleteReason, t);
	}

	@GetMapping(value = "/getCounts")
	public String getCounts() {
		return SignalUtil.getCounts(signalService.getValidateAndPrioritizeCount(),signalService.getAssessmentCount(),signalService.getRiskCount());
	}
}
