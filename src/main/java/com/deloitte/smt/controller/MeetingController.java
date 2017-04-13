package com.deloitte.smt.controller;

import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.MeetingType;
import com.deloitte.smt.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String createMeeting(@RequestParam("data") String meetingString,
                                @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
        Meeting meeting = new ObjectMapper().readValue(meetingString, Meeting.class);
        meeting.setMeetingType(MeetingType.SIGNAL_MEETING);
        meetingService.insert(meeting, attachments);
        return "Saved Successfully";
    }

    @PostMapping(value = "/risk/create")
    public String createMeetingForRisk(@RequestParam("data") String meetingString,
                                @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) throws IOException {
        Meeting meeting = new ObjectMapper().readValue(meetingString, Meeting.class);
        meeting.setMeetingType(MeetingType.RISK_MEETING);
        meetingService.insert(meeting, attachments);
        return "Saved Successfully";
    }

    @GetMapping( value = "/{signalId}")
    public List<Meeting> getAllMeetings(@PathVariable Long signalId) {
        return meetingService.findAllyByResourceIdAndMeetingType(signalId, MeetingType.SIGNAL_MEETING);
    }

    @GetMapping( value = "/risk/{riskId}/")
    public List<Meeting> getAllMeetingsForRisk(@PathVariable Long riskId) {
        return meetingService.findAllyByResourceIdAndMeetingType(riskId, MeetingType.RISK_MEETING);
    }
}
