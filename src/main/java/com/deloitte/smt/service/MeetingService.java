package com.deloitte.smt.service;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Transactional
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
        Date d = new Date();
        meeting.setCreatedDate(d);
        meeting.setLastModifiedDate(d);
        Meeting meetingUpdated = meetingRepository.save(meeting);
        attachmentService.addAttachments(meetingUpdated.getId(), attachments, AttachmentType.MEETING_ATTACHMENT, null, meetingUpdated.getFileMetadata());
    }

    public void update(Meeting meeting, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(meeting.getId() == null) {
            throw new UpdateFailedException("Failed to update Meeting. Invalid Id received");
        }
        meeting.setLastModifiedDate(new Date());
        attachmentService.addAttachments(meeting.getId(), attachments, AttachmentType.MEETING_ATTACHMENT, meeting.getDeletedAttachmentIds(), meeting.getFileMetadata());
        meetingRepository.save(meeting);
    }

    public Meeting findById(Long id) {
        return meetingRepository.findOne(id);
    }

    public List<Meeting> findAllyByResourceIdAndMeetingType(Long resourceId, MeetingType meetingType) {
        return meetingRepository.findAllByMeetingResourceIdAndMeetingType(resourceId, meetingType);
    }

    public void delete(Long meetingId) throws DeleteFailedException {
        Meeting m = meetingRepository.findOne(meetingId);
        if(m == null) {
            throw new DeleteFailedException("Meeting not found with the given Id : "+meetingId);
        }
        meetingRepository.delete(m);
    }
}
