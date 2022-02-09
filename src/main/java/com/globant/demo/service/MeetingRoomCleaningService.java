package com.globant.demo.service;

import com.globant.demo.entity.MeetingRoom;

import java.time.Duration;

public interface MeetingRoomCleaningService {

    Duration getCleanUpDuration(MeetingRoom meetingRoom);
}
