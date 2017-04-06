package com.deloitte.smt.controller;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ProcessNotFoundException;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.SignalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/signal")
public class SignalController {

	@Autowired
	SignalService signalService;

	@PostMapping(value = "/createTopic")
	public String createTopic(@RequestBody Topic topic,
							  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
		addAttachments(topic, attachments, AttachmentType.TOPIC_ATTACHMENT);
        return signalService.createTopic(topic);
	}
	
	@PostMapping(value = "/validateAndPrioritize")
	public String validateAndPrioritizeTopic(@RequestBody Topic topic,
                                @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws TaskNotFoundException, IOException, ProcessNotFoundException {
        if(topic.getProcessId() == null) {
            throw new ProcessNotFoundException("Process Id not found for the given Topic with Id ["+topic.getId()+"]");
        }
        addAttachments(topic, attachments, AttachmentType.VALIDATE_ATTACHMENT);
        signalService.validateAndPrioritize(topic);
		return "Validation is finished";
	}

	@GetMapping(value = "/all")
	public List<Topic> getAllByStatus(@RequestParam(name = "status", required = false) String statuses,
									  @RequestParam(name = "deleteReason", required = false, defaultValue = "completed") String deleteReason) {
		return signalService.findAllByStatus(statuses, deleteReason);

	}

	@GetMapping(value = "/getCounts")
	public String getCounts() {
		return SignalUtil.getCounts(signalService.getValidateAndPrioritizeCount(),signalService.getAssesmentCount(),signalService.getRiskCount());
	}

	private void addAttachments(Topic topic, MultipartFile[] attachments, AttachmentType attachmentType) throws IOException {
        if(attachments != null) {
            for (MultipartFile attachment : attachments) {
                Attachment a = new Attachment();
                a.setAttachmentType(attachmentType);
                a.setTopic(topic);
                a.setContent(attachment.getBytes());
                a.setFileName(attachment.getOriginalFilename());
                topic.getAttachments().add(a);
            }
        }
    }
	
	@PostMapping(value = "/{topicId}/createAssessmentPlan")
	public String createAssessmentPlan(@PathVariable Long topicId, @RequestBody AssessmentPlan assessmentPlan) {
		signalService.createAssessmentPlan(topicId, assessmentPlan);
		return "Saved Successfully";
	}

	@GetMapping(value = "/allAssessmentPlans")
    public List<AssessmentPlan> getAllAssessmentPlans(@RequestParam(value = "status", defaultValue = "completed") String status){
        return signalService.findAllAssessmentPlansByStatus();
    }

	@PostMapping(value = "/createAssessmentAction")
	public String createAssessmentAction(@RequestBody SignalAction signalAction) {
		signalService.createAssessmentAction(signalAction);
		return "Saved Successfully";
	}
	
	@PostMapping(value = "/{assessmentId}/allAssessmentActions")
	public List<SignalAction> getAllByAssessmentId(@PathVariable String assessmentId,
                                                   @RequestParam(value = "status", defaultValue = "completed") String actionStatus) {
		return signalService.findAllByAssessmentId(assessmentId, actionStatus);
	}
}
