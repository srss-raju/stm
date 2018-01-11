package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class SignalController {
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	SignalService signalService;

	@PostMapping(value = "/createTopic")
	public Topic createTopic(@RequestParam(value = "data") String topicString,
							  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws ApplicationException {
        
		Topic topic = null;
        try {
        	topic = new ObjectMapper().readValue(topicString, Topic.class);
		} catch (IOException e) {
			logger.info("Exception occured while creating "+e);
		}
        
		return signalService.createTopic(topic);
	}

	@PostMapping(value = "/updateTopic")
	public ResponseEntity<Void> updateTopic(@RequestParam(value = "data") String topicString) {
		try {
			Topic topic = new ObjectMapper().readValue(topicString, Topic.class);
			signalService.updateTopic(topic);
		} catch (ApplicationException | IOException e) {
			logger.info("Exception occured while updating "+e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/{topicId}")
    public Topic getTopicById(@PathVariable Long topicId) throws ApplicationException {
        return signalService.findById(topicId);
    }

	@PutMapping(value = "/{topicId}/validateAndPrioritize")
	public AssessmentPlan validateAndPrioritizeTopic(
	        @PathVariable Long topicId,
			@RequestBody AssessmentPlan assessmentPlan) throws ApplicationException{
		return signalService.validateAndPrioritize(topicId, assessmentPlan);
	}

	@PostMapping(value = "/template/associateTemplateTasks")
    public List<Task> associateTemplateTasks(@RequestBody AssessmentPlan assessmentPlan) {
        return signalService.associateTemplateTasks(assessmentPlan);
    }
	
	
	@GetMapping(value = "/run/{runInstanceId}")
	public Map<String,Object> findTopicsByRunInstanceId(@PathVariable Long runInstanceId) {
      List<Topic> signals= signalService.findTopicsByRunInstanceId(runInstanceId);
      List<NonSignal> nonSignals=signalService.findNonSignalsByRunInstanceId(runInstanceId);
      Map<String,Object> map=new HashMap<>();
      map.put("SIGNALS",signals);
      map.put("NON_SIGNALS", nonSignals);
      return map;
  }
	
	
	@DeleteMapping(value = "/url/{signalUrlId}")
    public ResponseEntity<Void> deleteSignalURL(@PathVariable Long signalUrlId) throws ApplicationException {
		signalService.deleteSignalURL(signalUrlId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(value = "/updateComments")
	public List<Comments> updateComments(@RequestParam(value = "data") String topicString) {
		List<Comments> list = null;
		try {
			Topic topic = new ObjectMapper().readValue(topicString, Topic.class);
			list = signalService.updateComments(topic);
		} catch (IOException e) {
			logger.info("Exception occured while updating "+e);
		}
		return list;
	}
	
	@GetMapping(value = "/getTopicComments/{topicId}")
	public List<Comments> getTopicComments(@PathVariable Long topicId){
		return signalService.getTopicComments(topicId);
	}
	
	@DeleteMapping(value = "/comments/{commentsId}")
    public ResponseEntity<Void> deleteTopicComments(@PathVariable Long commentsId) throws ApplicationException {
		signalService.deleteTopicComments(commentsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(value = "/dashboard/getCounts")
	public String getDashboardCounts(@RequestBody(required=false) SearchDto dto) {
		return SignalUtil.getCounts(signalService.getValidateAndPrioritizeCount(dto.getOwner(), dto.getUserKeys(), dto.getUserGroupKeys()),signalService.getAssessmentCount(dto.getOwner(), dto.getUserKeys(), dto.getUserGroupKeys()),signalService.getRiskCount(dto.getOwner(), dto.getUserKeys(), dto.getUserGroupKeys()));
	}
	
}
