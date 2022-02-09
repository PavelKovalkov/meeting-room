package com.globant.demo.controller;

import com.globant.demo.entity.MeetingRoomCapability;
import com.globant.demo.resource.MeetingRoomResource;
import com.globant.demo.service.MeetingRoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("meeting/rooms")
@AllArgsConstructor
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    @GetMapping
    public ResponseEntity<List<MeetingRoomResource>> findAvailableMeetingRooms(@RequestParam("startDate") String startDate,
                                                                               @RequestParam("endDate") String endDate,
                                                                               @RequestParam("attenders") Integer attendersNumber,
                                                                               @RequestParam(value = "capabilities", defaultValue = "") Set<MeetingRoomCapability> meetingRoomCapabilities) {
        List<MeetingRoomResource> availableMeetingRooms =
                meetingRoomService.findAvailableMeetingRooms(startDate, endDate, attendersNumber, meetingRoomCapabilities);
        return ResponseEntity.ok(availableMeetingRooms);
    }

    @PostMapping("{roomId}")
    public ResponseEntity<Void> reserveMeetingRoom(@PathVariable("roomId") String roomId,
                                                   @RequestParam("startDate") String startDate,
                                                   @RequestParam("endDate") String endDate) {
        meetingRoomService.bookMeetingRoom(roomId, startDate, endDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

