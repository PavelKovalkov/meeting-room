package com.globant.demo.service;

import com.globant.demo.entity.MeetingRoom;
import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class DefaultMeetingRoomCleaningService implements MeetingRoomCleaningService {

    private final Duration basicCleanUpDuration;
    private final Duration cleanUpDurationPerSeat;

    @Override
    public Duration getCleanUpDuration(MeetingRoom meetingRoom) {
        return basicCleanUpDuration.plus(cleanUpDurationPerSeat.multipliedBy(meetingRoom.getSeatsNumber()));
    }
}
