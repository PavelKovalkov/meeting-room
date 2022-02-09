package com.globant.demo.entity;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MeetingRoom {

    private String id;
    private String name;
    private int seatsNumber;
    private Set<MeetingRoomCapability> capabilities;
}
