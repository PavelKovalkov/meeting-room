package com.globant.demo.repository;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomCapability;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingRoomRepository {

    List<MeetingRoom> findByAttendersNumberAndCapabilities(Integer attendersNumber, Set<MeetingRoomCapability> capabilities);

    Collection<MeetingRoom> getAll();

    Optional<MeetingRoom> findById(String roomId);
}
