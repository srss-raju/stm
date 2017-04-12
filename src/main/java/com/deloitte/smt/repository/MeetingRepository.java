package com.deloitte.smt.repository;

import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.MeetingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findAllByMeetingResourceIdMeetingType(Long meetingSourceId, MeetingType meetingType);
}
