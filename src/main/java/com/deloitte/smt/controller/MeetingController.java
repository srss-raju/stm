package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/meeting")
public class MeetingController {
	
	private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    MeetingService meetingService;

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createMeeting(
            @RequestParam(value = "meetingType", defaultValue = "Signal Meeting") String meetingType,
            @RequestParam("data") String meetingString,
            @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
        Meeting meeting = new ObjectMapper().readValue(meetingString, Meeting.class);
        meeting.setMeetingType(MeetingType.getByDescription(meetingType));
        meetingService.insert(meeting, attachments);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Void> updateMeeting(
            @RequestParam("data") String meetingString,
            @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        try {
        	Meeting meeting = new ObjectMapper().readValue(meetingString, Meeting.class);
			meetingService.update(meeting, attachments);
		} catch (ApplicationException | IOException e) {
			logger.info("Exception occured while updating "+e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping( value = "/byId/{meetingId}")
    public Meeting getMeeting(@PathVariable Long meetingId) {
        return meetingService.findById(meetingId);
    }

    @GetMapping( value = "/{meetingResourceId}")
    public List<Meeting> getAllMeetings(
            @RequestParam(value = "meetingType", defaultValue = "Signal Meeting") String meetingType,
            @PathVariable Long meetingResourceId) {
        return meetingService.findAllyByResourceIdAndMeetingType(meetingResourceId, MeetingType.getByDescription(meetingType));
    }

    @DeleteMapping( value = "/byId/{meetingId}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long meetingId) throws ApplicationException {
        meetingService.delete(meetingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
