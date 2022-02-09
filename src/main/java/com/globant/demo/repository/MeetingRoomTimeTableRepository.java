package com.globant.demo.repository;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomTimeTable;

import java.util.List;

public interface MeetingRoomTimeTableRepository {

    void save(MeetingRoomTimeTable meetingRoomTimeTable);

    List<MeetingRoomTimeTable> findByMeetingRoom(MeetingRoom meetingRoom);

    void deleteAllByMeetingRoom(MeetingRoom meetingRoom);
}
