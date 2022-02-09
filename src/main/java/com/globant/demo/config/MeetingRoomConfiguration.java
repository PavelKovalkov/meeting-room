package com.globant.demo.config;

import com.globant.demo.entity.MeetingRoom;
import com.globant.demo.entity.MeetingRoomCapability;
import com.globant.demo.repository.InMemoryMeetingRoomRepository;
import com.globant.demo.repository.InMemoryMeetingRoomTimeTableRepository;
import com.globant.demo.repository.MeetingRoomRepository;
import com.globant.demo.repository.MeetingRoomTimeTableRepository;
import com.globant.demo.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MeetingRoomConfiguration {

    @Bean
    public LocalDateParser meetingRoomLocalDateParser() {
        return new MeetingRoomLocalDateParser();
    }

    @Bean
    public MeetingRoomAvailabilityVerifier meetingRoomAvailabilityVerifier(MeetingRoomTimeTableRepository meetingRoomTimeTableRepository,
                                                                           MeetingRoomCleaningService meetingRoomCleaningService) {
        return new DefaultMeetingRoomAvailabilityVerifier(meetingRoomCleaningService, meetingRoomTimeTableRepository);
    }

    @Bean
    public MeetingRoomTimeTableRepository inMemoryMeetingRoomTimeTableRepository() {
        return new InMemoryMeetingRoomTimeTableRepository(new ConcurrentHashMap<>());
    }

    @Bean
    public MeetingRoomRepository inMemoryMeetingRoomRepository() {
        MeetingRoom firstRoom = new MeetingRoom(UUID.randomUUID().toString(), "firstRoom", 5, Collections.singleton(MeetingRoomCapability.MULTIMEDIA));
        MeetingRoom secondRoom = new MeetingRoom(UUID.randomUUID().toString(), "secondRoom", 7, Collections.emptySet());

        ConcurrentHashMap<String, MeetingRoom> dataStore = new ConcurrentHashMap<>();
        dataStore.put(firstRoom.getId(), firstRoom);
        dataStore.put(secondRoom.getId(), secondRoom);

        return new InMemoryMeetingRoomRepository(dataStore);
    }

    @Bean
    public MeetingRoomCleaningService meetingRoomCleaningService(@Value("${cleaning.duration.basic.minutes}") Integer basicCleaningDurationMinutes,
                                                                 @Value("${cleaning.duration.per.seat.minutes}") Integer cleanUpDurationPerSeatMinutes) {
        return new DefaultMeetingRoomCleaningService(Duration.ofMinutes(basicCleaningDurationMinutes),
                Duration.ofMinutes(cleanUpDurationPerSeatMinutes));
    }

    @Bean
    public MeetingRoomService meetingRoomService(MeetingRoomRepository meetingRoomRepository,
                                                 LocalDateParser meetingRoomLocalDateParser,
                                                 MeetingRoomTimeTableRepository meetingRoomTimeTableRepository,
                                                 MeetingRoomAvailabilityVerifier meetingRoomAvailabilityVerifier) {
        return new DefaultMeetingRoomService(meetingRoomLocalDateParser, meetingRoomAvailabilityVerifier,
                meetingRoomRepository, meetingRoomTimeTableRepository);
    }

}
