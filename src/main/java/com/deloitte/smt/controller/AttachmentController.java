package com.deloitte.smt.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.AttachmentService;

/**
 * Created by myelleswarapu on 11-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/attachment")
public class AttachmentController {
	
	private final Logger logger = LogManager.getLogger(this.getClass());

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
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws ApplicationException {
        attachmentService.delete(attachmentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping(value = "/risk/{riskId}")
    public List<Attachment> findAllByRiskId(@PathVariable Long riskId) {
        return attachmentService.findByResourceIdAndAttachmentType(riskId, AttachmentType.RISK_ASSESSMENT);
    }
    
    @GetMapping(value = "/risk/task/{riskTaskId}")
    public List<Attachment> findAllByRiskTaskId(@PathVariable Long riskTaskId) {
        return attachmentService.findByResourceIdAndAttachmentType(riskTaskId, AttachmentType.RISK_TASK_ASSESSMENT);
    }

    @GetMapping(value = "/meeting/{meetingId}")
    public List<Attachment> findAllByMeetingId(@PathVariable Long meetingId) {
        return attachmentService.findByResourceIdAndAttachmentType(meetingId, AttachmentType.MEETING_ATTACHMENT);
    }


    @GetMapping(value = "/content/{attachmentId}")
    public void downloadContent(@PathVariable Long attachmentId,
                                HttpServletResponse response) {
        try {
        	Attachment a = attachmentService.findById(attachmentId);
            response.setContentType(a.getContentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + a.getFileName());
			IOUtils.copyLarge(new ByteArrayInputStream(a.getContent()), response.getOutputStream());
		} catch (IOException | ApplicationException e) {
			logger.info("Exception occured while downloadContent "+e);
		}
    }
}
