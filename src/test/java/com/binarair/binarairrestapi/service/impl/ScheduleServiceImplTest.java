package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.model.request.ScheduleRequest;
import com.binarair.binarairrestapi.model.response.RoundTripTicketResponse;
import com.binarair.binarairrestapi.model.response.ScheduleResponse;
import com.binarair.binarairrestapi.model.response.TicketResponse;
import com.binarair.binarairrestapi.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private BagageRepository baggageRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    void testFilterFullTwoInvalidRequest() {
        String airport = "CGK.HND";
        String departure = "12-12-2022.24-12-2022";
        String passenger = "1.1.0";
        String serviceClass = "";

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.filterFullTwoSearch(airport, departure, passenger, serviceClass);
        });

    }

    @Test
    void testFilterFullTwoSearchFlightNotAvailable() {
        String airport = "CGK.HND";
        String departure = "12-12-2022.24-12-2022";
        String passenger = "1.1.0";
        String serviceClass = "ECONOMY";

        Mockito.when(scheduleRepository.findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass))
                .thenReturn(List.of());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.filterFullTwoSearch(airport, departure, passenger, serviceClass);
        });

        Mockito.verify(scheduleRepository).findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass);

    }

    @Test
    void testFullTwoSearchReturnNotAvailable() {

        String airport = "CGK.HND";
        String departure = "12-12-2022.24-12-2022";
        String passenger = "1.1.0";
        String serviceClass = "ECONOMY";

        City jakarta = City.builder()
                .name("Jakarta")
                .build();

        City tokyo = City.builder()
                .name("Tokyo")
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Benefit benefit = Benefit.builder()
                .status(true)
                .name("Refundable")
                .description("refundable")
                .build();

        Facility facility = Facility.builder()
                .name("Wifi")
                .status(true)
                .description("wifi")
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Schedule schedule1  = Schedule.builder()
                .id("random-uuid")
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .aircraft(aircraft)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule1);

        List<Bagage> bagageList = new ArrayList<>();

        Mockito.when(scheduleRepository.findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass))
                .thenReturn(scheduleList);
        Mockito.when(baggageRepository.findByAircraftId(aircraft.getId()))
                .thenReturn(bagageList);
        Mockito.when(benefitRepository.findBenefitByAircraftId(aircraft.getId()))
                        .thenReturn(benefitList);
        Mockito.when(facilityRepository.findFacilitiesByAircraftId(aircraft.getId()))
                        .thenReturn(facilityList);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.filterFullTwoSearch(airport, departure, passenger, serviceClass);
        });

        Mockito.verify(scheduleRepository).findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass);
        Mockito.verify(baggageRepository).findByAircraftId(aircraft.getId());
        Mockito.verify(benefitRepository).findBenefitByAircraftId(aircraft.getId());
        Mockito.verify(facilityRepository).findFacilitiesByAircraftId(aircraft.getId());

    }

    @Test
    void testFullTwoSearchSuccess() {
        String airport = "CGK.HND";
        String departure = "12-12-2022.24-12-2022";
        String passenger = "1.1.0";
        String serviceClass = "ECONOMY";

        City jakarta = City.builder()
                .name("Jakarta")
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Benefit benefit = Benefit.builder()
                .status(true)
                .name("Refundable")
                .description("refundable")
                .build();

        Facility facility = Facility.builder()
                .name("Wifi")
                .status(true)
                .description("wifi")
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Schedule schedule1  = Schedule.builder()
                .id("random-uuid")
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .aircraft(aircraft)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule1);

        List<Bagage> bagageList = new ArrayList<>();

        Mockito.when(scheduleRepository.findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass))
                .thenReturn(scheduleList);
        Mockito.when(scheduleRepository.findFullTwoSearch(LocalDate.of(2022, 12, 24), "HND","CGK",serviceClass))
                .thenReturn(scheduleList);
        Mockito.when(baggageRepository.findByAircraftId(aircraft.getId()))
                .thenReturn(bagageList);
        Mockito.when(benefitRepository.findBenefitByAircraftId(aircraft.getId()))
                .thenReturn(benefitList);
        Mockito.when(facilityRepository.findFacilitiesByAircraftId(aircraft.getId()))
                .thenReturn(facilityList);

        RoundTripTicketResponse roundTripTicketResponse = scheduleService.filterFullTwoSearch(airport, departure, passenger, serviceClass);
        Assertions.assertEquals(1, roundTripTicketResponse.getDepartures().size());
        Assertions.assertEquals(1, roundTripTicketResponse.getReturns().size());

        Mockito.verify(scheduleRepository).findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass);
        Mockito.verify(scheduleRepository).findFullTwoSearch(LocalDate.of(2022, 12, 24), "HND","CGK",serviceClass);
        Mockito.verify(baggageRepository, Mockito.times(2)).findByAircraftId(aircraft.getId());
        Mockito.verify(benefitRepository, Mockito.times(2)).findBenefitByAircraftId(aircraft.getId());
        Mockito.verify(facilityRepository, Mockito.times(2)).findFacilitiesByAircraftId(aircraft.getId());

    }

    @Test
    void testFilterFullSearchInvalidRequest() {
        String airport = "CGK.NA";
        String departure = "12-12-2022.24-12-2022";
        String passenger = "1.1.0";
        String serviceClass = "ECONOMY";

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.filterFullSearch(airport, departure, passenger, serviceClass);
        });

    }

    @Test
    void testFilterFullSearchFlightNotAvailable() {
        String airport = "CGK.HND";
        String departure = "12-12-2022.NA";
        String passenger = "1.1.0";
        String serviceClass = "ECONOMY";

        Mockito.when(scheduleRepository.findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass))
                .thenReturn(List.of());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.filterFullSearch(airport, departure, passenger, serviceClass);
        });

        Mockito.verify(scheduleRepository).findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass);

    }

    @Test
    void testFilterFullSearch() {
        String airport = "CGK.HND";
        String departure = "12-12-2022.NA";
        String passenger = "1.1.0";
        String serviceClass = "ECONOMY";

        City jakarta = City.builder()
                .name("Jakarta")
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Benefit benefit = Benefit.builder()
                .status(true)
                .name("Refundable")
                .description("refundable")
                .build();

        Facility facility = Facility.builder()
                .name("Wifi")
                .status(true)
                .description("wifi")
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Schedule schedule1  = Schedule.builder()
                .id("random-uuid")
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .aircraft(aircraft)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule1);

        List<Bagage> bagageList = new ArrayList<>();

        Mockito.when(scheduleRepository.findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass))
                .thenReturn(scheduleList);
        Mockito.when(baggageRepository.findByAircraftId(aircraft.getId()))
                .thenReturn(bagageList);
        Mockito.when(benefitRepository.findBenefitByAircraftId(aircraft.getId()))
                .thenReturn(benefitList);
        Mockito.when(facilityRepository.findFacilitiesByAircraftId(aircraft.getId()))
                .thenReturn(facilityList);

        List<TicketResponse> ticketResponses = scheduleService.filterFullSearch(airport, departure, passenger, serviceClass);
        Assertions.assertEquals(1, ticketResponses.size());
        Mockito.verify(scheduleRepository).findFullTwoSearch(LocalDate.of(2022, 12, 12), "CGK","HND",serviceClass);
        Mockito.verify(baggageRepository).findByAircraftId(aircraft.getId());
        Mockito.verify(benefitRepository).findBenefitByAircraftId(aircraft.getId());
        Mockito.verify(facilityRepository).findFacilitiesByAircraftId(aircraft.getId());

    }

    @Test
    void testSaveScheduleSuccess() {
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .originIataAirportCode("CGK")
                .destinationIataAirportCode("HND")
                .aircraftId("random-aircraft-id")
                .departureDate(LocalDate.now())
                .arrivalDate(LocalDate.now())
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now())
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .build();

        Country indonesia = Country.builder()
                .countryCode("ID")
                .name("Indonesia")
                .build();

        City jakarta = City.builder()
                .name("Jakarta")
                .country(indonesia)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .iataAirportCode("CGK")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .iataAirportCode("HND")
                .city(jakarta)
                .build();

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        List<Bagage> bagageList = new ArrayList<>();

        Mockito.when(airportRepository.findById(scheduleRequest.getOriginIataAirportCode()))
                .thenReturn(Optional.of(originAirport));
        Mockito.when(airportRepository.findById(scheduleRequest.getDestinationIataAirportCode()))
                .thenReturn(Optional.of(destAirport));
        Mockito.when(aircraftRepository.findById(scheduleRequest.getAircraftId()))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(baggageRepository.findByAircraftId(scheduleRequest.getAircraftId()))
                .thenReturn(bagageList);

        ScheduleResponse scheduleResponse = scheduleService.save(scheduleRequest);
        Assertions.assertEquals(scheduleRequest.getAircraftId(), scheduleResponse.getAircraft().getId());
        Assertions.assertEquals(scheduleRequest.getOriginIataAirportCode(), scheduleResponse.getOriginAirport().getIata());
        Assertions.assertEquals(scheduleRequest.getDestinationIataAirportCode(), scheduleResponse.getDestinationAirport().getIata());

        Mockito.verify(airportRepository).findById(scheduleRequest.getOriginIataAirportCode());
        Mockito.verify(airportRepository).findById(scheduleRequest.getDestinationIataAirportCode());
        Mockito.verify(aircraftRepository).findById(scheduleRequest.getAircraftId());
        Mockito.verify(baggageRepository).findByAircraftId(scheduleRequest.getAircraftId());

    }

    @Test
    void testSaveScheduleOriginAirportNotFound() {
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .originIataAirportCode("CGK")
                .destinationIataAirportCode("HND")
                .aircraftId("random-aircraft-id")
                .departureDate(LocalDate.now())
                .arrivalDate(LocalDate.now())
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now())
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .build();

        Mockito.when(airportRepository.findById(scheduleRequest.getOriginIataAirportCode()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.save(scheduleRequest);
        });
        Mockito.verify(airportRepository).findById(scheduleRequest.getOriginIataAirportCode());

    }

    @Test
    void testSaveScheduleDestinationAirportNotFound() {
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .originIataAirportCode("CGK")
                .destinationIataAirportCode("HND")
                .aircraftId("random-aircraft-id")
                .departureDate(LocalDate.now())
                .arrivalDate(LocalDate.now())
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now())
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .build();

        Country indonesia = Country.builder()
                .countryCode("ID")
                .name("Indonesia")
                .build();

        City jakarta = City.builder()
                .name("Jakarta")
                .country(indonesia)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .iataAirportCode("CGK")
                .city(jakarta)
                .build();

        Mockito.when(airportRepository.findById(scheduleRequest.getOriginIataAirportCode()))
                .thenReturn(Optional.of(originAirport));
        Mockito.when(airportRepository.findById(scheduleRequest.getDestinationIataAirportCode()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.save(scheduleRequest);
        });

        Mockito.verify(airportRepository).findById(scheduleRequest.getOriginIataAirportCode());
        Mockito.verify(airportRepository).findById(scheduleRequest.getDestinationIataAirportCode());

    }

    @Test
    void testSaveScheduleAircraftNotFound() {
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .originIataAirportCode("CGK")
                .destinationIataAirportCode("HND")
                .aircraftId("random-aircraft-id")
                .departureDate(LocalDate.now())
                .arrivalDate(LocalDate.now())
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now())
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .build();

        Country indonesia = Country.builder()
                .countryCode("ID")
                .name("Indonesia")
                .build();

        City jakarta = City.builder()
                .name("Jakarta")
                .country(indonesia)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .iataAirportCode("CGK")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .iataAirportCode("HND")
                .city(jakarta)
                .build();

        Mockito.when(airportRepository.findById(scheduleRequest.getOriginIataAirportCode()))
                .thenReturn(Optional.of(originAirport));
        Mockito.when(airportRepository.findById(scheduleRequest.getDestinationIataAirportCode()))
                .thenReturn(Optional.of(destAirport));
        Mockito.when(aircraftRepository.findById(scheduleRequest.getAircraftId()))
                        .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.save(scheduleRequest);
        });

        Mockito.verify(airportRepository).findById(scheduleRequest.getOriginIataAirportCode());
        Mockito.verify(airportRepository).findById(scheduleRequest.getDestinationIataAirportCode());
        Mockito.verify(aircraftRepository).findById(scheduleRequest.getAircraftId());

    }

    @Test
    void testGetAllSchedule() {
        Country indonesia = Country.builder()
                .countryCode("ID")
                .name("Indonesia")
                .build();

        City jakarta = City.builder()
                .name("Jakarta")
                .country(indonesia)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Benefit benefit = Benefit.builder()
                .status(true)
                .name("Refundable")
                .description("refundable")
                .build();

        Facility facility = Facility.builder()
                .name("Wifi")
                .status(true)
                .description("wifi")
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Schedule schedule1  = Schedule.builder()
                .id("random-uuid")
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .aircraft(aircraft)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        List<Bagage> bagageList = new ArrayList<>();

        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule1);

        Mockito.when(scheduleRepository.findAll())
                .thenReturn(scheduleList);
        Mockito.when(baggageRepository.findByAircraftId(schedule1.getAircraft().getId()))
                .thenReturn(bagageList);

        List<ScheduleResponse> scheduleResponses = scheduleService.getAll();
        Assertions.assertNotNull(scheduleResponses);
        Assertions.assertEquals(1, scheduleResponses.size());

        Mockito.verify(scheduleRepository).findAll();
        Mockito.verify(baggageRepository).findByAircraftId(schedule1.getAircraft().getId());

    }

    @Test
    void testDeleteScheduleSuccess() {
        String scheduleId = "random-schedule-id";
        Mockito.when(scheduleRepository.existsById(scheduleId))
                .thenReturn(true);

        Boolean status = scheduleService.delete(scheduleId);
        Assertions.assertTrue(status);

        Mockito.verify(scheduleRepository).existsById(scheduleId);
        Mockito.verify(scheduleRepository).deleteById(scheduleId);

    }

    @Test
    void testDeleteScheduleNotFound() {
        String scheduleId = "random-schedule-id";
        Mockito.when(scheduleRepository.existsById(scheduleId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.delete(scheduleId);
        });

        Mockito.verify(scheduleRepository).existsById(scheduleId);

    }

    @Test
    void testFindScheduleByIdSuccess() {
        Country indonesia = Country.builder()
                .countryCode("ID")
                .name("Indonesia")
                .build();

        City jakarta = City.builder()
                .name("Jakarta")
                .country(indonesia)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Benefit benefit = Benefit.builder()
                .status(true)
                .name("Refundable")
                .description("refundable")
                .build();

        Facility facility = Facility.builder()
                .name("Wifi")
                .status(true)
                .description("wifi")
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Schedule schedule  = Schedule.builder()
                .id("random-uuid")
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .aircraft(aircraft)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        List<Bagage> bagageList = new ArrayList<>();

        Mockito.when(scheduleRepository.findById(schedule.getId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(baggageRepository.findByAircraftId(aircraft.getId()))
                .thenReturn(bagageList);

        ScheduleResponse scheduleResponse = scheduleService.findById(schedule.getId());
        Assertions.assertNotNull(scheduleResponse);;

        Mockito.verify(scheduleRepository).findById(schedule.getId());
        Mockito.verify(baggageRepository).findByAircraftId(aircraft.getId());
    }

    @Test
    void testFindScheduleByIdNotFound() {
        String scheduleId = "random-uuid";

        Mockito.when(scheduleRepository.findById(scheduleId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            scheduleService.findById(scheduleId);
        });

        Mockito.verify(scheduleRepository).findById(scheduleId);

    }

    @Test
    void testConvertToLocalDate() {
        LocalDate localDate = scheduleService.convertToLocalDate("7-12-2022");
        Assertions.assertEquals(localDate.toString(), "2022-12-07");

    }
}