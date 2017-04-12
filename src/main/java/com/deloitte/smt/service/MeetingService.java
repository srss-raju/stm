package com.deloitte.smt.service;

import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.MeetingType;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    AttachmentService attachmentService;

    public void insert(Meeting meeting, MultipartFile[] attachments) throws IOException {
        if(meeting.getId() != null) {
            meeting.setId(null);
        }
        meeting = meetingRepository.save(meeting);
        attachmentService.addAttachments(meeting.getId(), attachments, AttachmentType.MEETING_ATTACHMENT);
    }

    public void update(Meeting meeting, MultipartFile[] attachments) throws UpdateFailedException {
        if(meeting.getId() == null) {
            throw new UpdateFailedException("Failed to update Meeting. Invalid Id received");
        }
        meetingRepository.save(meeting);
    }

    public Meeting findById(Long id) {
        return meetingRepository.findOne(id);
    }

    public List<Meeting> findAllyByResourceIdAndMeetingType(Long resourceId, MeetingType meetingType) {
        return meetingRepository.findAllByMeetingResourceIdMeetingType(resourceId, meetingType);
    }
}
