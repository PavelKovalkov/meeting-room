package com.globant.demo.service

import com.globant.demo.entity.MeetingRoom
import com.globant.demo.entity.MeetingRoomCapability
import com.globant.demo.exception.InvalidMeetingTimeFrameException
import com.globant.demo.exception.MeetingRoomNotFoundException
import com.globant.demo.repository.MeetingRoomRepository
import com.globant.demo.repository.MeetingRoomTimeTableRepository
import spock.lang.Shared
import spock.lang.Specification

class DefaultMeetingRoomServiceTest extends Specification {

    @Shared
    def startDate = 'someStartDate'
    @Shared
    def endDate = 'endStartDate'
    @Shared
    def attendersNumber = 3
    @Shared
    def capabilities = [MeetingRoomCapability.MULTIMEDIA] as Set

    def localDateParser = Mock(LocalDateParser)
    def meetingRoomAvailabilityVerifier = Mock(MeetingRoomAvailabilityVerifier)
    def meetingRoomRepository = Mock(MeetingRoomRepository)
    def meetingRoomTimeTableRepository = Mock(MeetingRoomTimeTableRepository)

    /**
     * Service under test
     */
    def sut = new DefaultMeetingRoomService(localDateParser, meetingRoomAvailabilityVerifier, meetingRoomRepository, meetingRoomTimeTableRepository)

    def "findAvailableMeetingRooms method throws exception if startDate is not greater than endDate"() {
        given:
            def endDateMillis = 123L
            localDateParser.toEpochMillis(endDate) >> endDateMillis
            localDateParser.toEpochMillis(startDate) >> endDateMillis + 1

        when:
            sut.findAvailableMeetingRooms(startDate, endDate, attendersNumber, capabilities)

        then:
            thrown(InvalidMeetingTimeFrameException)
    }

    def "findAvailableMeetingRooms method returns available meeting rooms sorted by allocation efficiency"() {
        given:
            def startDateMillis = 123L
            def endDateMillis = 1234L
            localDateParser.toEpochMillis(startDate) >> startDateMillis
            localDateParser.toEpochMillis(endDate) >> endDateMillis
        and: 'prepare available room with the most efficient allocation'
            def availableRoomId1 = 'availableRoomId1'
            def availableRoomName1 = 'availableRoomName1'
            def availableRoomSeatsNumber1 = attendersNumber
            def availableRoomCapabilities1 = [MeetingRoomCapability.MULTIMEDIA] as Set
            def availableRoom1 = new MeetingRoom(id: availableRoomId1, name: availableRoomName1, seatsNumber: availableRoomSeatsNumber1,
                    capabilities: availableRoomCapabilities1)
            meetingRoomAvailabilityVerifier.isRoomAvailable(availableRoom1, startDateMillis, endDateMillis) >> true
        and: 'prepare available room'
            def availableRoomId2 = 'availableRoomId2'
            def availableRoomName2 = 'availableRoomName2'
            def availableRoomSeatsNumber2 = 7
            def availableRoomCapabilities2 = [MeetingRoomCapability.MULTIMEDIA] as Set
            def availableRoom2 = new MeetingRoom(id: availableRoomId2, name: availableRoomName2, seatsNumber: availableRoomSeatsNumber2,
                    capabilities: availableRoomCapabilities2)
            meetingRoomAvailabilityVerifier.isRoomAvailable(availableRoom2, startDateMillis, endDateMillis) >> true
        and: 'prepare unavailable room'
            def unavailableRoomId = 'unavailableRoomId'
            def unavailableRoomName = 'unavailableRoomName'
            def unavailableRoomSeatsNumber = 4
            def unavailableRoomCapabilities = [MeetingRoomCapability.MULTIMEDIA] as Set
            def unavailableRoom = new MeetingRoom(id: unavailableRoomId, name: unavailableRoomName, seatsNumber: unavailableRoomSeatsNumber,
                    capabilities: unavailableRoomCapabilities)
            meetingRoomAvailabilityVerifier.isRoomAvailable(unavailableRoom, startDateMillis, endDateMillis) >> false
        and:
            meetingRoomRepository.findByAttendersNumberAndCapabilities(attendersNumber, capabilities) >> [availableRoom1, availableRoom2, unavailableRoom]

        when:
            def foundRooms = sut.findAvailableMeetingRooms(startDate, endDate, attendersNumber, capabilities)

        then:
            foundRooms.size() == 2
        and:
            with(foundRooms[0]) {
                getId() == availableRoomId1
                getName() == availableRoomName1
                getCapabilities() == availableRoomCapabilities1
                getSeatsNumber() == availableRoomSeatsNumber1
            }
        and:
            with(foundRooms[1]) {
                getId() == availableRoomId2
                getName() == availableRoomName2
                getCapabilities() == availableRoomCapabilities2
                getSeatsNumber() == availableRoomSeatsNumber2
            }
    }

    def "bookMeetingRoom method throws exception if startDate is not greater than endDate"() {
        given:
            def endDateMillis = 123L
            localDateParser.toEpochMillis(endDate) >> endDateMillis
            localDateParser.toEpochMillis(startDate) >> endDateMillis + 1

        when:
            sut.bookMeetingRoom('id', startDate, endDate)

        then:
            def ex = thrown(InvalidMeetingTimeFrameException)
            ex.message == "Param 'startDate' should be greater than Param 'endDate'"
    }

    def "bookMeetingRoom method throws exception if meeting room is not found by id"() {
        given:
            def roomId = 'id'
            def startDateMillis = 123L
            def endDateMillis = 1234L
            localDateParser.toEpochMillis(startDate) >> startDateMillis
            localDateParser.toEpochMillis(endDate) >> endDateMillis
        and:
            meetingRoomRepository.findById(roomId) >> Optional.empty()

        when:
            sut.bookMeetingRoom(roomId, startDate, endDate)

        then:
            thrown(MeetingRoomNotFoundException)
    }

    def "bookMeetingRoom method throws exception if meeting room is unavailable for the provided time"() {
        given:
            def roomId = 'id'
            def startDateMillis = 123L
            def endDateMillis = 1234L
            localDateParser.toEpochMillis(startDate) >> startDateMillis
            localDateParser.toEpochMillis(endDate) >> endDateMillis
        and:
            def unavailableMeetingRoom = Mock(MeetingRoom)
            meetingRoomRepository.findById(roomId) >> Optional.of(unavailableMeetingRoom)
        and:
            meetingRoomAvailabilityVerifier.isRoomAvailable(unavailableMeetingRoom, startDateMillis, endDateMillis) >> false

        when:
            sut.bookMeetingRoom(roomId, startDate, endDate)

        then:
            def ex = thrown(InvalidMeetingTimeFrameException)
            ex.message == "Meeting room is already booked for this time"
    }


    def "bookMeetingRoom method saves new time table if the room is available for the provided time"() {
        given:
            def roomId = 'id'
            def startDateMillis = 123L
            def endDateMillis = 1234L
            localDateParser.toEpochMillis(startDate) >> startDateMillis
            localDateParser.toEpochMillis(endDate) >> endDateMillis
        and:
            def availableMeetingRoom = Mock(MeetingRoom)
            meetingRoomRepository.findById(roomId) >> Optional.of(availableMeetingRoom)
        and:
            meetingRoomAvailabilityVerifier.isRoomAvailable(availableMeetingRoom, startDateMillis, endDateMillis) >> true

        when:
            sut.bookMeetingRoom(roomId, startDate, endDate)

        then:
            1 * meetingRoomTimeTableRepository.save({
                it.id != null
                it.meetingRoom == availableMeetingRoom
                it.startDate == startDateMillis
                it.endDate == endDateMillis
            })
    }
}
