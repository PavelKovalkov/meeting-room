package com.globant.demo.service;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomCapability;
import com.globant.demo.entity.MeetingRoomTimeTable;
import com.globant.demo.exception.InvalidMeetingTimeFrameException;
import com.globant.demo.exception.MeetingRoomNotFoundException;
import com.globant.demo.repository.MeetingRoomRepository;
import com.globant.demo.repository.MeetingRoomTimeTableRepository;
import com.globant.demo.resource.MeetingRoomResource;
import com.globant.demo.util.MeetingRoomAssembler;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DefaultMeetingRoomService implements MeetingRoomService {

    private final LocalDateParser localDateParser;
    private final MeetingRoomAvailabilityVerifier meetingRoomAvailabilityVerifier;
    private final MeetingRoomRepository meetingRoomRepository;
    private final MeetingRoomTimeTableRepository meetingRoomTimeTableRepository;

    @Override
    public List<MeetingRoomResource> findAvailableMeetingRooms(String startDate, String endDate, Integer attendersNumber,
                                                               Set<MeetingRoomCapability> requiredCapabilities) {

        Long startDateMillis = localDateParser.toEpochMillis(startDate);
        Long endDateMillis = localDateParser.toEpochMillis(endDate);
        if (startDateMillis >= endDateMillis) {
            throw new InvalidMeetingTimeFrameException("Param 'startDate' should be greater than Param 'endDate'");
        }

        List<MeetingRoom> meetingRooms = meetingRoomRepository.findByAttendersNumberAndCapabilities(attendersNumber, requiredCapabilities);

        return meetingRooms
                .stream()
                .filter(meetingRoom -> meetingRoomAvailabilityVerifier.isRoomAvailable(meetingRoom, startDateMillis, endDateMillis))
                .map(MeetingRoomAssembler::toRoomResource)
                .sorted(Comparator.comparingInt(MeetingRoomResource::getSeatsNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void bookMeetingRoom(String roomId, String startDate, String endDate) {

        Long startDateMillis = localDateParser.toEpochMillis(startDate);
        Long endDateMillis = localDateParser.toEpochMillis(endDate);
        if (startDateMillis >= endDateMillis) {
            throw new InvalidMeetingTimeFrameException("Param 'startDate' should be greater than Param 'endDate'");
        }

        MeetingRoom meetingRoom = meetingRoomRepository
                .findById(roomId)
                .orElseThrow(MeetingRoomNotFoundException::new);

        if (!meetingRoomAvailabilityVerifier.isRoomAvailable(meetingRoom, startDateMillis, endDateMillis)) {
            throw new InvalidMeetingTimeFrameException("Meeting room is already booked for this time");
        }

        MeetingRoomTimeTable meetingRoomTimeTable = MeetingRoomTimeTable
                .builder()
                .id(UUID.randomUUID().toString())
                .meetingRoom(meetingRoom)
                .startDate(startDateMillis)
                .endDate(endDateMillis)
                .build();
        meetingRoomTimeTableRepository.save(meetingRoomTimeTable);
    }

}
