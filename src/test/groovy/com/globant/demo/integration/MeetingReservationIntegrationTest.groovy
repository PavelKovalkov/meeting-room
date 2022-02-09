package com.globant.demo.integration

import com.globant.demo.entity.MeetingRoomCapability
import com.globant.demo.repository.MeetingRoomRepository
import com.globant.demo.repository.MeetingRoomTimeTableRepository
import com.globant.demo.service.LocalDateParser
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MeetingReservationIntegrationTest extends SpringBootSpecification {

    @Autowired
    LocalDateParser localDateParser
    @Autowired
    MeetingRoomTimeTableRepository meetingRoomTimeTableRepository
    @Autowired
    MeetingRoomRepository meetingRoomRepository

    def "book a room for the specified time"() {
        given:
            def startDate = '20221010_12:59:59'
            def endDate = '20221010_13:58:00'
            def attenders = 5
            def capability = MeetingRoomCapability.MULTIMEDIA.toString()
        and:
            def availableRoomsResponse = mvc.perform(
                    get("/meeting/rooms?startDate=${startDate}&endDate=${endDate}&attenders=${attenders}&capabilities=${capability}")
                            .accept('application/json'))
                    .andReturn()
                    .getResponse()
        and:
            def jsonSlurper = new JsonSlurper()
            def responseBodyJson = jsonSlurper.parseText(availableRoomsResponse.getContentAsString())
            def availableRoomId = responseBodyJson[0]['id']
        and:
            def meetingRoom = meetingRoomRepository.findById(availableRoomId).get()

        when:
            def bookAvailableRoomResponse = mvc.perform(
                    post("/meeting/rooms/${availableRoomId}?startDate=${startDate}&endDate=${endDate}")
                            .accept('application/json'))

        then:
            bookAvailableRoomResponse.andExpect(status().isOk())
        and:
            def timeTables = meetingRoomTimeTableRepository.findByMeetingRoom(meetingRoom)
            timeTables.size() == 1
        and:
            with(timeTables[0]) {
                getStartDate() == localDateParser.toEpochMillis(startDate)
                getEndDate() == localDateParser.toEpochMillis(endDate)
            }
    }

    def "cannot book room if it is already booked for the specified time"() {
        given:
            def startDate = '20221010_12:59:59'
            def endDate = '20221010_13:58:00'
            def attenders = 5
            def capability = MeetingRoomCapability.MULTIMEDIA.toString()
        and:
            def availableRoomsResponse = mvc.perform(
                    get("/meeting/rooms?startDate=${startDate}&endDate=${endDate}&attenders=${attenders}&capabilities=${capability}")
                            .accept('application/json'))
                    .andReturn()
                    .getResponse()
        and:
            def jsonSlurper = new JsonSlurper()
            def responseBodyJson = jsonSlurper.parseText(availableRoomsResponse.getContentAsString())
            def availableRoomId = responseBodyJson[0]['id']

        and:
            def bookAvailableRoomResponse = mvc.perform(
                    post("/meeting/rooms/${availableRoomId}?startDate=${startDate}&endDate=${endDate}")
                            .accept('application/json'))

        expect: 'first book room request passes'
            bookAvailableRoomResponse.andExpect(status().isOk())

        when: 'try to book room for the same time'
            def bookUnavailableRoomResponse = mvc.perform(
                    post("/meeting/rooms/${availableRoomId}?startDate=${startDate}&endDate=${endDate}")
                            .accept('application/json'))
        then: 'the second request fails as the room is already booked'
            bookUnavailableRoomResponse.andExpect(status().isBadRequest())
    }

    def cleanup() {
        meetingRoomRepository
                .getAll()
                .each {
                    meetingRoomTimeTableRepository.deleteAllByMeetingRoom(it)
                }
    }

}