package com.globant.demo.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingRoomTimeTable {

    private final String id;
    private final MeetingRoom meetingRoom;

    private final Long startDate;
    private final Long endDate;
}
