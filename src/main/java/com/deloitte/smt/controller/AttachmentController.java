package com.deloitte.smt.controller;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 11-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @GetMapping(value = "/{signalId}")
    public List<Attachment> findAllBySignalId(@PathVariable Long signalId) {
        return attachmentService.findByResourceIdAndAttachmentType(signalId, AttachmentType.TOPIC_ATTACHMENT);
    }

    @GetMapping(value = "/assessment/{assessmentId}")
    public List<Attachment> findAllByAssessmentId(@PathVariable Long assessmentId) {
        return attachmentService.findByResourceIdAndAttachmentType(assessmentId, AttachmentType.ASSESSMENT_ATTACHMENT);
    }

    @GetMapping(value = "/assessment/action/{assessmentActionId}")
    public List<Attachment> findAllByAssessmentActionId(@PathVariable Long assessmentActionId) {
        return attachmentService.findByResourceIdAndAttachmentType(assessmentActionId, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT);
    }

    @DeleteMapping(value = "/{attachmentId}")
    public String deleteAttachment(@PathVariable Long attachmentId) throws DeleteFailedException {
        attachmentService.delete(attachmentId);
        return "Successfully Deleted";
    }
}
