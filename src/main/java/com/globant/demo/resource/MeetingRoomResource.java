package com.globant.demo.resource;

import com.globant.demo.entity.MeetingRoomCapability;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class MeetingRoomResource {

    private final String id;
    private final String name;
    private final int seatsNumber;
    private final Set<MeetingRoomCapability> capabilities;
}
