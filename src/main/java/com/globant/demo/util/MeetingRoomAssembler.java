package com.globant.demo.util;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.resource.MeetingRoomResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetingRoomAssembler {

    public static MeetingRoomResource toRoomResource(MeetingRoom meetingRoom) {
        if (meetingRoom == null) {
            return null;
        }

        return new MeetingRoomResource(meetingRoom.getId(), meetingRoom.getName(), meetingRoom.getSeatsNumber(),
                meetingRoom.getCapabilities());
    }
}
