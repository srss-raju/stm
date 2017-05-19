package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.exception.TopicNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class SignalController {

	@Autowired
	SignalService signalService;

	@PostMapping(value = "/createTopic")
	public Topic createTopic(@RequestParam(value = "data") String topicString,
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
    public Topic getTopicById(@PathVariable Long topicId) throws EntityNotFoundException {
        return signalService.findById(topicId);
    }

	@PutMapping(value = "/{topicId}/validateAndPrioritize")
	public AssessmentPlan validateAndPrioritizeTopic(
	        @PathVariable Long topicId,
			@RequestBody AssessmentPlan assessmentPlan) throws TaskNotFoundException, TopicNotFoundException {
		assessmentPlan = signalService.validateAndPrioritize(topicId, assessmentPlan);
		return assessmentPlan;
	}

	

	@PostMapping(value = "/search")	
	public List<Topic> getAllByStatus(@RequestBody(required=false) SearchDto dto) {
		return signalService.findTopics(dto);
	}

	@GetMapping(value = "/getCounts")
	public String getCounts(@RequestParam(value = "ingredient", required = false) String ingredient, 
							@RequestParam(value = "assignTo", required = false) String assignTo) {
		if(StringUtils.isNotBlank(ingredient)){
			return signalService.getCountsByFilter(ingredient, assignTo);
		}
		return SignalUtil.getCounts(signalService.getValidateAndPrioritizeCount(assignTo),signalService.getAssessmentCount(assignTo),signalService.getRiskCount(assignTo));
	}
	
	@GetMapping(value = "/getTemplates/{ingrediantName}")
	public List<TaskTemplate> getTaskTamplatesOfIngrediant(@PathVariable String ingrediantName){
		return signalService.getTaskTamplatesOfIngrediant(ingrediantName);
	}
	
	@PostMapping(value = "/template/associateTemplateTasks")
    public List<SignalAction> associateTemplateTasks(@RequestBody AssessmentPlan assessmentPlan) {
        return signalService.associateTemplateTasks(assessmentPlan);
    }
	
	@GetMapping(value = "/run/{runInstanceId}")
    public List<Topic> findTopicsByRunInstanceId(@PathVariable Long runInstanceId) {
        return signalService.findTopicsByRunInstanceId(runInstanceId);
    }
	
	@DeleteMapping(value = "/url/{signalUrlId}")
    public ResponseEntity<Void> deleteSignalURL(@PathVariable Long signalUrlId) throws EntityNotFoundException {
		signalService.deleteSignalURL(signalUrlId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
}
