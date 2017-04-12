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

    @PostMapping(value = "/{signalId}/create")
    public String createMeeting(@PathVariable Long signalId,
                                @RequestParam("data") String meetingString,
                                @RequestParam(value = "attachment", required = false) MultipartFile[] attachments) throws IOException {
        Meeting meeting = new ObjectMapper().readValue(meetingString, Meeting.class);
        meetingService.insert(meeting, attachments);
        return "Saved Successfully";
    }

    @GetMapping( value = "/{signalId}/all")
    public List<Meeting> getAllMeetings(@PathVariable Long signalId) {
        return meetingService.findAllyByResourceIdAndMeetingType(signalId, MeetingType.SIGNAL_MEETING);
    }
}
