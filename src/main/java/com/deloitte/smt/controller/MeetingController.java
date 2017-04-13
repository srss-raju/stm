package com.deloitte.smt.controller;

import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.MeetingType;
import com.deloitte.smt.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/meeting")
public class MeetingController {

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

    @GetMapping( value = "/{meetingResourceId}")
    public List<Meeting> getAllMeetings(
            @RequestParam(value = "meetingType", defaultValue = "Signal Meeting") String meetingType,
            @PathVariable Long meetingResourceId) {
        return meetingService.findAllyByResourceIdAndMeetingType(meetingResourceId, MeetingType.getByDescription(meetingType));
    }
}
