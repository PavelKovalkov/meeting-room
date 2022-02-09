package com.globant.demo.service;

import com.globant.demo.entity.MeetingRoom;

public interface MeetingRoomAvailabilityVerifier {

    boolean isRoomAvailable(MeetingRoom meetingRoom, Long startDate, Long endDate);
}
