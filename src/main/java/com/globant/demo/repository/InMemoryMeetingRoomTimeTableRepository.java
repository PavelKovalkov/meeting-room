package com.globant.demo.repository;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomTimeTable;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InMemoryMeetingRoomTimeTableRepository implements MeetingRoomTimeTableRepository {

    private final ConcurrentMap<String, MeetingRoomTimeTable> dataStore;

    @Override
    public void save(MeetingRoomTimeTable meetingRoomTimeTable) {
        dataStore.put(meetingRoomTimeTable.getId(), meetingRoomTimeTable);
    }

    @Override
    public List<MeetingRoomTimeTable> findByMeetingRoom(MeetingRoom meetingRoom) {
        return dataStore
                .values()
                .stream()
                .filter(meetingRoomTimeTable -> meetingRoomTimeTable.getMeetingRoom().equals(meetingRoom))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByMeetingRoom(MeetingRoom meetingRoom) {
        dataStore
                .values()
                .stream()
                .filter(meetingRoomTimeTable -> meetingRoomTimeTable.getMeetingRoom().equals(meetingRoom))
                .map(MeetingRoomTimeTable::getId)
                .forEach(dataStore::remove);
    }

}
