package com.globant.demo.exception;

public class MeetingRoomNotFoundException extends BusinessException {

    public MeetingRoomNotFoundException() {
        super("Meeting room is not found");
    }
}
