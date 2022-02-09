package com.globant.demo.service;

import com.globant.demo.entity.MeetingRoomCapability;
import com.globant.demo.resource.MeetingRoomResource;

import java.util.List;
import java.util.Set;

public interface MeetingRoomService {
    List<MeetingRoomResource> findAvailableMeetingRooms(String startDate, String endDate, Integer attendersNumber,
                                                        Set<MeetingRoomCapability> requiredCapabilities);

    void bookMeetingRoom(String roomId, String startDate, String endDate);
}
