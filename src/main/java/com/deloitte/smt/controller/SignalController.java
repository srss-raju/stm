package com.deloitte.smt.controller;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.TaskNotFoundException;
import com.deloitte.smt.repository.TaskInstRepository;
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

	@Autowired
	TaskInstRepository taskInstRepository;

	@PostMapping(value = "/createTopic")
	public String createTopic(@RequestBody Topic topic,
							  @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
		addAttachments(topic, attachments);
        return signalService.createTopic(topic);
	}
	
	@PostMapping(value = "/{processInstanceId}/validate")
	public String validateTopic(@PathVariable String processInstanceId,
                                @RequestBody Topic topic,
                                @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws TaskNotFoundException, IOException {
        addAttachments(topic, attachments);
        signalService.validateTopic(topic, processInstanceId);
		return "Validation is finished";
	}
	
	@PostMapping(value = "/{processInstanceId}/prioritize")
	public String prioritizeTopic(@PathVariable String processInstanceId,
                                  @RequestBody Topic topic) throws TaskNotFoundException {
		signalService.prioritizeTopic(topic, processInstanceId);
		return "finished";
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

	private void addAttachments(Topic topic, MultipartFile[] attachments) throws IOException {
        for(MultipartFile attachment : attachments) {
            Attachment a = new Attachment();
            a.setTopic(topic);
            a.setContent(attachment.getBytes());
            a.setFileName(attachment.getOriginalFilename());
            topic.getAttachments().add(a);
        }
    }
}
