package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.MeetingRepository;

/**
 * Created by RajeshKumar on 12-04-2017.
 */
@Transactional
@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    public void insert(Meeting meeting) {
        if(meeting.getId() != null) {
            meeting.setId(null);
        }
        Date d = new Date();
        meeting.setCreatedDate(d);
        meeting.setLastModifiedDate(d);
        meetingRepository.save(meeting);
    }

    public void update(Meeting meeting) throws ApplicationException {
        if(meeting.getId() == null) {
            throw new ApplicationException("Failed to update Meeting. Invalid Id received");
        }
        meeting.setLastModifiedDate(new Date());
        meetingRepository.save(meeting);
    }

    public Meeting findById(Long id) {
        return meetingRepository.findOne(id);
    }

    public List<Meeting> findAllyByResourceIdAndMeetingType(Long resourceId, MeetingType meetingType) {
        return meetingRepository.findAllByMeetingResourceIdAndMeetingType(resourceId, meetingType);
    }

    public void delete(Long meetingId) throws ApplicationException {
        Meeting m = meetingRepository.findOne(meetingId);
        if(m == null) {
            throw new ApplicationException("Meeting not found with the given Id : "+meetingId);
        }
        meetingRepository.delete(m);
    }
}
