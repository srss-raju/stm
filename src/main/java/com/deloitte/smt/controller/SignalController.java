package com.deloitte.smt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;

@RestController
@RequestMapping(value = "/camunda/api/signal")
public class SignalController {

	@Autowired
	SignalService signalService;

	@PostMapping(value = "/createTopic")
	public Topic createTopic(@RequestBody Topic topic) throws ApplicationException {
		return signalService.createTopic(topic);
	}

	@PostMapping(value = "/updateTopic")
	public Topic updateTopic(@RequestBody Topic topic) throws ApplicationException{
		return signalService.updateTopic(topic);
	}

	@GetMapping(value = "/{topicId}")
    public Topic getTopicById(@PathVariable Long topicId) throws ApplicationException {
        return signalService.findById(topicId);
    }

	@PutMapping(value = "/{topicId}/validateAndPrioritize")
	public AssessmentPlan validateAndPrioritizeTopic( @PathVariable Long topicId, @RequestBody AssessmentPlan assessmentPlan) throws ApplicationException{
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
	
	@PostMapping(value = "/updateComments")
	public List<Comments> updateComments(@RequestBody Topic topic) {
		return signalService.updateComments(topic);
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
