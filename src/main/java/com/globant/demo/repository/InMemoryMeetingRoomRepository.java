package com.globant.demo.repository;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomCapability;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InMemoryMeetingRoomRepository implements MeetingRoomRepository {

    private final ConcurrentMap<String, MeetingRoom> dataStore;

    @Override
    public List<MeetingRoom> findByAttendersNumberAndCapabilities(Integer attendersNumber, Set<MeetingRoomCapability> capabilities) {
        return dataStore
                .values()
                .stream()
                .filter(meetingRoom -> meetingRoom.getSeatsNumber() >= attendersNumber)
                .filter(meetingRoom -> meetingRoom.getCapabilities().containsAll(capabilities))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MeetingRoom> getAll() {
        return new ArrayList<>(dataStore.values());
    }

    @Override
    public Optional<MeetingRoom> findById(String roomId) {
        return Optional.ofNullable(dataStore.get(roomId));
    }
}
