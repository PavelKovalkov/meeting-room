package com.globant.demo.service;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomTimeTable;
import com.globant.demo.repository.MeetingRoomTimeTableRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DefaultMeetingRoomAvailabilityVerifier implements MeetingRoomAvailabilityVerifier {

    private final MeetingRoomCleaningService meetingRoomCleaningService;
    private final MeetingRoomTimeTableRepository meetingRoomTimeTableRepository;

    @Override
    public boolean isRoomAvailable(MeetingRoom meetingRoom, Long startDate, Long endDate) {
        List<MeetingRoomTimeTable> meetingRoomTimeTables = meetingRoomTimeTableRepository.findByMeetingRoom(meetingRoom);

        return meetingRoomTimeTables
                .stream()
                .allMatch(meetingRoomTimeTable -> {
                    long cleanUpDuration = meetingRoomCleaningService.getCleanUpDuration(meetingRoom).toMillis();

                    if (startDate < meetingRoomTimeTable.getStartDate()) {
                        return endDate + cleanUpDuration <= meetingRoomTimeTable.getStartDate();

                    } else {
                        return startDate >= meetingRoomTimeTable.getEndDate() + cleanUpDuration;
                    }
                });
    }
}
