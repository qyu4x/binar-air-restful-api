package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.NotificationConfiguration;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.exception.ValidationException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.model.request.*;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.repository.*;
import com.binarair.binarairrestapi.service.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingDetailServiceImplTest {

    @Mock
    private BookingDetailRepository bookingDetailRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AircraftSeatRepository aircraftSeatRepository;

    @Mock
    private SeatScheduleBookingRepository seatScheduleBookingRepository;

    @Mock
    private TitelRepository titelRepository;

    @Mock
    private AgeCategoryRepository ageCategoryRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private BagageRepository baggageRepository;

    @Mock
    private NotificationConfiguration notificationConfiguration;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingDetailServiceImpl bookingDetailService;


    @Test
    void testCreateBookingSuccess() {
        String userId = "random-user-id";

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingPassengerRequest passenger = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
                .build();

        List<BookingPassengerRequest> bookingPassengerRequests = new ArrayList<>();
        bookingPassengerRequests.add(passenger);

        BookingRequest bookingRequest = BookingRequest.builder()
                .data(bookingPassengerRequests)
                .build();

        BookingDetailRequest bookingDetail = BookingDetailRequest.builder()
                .amount(BigDecimal.valueOf(7000000L))
                .bookingType("One way")
                .adult(1)
                .child(0)
                .infant(0)
                .departures(bookingRequest)
                .returns(bookingRequest)
                .build();

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .adult(bookingDetail.getAdult())
                .child(bookingDetail.getChild())
                .infant(bookingDetail.getInfant())
                .user(user)
                .bookingType(bookingDetail.getBookingType())
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenReturn(booking);

        Booking bookingResponse = bookingDetailService.createBooking(bookingDetail, userId);
        Assertions.assertNotNull(bookingResponse);

        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(bookingRepository).save(ArgumentMatchers.any(Booking.class));

    }

    @Test
    void testCreateBookingInvalidNumberOfPassangerOnceTrip() {
        String userId = "random-user-id";

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingPassengerRequest passenger = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
                .build();

        List<BookingPassengerRequest> bookingPassengerRequests = new ArrayList<>();
        bookingPassengerRequests.add(passenger);

        BookingRequest bookingRequest = BookingRequest.builder()
                .data(bookingPassengerRequests)
                .build();

        BookingDetailRequest bookingDetail = BookingDetailRequest.builder()
                .amount(BigDecimal.valueOf(7000000L))
                .bookingType("One way")
                .adult(1)
                .child(1)
                .infant(0)
                .departures(bookingRequest)
                .returns(bookingRequest)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            bookingDetailService.createBooking(bookingDetail, userId);
        });
    }

    @Test
    void testCreateBookingInvalidNumberOfPassengerRoundTrip() {
        String userId = "random-user-id";

        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingPassengerRequest passenger = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
                .build();

        List<BookingPassengerRequest> bookingPassengerRequests = new ArrayList<>();
        bookingPassengerRequests.add(passenger);
        bookingPassengerRequests.add(passenger);

        BookingRequest bookingRequest = BookingRequest.builder()
                .data(bookingPassengerRequests)
                .build();

        BookingDetailRequest bookingDetail = BookingDetailRequest.builder()
                .amount(BigDecimal.valueOf(7000000L))
                .bookingType("Round Trip")
                .adult(1)
                .child(0)
                .infant(0)
                .departures(bookingRequest)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            bookingDetailService.createBooking(bookingDetail, userId);
        });

    }

    @Test
    void testInsertSeatBookingSeatHasBeenBooked() {
        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("random-booking-aircraft-seat")
                .build();

        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .aircraftSeat(bookingAircraftSeatRequest)
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
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

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(seatScheduleBooking);

        Assertions.assertThrows(ValidationException.class, () -> {
            bookingDetailService.insertSeatBooking(passengerRequest.getAircraftSeat(), passengerRequest.getScheduleId());
        });

        Mockito.verify(seatScheduleBookingRepository).checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId());

    }

    @Test
    void testInsertSeatBookingAircraftSeatNotFound() {
        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("random-booking-aircraft-seat")
                .build();

        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .aircraftSeat(bookingAircraftSeatRequest)
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
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

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(null);
        Mockito.when(aircraftSeatRepository.findById(bookingAircraftSeatRequest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bookingDetailService.insertSeatBooking(passengerRequest.getAircraftSeat(), passengerRequest.getScheduleId());
        });

        Mockito.verify(seatScheduleBookingRepository).checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId());
        Mockito.verify(aircraftSeatRepository).findById(bookingAircraftSeatRequest.getId());

    }

    @Test
    void testInsertSeatBookingScheduleNotFound() {
        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("random-booking-aircraft-seat")
                .build();

        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .aircraftSeat(bookingAircraftSeatRequest)
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
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

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(null);
        Mockito.when(aircraftSeatRepository.findById(bookingAircraftSeatRequest.getId()))
                .thenReturn(Optional.of(aircraftSeat));
        Mockito.when(scheduleRepository.findById(passengerRequest.getScheduleId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bookingDetailService.insertSeatBooking(passengerRequest.getAircraftSeat(), passengerRequest.getScheduleId());
        });

        Mockito.verify(seatScheduleBookingRepository).checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId());
        Mockito.verify(aircraftSeatRepository).findById(bookingAircraftSeatRequest.getId());
        Mockito.verify(scheduleRepository).findById(passengerRequest.getScheduleId());

    }

    @Test
    void testInsertSeatBookingSucess() {
        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("random-booking-aircraft-seat")
                .build();

        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .aircraftSeat(bookingAircraftSeatRequest)
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
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

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(null);
        Mockito.when(aircraftSeatRepository.findById(bookingAircraftSeatRequest.getId()))
                .thenReturn(Optional.of(aircraftSeat));
        Mockito.when(scheduleRepository.findById(passengerRequest.getScheduleId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(seatScheduleBookingRepository.save(ArgumentMatchers.any(SeatScheduleBooking.class)))
                .thenReturn(seatScheduleBooking);

        AircraftSeatResponse insertAircraftSeatResponse = bookingDetailService.insertSeatBooking(passengerRequest.getAircraftSeat(), passengerRequest.getScheduleId());
        Assertions.assertNotNull(insertAircraftSeatResponse);

        Mockito.verify(seatScheduleBookingRepository).checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId());
        Mockito.verify(aircraftSeatRepository).findById(bookingAircraftSeatRequest.getId());
        Mockito.verify(scheduleRepository).findById(passengerRequest.getScheduleId());
        Mockito.verify(seatScheduleBookingRepository).save(ArgumentMatchers.any(SeatScheduleBooking.class));

    }

    @Test
    void testFindBaggageByScheduleIdSuccess() {
        String scheduleId = "random-chedule-id";
        Integer baggageweight = 20;

        Airlines airlines = Airlines.builder()
                .name("Neko Air")
                .logoURL("https://nekoair.com?media=jpg")
                .description("neko air nya")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .aircraftManufacture(aircraftManufacture)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        Bagage baggage = Bagage.builder()
                .id("random-baggage-id")
                .weight(20)
                .freeBagageCapacity(0)
                .freeCabinBagageCapacity(20)
                .price(BigDecimal.valueOf(350000L))
                .build();

        Mockito.when(scheduleRepository.findById(scheduleId))
                .thenReturn(Optional.of(schedule));
        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(aircraft.getId(), baggageweight))
                .thenReturn(baggage);

        ProcessBaggageResponse baggageeResponse = bookingDetailService.findBaggageByScheduleId(scheduleId, baggageweight);
        Assertions.assertNotNull(baggageeResponse);

        Mockito.verify(scheduleRepository).findById(scheduleId);
        Mockito.verify(baggageRepository).findByAircraftIdAndBaggageWeight(aircraft.getId(), baggageweight);

    }

    @Test
    void testFindBaggageByScheduleIdNotFOund() {
        String scheduleId = "random-chedule-id";
        Integer baggageweight = 20;

        Mockito.when(scheduleRepository.findById(scheduleId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bookingDetailService.findBaggageByScheduleId(scheduleId, baggageweight);
        });

        Mockito.verify(scheduleRepository).findById(scheduleId);

    }

    // test insert seat booking & find baggage by schedule id before
    @Test
    void testInsertPassengerSuccess() {
        String userId = "random-user-id";
        Integer baggageweight = 20;

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();

        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("Adult")
                .description("14+")
                .build();
        Passenger passenger = Passenger.builder()
                .id("random-ps-id")
                .titel(titel)
                .cityzenship(country)
                .issuingCountry(country)
                .ageCategory(ageCategory)
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .passportNumber("9134801830913")
                .build();

        BookingDetail bookingDetail1 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        BookingDetail bookingDetail2 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .seatPrice(BigDecimal.valueOf(600000L))
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("return")
                .build();

        List<BookingDetail> bookingDetails = new ArrayList<>();
        bookingDetails.add(bookingDetail1);
        bookingDetails.add(bookingDetail2);

        Bagage baggage = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("A1")
                .build();
        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .aircraftSeat(bookingAircraftSeatRequest)
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
                .build();

        Passenger passenger1 = Passenger.builder()
                .id("random-ps-id")
                .titel(titel)
                .cityzenship(country)
                .issuingCountry(country)
                .ageCategory(ageCategory)
                .firstName(passengerRequest.getFirstName())
                .lastName(passengerRequest.getLastName())
                .passportNumber(passengerRequest.getPassportNumber())
                .build();

        List<Passenger> passengerList = new ArrayList<>();
        passengerList.add(passenger1);

        List<BookingPassengerRequest> bookingPassengerRequests = new ArrayList<>();
        bookingPassengerRequests.add(passengerRequest);

        BookingRequest bookingRequest = BookingRequest.builder()
                .data(bookingPassengerRequests)
                .build();

        BookingDetailRequest bookingDetailRequest = BookingDetailRequest.builder()
                .amount(BigDecimal.valueOf(7000000L))
                .bookingType("Round Trip")
                .adult(1)
                .child(0)
                .infant(0)
                .departures(bookingRequest)
                .returns(bookingRequest)
                .build();

        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(aircraft.getId(), baggageweight))
                .thenReturn(baggage);
        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(null);
        Mockito.when(aircraftSeatRepository.findById(bookingAircraftSeatRequest.getId()))
                .thenReturn(Optional.of(aircraftSeat));
        Mockito.when(scheduleRepository.findById(passengerRequest.getScheduleId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(seatScheduleBookingRepository.save(ArgumentMatchers.any(SeatScheduleBooking.class)))
                .thenReturn(seatScheduleBooking);
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(ageCategoryRepository.findById(passengerRequest.getAgeCategoryId()))
                .thenReturn(Optional.of(ageCategory));
        Mockito.when(countryRepository.findById(passengerRequest.getCitizenshipId()))
                .thenReturn(Optional.of(country));
        Mockito.when(countryRepository.findById(passengerRequest.getIssuingCountryId()))
                .thenReturn(Optional.of(country));
        Mockito.when(passengerRepository.saveAll(Mockito.any(List.class)))
                .thenReturn(passengerList);
        Mockito.when(titelRepository.findById(passengerRequest.getTitelId()))
                .thenReturn(Optional.of(titel));

        List<ProcessPassengerResponse> processPassengerResponses = bookingDetailService.insertPassenger(bookingDetailRequest, userId);
        Assertions.assertNotNull(processPassengerResponses);

        Mockito.verify(baggageRepository, Mockito.times(2)).findByAircraftIdAndBaggageWeight(aircraft.getId(), baggageweight);
        Mockito.verify(seatScheduleBookingRepository, Mockito.times(2)).checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId());
        Mockito.verify(aircraftSeatRepository, Mockito.times(2)).findById(bookingAircraftSeatRequest.getId());
        Mockito.verify(scheduleRepository, Mockito.times(4)).findById(passengerRequest.getScheduleId());
        Mockito.verify(seatScheduleBookingRepository, Mockito.times(2)).save(ArgumentMatchers.any(SeatScheduleBooking.class));
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(ageCategoryRepository, Mockito.times(2)).findById(passengerRequest.getAgeCategoryId());
        Mockito.verify(countryRepository, Mockito.times(2)).findById(passengerRequest.getCitizenshipId());
        Mockito.verify(countryRepository, Mockito.times(2)).findById(passengerRequest.getIssuingCountryId());
        Mockito.verify(passengerRepository).saveAll(Mockito.any(List.class));
        Mockito.verify(titelRepository, Mockito.times(2)).findById(passengerRequest.getTitelId());

    }

    @Test
    void testSaveBookingDetailSuccess() {

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        Passenger passenger = Passenger.builder()
                .id("random-passenger-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .cityzenship(country)
                .issuingCountry(country)
                .build();

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();


        ProcessBaggageResponse processBaggageResponse = ProcessBaggageResponse.builder()
                .price(BigDecimal.valueOf(350000L))
                .extraBagage(30)
                .build();
        AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                .seatCode("A1")
                .price(PriceResponse.builder()
                        .amount(BigDecimal.valueOf(4000000L))
                        .build())
                .build();

        ProcessPassengerResponse processPassengerResponse = ProcessPassengerResponse.builder()
                .id("random-process-passenger-id")
                .status("departure")
                .scheduleId("random-schedule-id")
                .titel(titel)
                .baggageResponse(processBaggageResponse)
                .seatResponse(aircraftSeatResponse)
                .build();

        User user = User.builder()
                .id("random-user-id")
                .fullName("Kaguya shinomiya")
                .build();

        Booking booking = Booking.builder()
                .user(user)
                .adult(1)
                .child(0)
                .infant(0)
                .build();

        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .booking(booking)
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        Mockito.when(scheduleRepository.findById(processPassengerResponse.getScheduleId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(passengerRepository.findById(processPassengerResponse.getId()))
                .thenReturn(Optional.of(passenger));
        Mockito.when(bookingDetailRepository.save(ArgumentMatchers.any(BookingDetail.class)))
                .thenReturn(bookingDetail);

        BookingDetail bookingDetailResponse = bookingDetailService.saveBookingDetail(processPassengerResponse, booking);
        Assertions.assertNotNull(bookingDetailResponse);

        Mockito.verify(scheduleRepository).findById(processPassengerResponse.getScheduleId());
        Mockito.verify(passengerRepository).findById(processPassengerResponse.getId());
        Mockito.verify(bookingDetailRepository).save(ArgumentMatchers.any(BookingDetail.class));

    }

    @Test
    void testBookingDetailScheduleNotFound() {
        Booking booking = Booking.builder()
                .adult(1)
                .child(0)
                .infant(0)
                .build();

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();

        ProcessBaggageResponse processBaggageResponse = ProcessBaggageResponse.builder()
                .price(BigDecimal.valueOf(350000L))
                .extraBagage(30)
                .build();
        AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                .seatCode("A1")
                .price(PriceResponse.builder()
                        .amount(BigDecimal.valueOf(4000000L))
                        .build())
                .build();

        ProcessPassengerResponse processPassengerResponse = ProcessPassengerResponse.builder()
                .id("random-process-passenger-id")
                .status("departure")
                .scheduleId("random-schedule-id")
                .titel(titel)
                .baggageResponse(processBaggageResponse)
                .seatResponse(aircraftSeatResponse)
                .build();

        Mockito.when(scheduleRepository.findById(processPassengerResponse.getScheduleId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bookingDetailService.saveBookingDetail(processPassengerResponse, booking);
        });

        Mockito.verify(scheduleRepository).findById(processPassengerResponse.getScheduleId());
    }

    @Test
    void testSaveBookingDetailPassengerNotFound() {
        Booking booking = Booking.builder()
                .adult(1)
                .child(0)
                .infant(0)
                .build();

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();

        ProcessBaggageResponse processBaggageResponse = ProcessBaggageResponse.builder()
                .price(BigDecimal.valueOf(350000L))
                .extraBagage(30)
                .build();
        AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                .seatCode("A1")
                .price(PriceResponse.builder()
                        .amount(BigDecimal.valueOf(4000000L))
                        .build())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        ProcessPassengerResponse processPassengerResponse = ProcessPassengerResponse.builder()
                .id("random-process-passenger-id")
                .status("departure")
                .scheduleId("random-schedule-id")
                .titel(titel)
                .baggageResponse(processBaggageResponse)
                .seatResponse(aircraftSeatResponse)
                .build();

        Schedule schedule = Schedule.builder()
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

        Mockito.when(scheduleRepository.findById(processPassengerResponse.getScheduleId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(passengerRepository.findById(processPassengerResponse.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bookingDetailService.saveBookingDetail(processPassengerResponse, booking);
        });

        Mockito.verify(scheduleRepository).findById(processPassengerResponse.getScheduleId());
    }

    @Test
    void testGetBookingResponseSuccess() {
        String bookingId = "random-booking-id";

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();
        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("adult")
                .description("adult desu")
                .build();

        Passenger passenger = Passenger.builder()
                .id("random-passenger-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .titel(titel)
                .ageCategory(ageCategory)
                .cityzenship(country)
                .issuingCountry(country)
                .build();

        ProcessBaggageResponse processBaggageResponse = ProcessBaggageResponse.builder()
                .price(BigDecimal.valueOf(350000L))
                .extraBagage(30)
                .build();
        AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                .seatCode("A1")
                .price(PriceResponse.builder()
                        .amount(BigDecimal.valueOf(4000000L))
                        .build())
                .build();

        ProcessPassengerResponse processPassengerResponse = ProcessPassengerResponse.builder()
                .id("random-process-passenger-id")
                .status("departure")
                .scheduleId("random-schedule-id")
                .titel(titel)
                .baggageResponse(processBaggageResponse)
                .seatResponse(aircraftSeatResponse)
                .build();

        User user = User.builder()
                .id("random-user-id")
                .fullName("Kaguya shinomiya")
                .build();

        BookingDetail bookingDetail1 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .passenger(passenger)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        BookingDetail bookingDetail2 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("arrival")
                .build();

        List<BookingDetail> bookingDetails = new ArrayList<>();
        bookingDetails.add(bookingDetail1);
        bookingDetails.add(bookingDetail2);

        Bagage baggage = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .bookingDetails(bookingDetails)
                .user(user)
                .adult(1)
                .child(0)
                .infant(0)
                .build();

        Mockito.when(bookingRepository.findBookingDetailsById(bookingId))
                .thenReturn(booking);
        Mockito.when(scheduleRepository.findById(schedule.getId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(schedule.getAircraft().getId(), bookingDetail1.getExtraBagage()))
                .thenReturn(baggage);

        BookingResponse bookingResponse = bookingDetailService.getBookingResponse(bookingId);
        Assertions.assertNotNull(bookingResponse);

    }

    @Test
    void testUpdateScheduleStock() {
        String scheduleId = "random-schedule-id";

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        Mockito.when(scheduleRepository.findById(scheduleId))
                .thenReturn(Optional.of(schedule));
        Mockito.when(scheduleRepository.save(ArgumentMatchers.any(Schedule.class)))
                .thenReturn(schedule);

        bookingDetailService.updateScheduleStock(scheduleId);

        Mockito.verify(scheduleRepository).findById(scheduleId);
        Mockito.verify(scheduleRepository).save(ArgumentMatchers.any(Schedule.class));

    }

    @Test
    void testUpdateTotalPaidBookingSuccess() {
        String bookingId = "random-booking-id";
        BigDecimal total = BigDecimal.valueOf(10000000L);

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();
        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("adult")
                .description("adult desu")
                .build();

        Passenger passenger = Passenger.builder()
                .id("random-passenger-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .titel(titel)
                .ageCategory(ageCategory)
                .cityzenship(country)
                .issuingCountry(country)
                .build();

        User user = User.builder()
                .id("random-user-id")
                .fullName("Kaguya shinomiya")
                .build();

        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("arrival")
                .build();

        List<BookingDetail> bookingDetails = new ArrayList<>();
        bookingDetails.add(bookingDetail);

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .bookingDetails(bookingDetails)
                .user(user)
                .adult(1)
                .child(0)
                .infant(0)
                .build();

        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenReturn(booking);

        bookingDetailService.updateTotalPaidBooking(bookingId, total);

        Mockito.verify(bookingRepository).findById(bookingId);
        Mockito.verify(bookingRepository).save(ArgumentMatchers.any(Booking.class));

    }

    @Test
    void testUpdateTotalPaidBookingNotFound() {
        String bookingId = "random-booking-id";
        BigDecimal total = BigDecimal.valueOf(10000000L);

        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bookingDetailService.updateTotalPaidBooking(bookingId, total);
        });

        Mockito.verify(bookingRepository).findById(bookingId);

    }

    @Test
    void testTransactionSuccess() {
        String userId = "random-user-id";
        Integer baggageweight = 20;

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();

        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("Adult")
                .description("14+")
                .build();
        Passenger passenger = Passenger.builder()
                .id("random-ps-id")
                .titel(titel)
                .cityzenship(country)
                .issuingCountry(country)
                .ageCategory(ageCategory)
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .passportNumber("9134801830913")
                .build();

        BookingDetail bookingDetail1 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        BookingDetail bookingDetail2 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .seatPrice(BigDecimal.valueOf(600000L))
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("return")
                .build();

        List<BookingDetail> bookingDetails = new ArrayList<>();
        bookingDetails.add(bookingDetail1);
        bookingDetails.add(bookingDetail2);

        Bagage baggage = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("A1")
                .build();
        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .aircraftSeat(bookingAircraftSeatRequest)
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
                .build();

        Passenger passenger1 = Passenger.builder()
                .id("random-ps-id")
                .titel(titel)
                .cityzenship(country)
                .issuingCountry(country)
                .ageCategory(ageCategory)
                .firstName(passengerRequest.getFirstName())
                .lastName(passengerRequest.getLastName())
                .passportNumber(passengerRequest.getPassportNumber())
                .build();

        List<Passenger> passengerList = new ArrayList<>();
        passengerList.add(passenger1);

        List<BookingPassengerRequest> bookingPassengerRequests = new ArrayList<>();
        bookingPassengerRequests.add(passengerRequest);

        BookingRequest bookingRequest = BookingRequest.builder()
                .data(bookingPassengerRequests)
                .build();

        BookingDetailRequest bookingDetailRequest = BookingDetailRequest.builder()
                .amount(BigDecimal.valueOf(16200000L))
                .bookingType("Round Trip")
                .adult(1)
                .child(0)
                .infant(0)
                .departures(bookingRequest)
                .returns(bookingRequest)
                .build();

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .adult(bookingDetailRequest.getAdult())
                .child(bookingDetailRequest.getChild())
                .infant(bookingDetailRequest.getInfant())
                .user(user)
                .bookingType(bookingDetailRequest.getBookingType())
                .total(BigDecimal.valueOf(10000L))
                .bookingDetails(bookingDetails)
                .createdAt(LocalDateTime.now())
                .build();

        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .booking(booking)
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        ProcessBaggageResponse processBaggageResponse = ProcessBaggageResponse.builder()
                .price(BigDecimal.valueOf(350000L))
                .extraBagage(30)
                .build();
        AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                .seatCode("A1")
                .price(PriceResponse.builder()
                        .amount(BigDecimal.valueOf(4000000L))
                        .build())
                .build();

        ProcessPassengerResponse processPassengerResponse = ProcessPassengerResponse.builder()
                .status("departure")
                .scheduleId("random-schedule-id")
                .titel(titel)
                .baggageResponse(processBaggageResponse)
                .seatResponse(aircraftSeatResponse)
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenReturn(booking);
        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(null);
        Mockito.when(aircraftSeatRepository.findById(bookingAircraftSeatRequest.getId()))
                .thenReturn(Optional.of(aircraftSeat));
        Mockito.when(scheduleRepository.findById(passengerRequest.getScheduleId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(seatScheduleBookingRepository.save(ArgumentMatchers.any(SeatScheduleBooking.class)))
                .thenReturn(seatScheduleBooking);
        Mockito.when(scheduleRepository.findById(schedule.getId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(ageCategoryRepository.findById(passengerRequest.getAgeCategoryId()))
                .thenReturn(Optional.of(ageCategory));
        Mockito.when(countryRepository.findById(passengerRequest.getCitizenshipId()))
                .thenReturn(Optional.of(country));
        Mockito.when(countryRepository.findById(passengerRequest.getIssuingCountryId()))
                .thenReturn(Optional.of(country));
        Mockito.when(titelRepository.findById(passengerRequest.getTitelId()))
                .thenReturn(Optional.of(titel));
        Mockito.when(passengerRepository.saveAll(Mockito.any(List.class)))
                .thenReturn(passengerList);
        Mockito.when(passengerRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(passenger));
        Mockito.when(bookingDetailRepository.save(ArgumentMatchers.any(BookingDetail.class)))
                .thenReturn(bookingDetail1);
        Mockito.when(bookingRepository.findBookingDetailsById(Mockito.anyString()))
                .thenReturn(booking);
        Mockito.when(bookingRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(booking));
        Mockito.when(notificationConfiguration.getTitle())
                .thenReturn("test-notification");
        Mockito.when(notificationConfiguration.getDescription())
                .thenReturn("test-desc");
        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(baggage);

        BookingResponse transactionResponse = bookingDetailService.transaction(bookingDetailRequest, userId);
        Assertions.assertNotNull(transactionResponse);

    }

    @Test
    void testTransactionTotalInvalid() {
        String userId = "random-user-id";
        Integer baggageweight = 20;

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture")
                .name("Neko Air")
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

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .aircraftManufacture(aircraftManufacture)
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("jakarta")
                .country(country)
                .build();

        Airport originAirport = Airport.builder()
                .name("Soekarno hatta international Airport")
                .city(jakarta)
                .build();

        Airport destAirport = Airport.builder()
                .name("Haneda international Airport")
                .city(jakarta)
                .build();

        Schedule schedule = Schedule.builder()
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

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .aircraft(aircraft)
                .seatCode("A1")
                .price(BigDecimal.valueOf(500000L))
                .createdAt(LocalDateTime.now())
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-id")
                .schedule(schedule)
                .aircraftSeat(aircraftSeat)
                .seatStatus(true)
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit);

        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);

        Titel titel = Titel.builder()
                .id("random-titel-id")
                .titelName("MC")
                .description("Mc anime")
                .build();

        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("Adult")
                .description("14+")
                .build();
        Passenger passenger = Passenger.builder()
                .id("random-ps-id")
                .titel(titel)
                .cityzenship(country)
                .issuingCountry(country)
                .ageCategory(ageCategory)
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .passportNumber("9134801830913")
                .build();

        BookingDetail bookingDetail1 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        BookingDetail bookingDetail2 = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .seatPrice(BigDecimal.valueOf(600000L))
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("return")
                .build();

        List<BookingDetail> bookingDetails = new ArrayList<>();
        bookingDetails.add(bookingDetail1);
        bookingDetails.add(bookingDetail2);

        Bagage baggage = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        BookingBaggageRequest bookingBaggageRequest = BookingBaggageRequest.builder()
                .total(20)
                .build();

        BookingAircraftSeatRequest bookingAircraftSeatRequest = BookingAircraftSeatRequest.builder()
                .id("A1")
                .build();
        BookingPassengerRequest passengerRequest = BookingPassengerRequest.builder()
                .scheduleId("random-schedule-id")
                .titelId("random-titel-id")
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .ageCategoryId("random-age-category-id")
                .citizenshipId("random-citizenship-id")
                .issuingCountryId("random-issuing-country-id")
                .birthDate(LocalDate.now())
                .status("departure")
                .aircraftSeat(bookingAircraftSeatRequest)
                .baggage(bookingBaggageRequest)
                .passportNumber("9134801830913")
                .build();

        Passenger passenger1 = Passenger.builder()
                .id("random-ps-id")
                .titel(titel)
                .cityzenship(country)
                .issuingCountry(country)
                .ageCategory(ageCategory)
                .firstName(passengerRequest.getFirstName())
                .lastName(passengerRequest.getLastName())
                .passportNumber(passengerRequest.getPassportNumber())
                .build();

        List<Passenger> passengerList = new ArrayList<>();
        passengerList.add(passenger1);

        List<BookingPassengerRequest> bookingPassengerRequests = new ArrayList<>();
        bookingPassengerRequests.add(passengerRequest);

        BookingRequest bookingRequest = BookingRequest.builder()
                .data(bookingPassengerRequests)
                .build();

        BookingDetailRequest bookingDetailRequest = BookingDetailRequest.builder()
                .amount(BigDecimal.valueOf(16000000L))
                .bookingType("Round Trip")
                .adult(1)
                .child(0)
                .infant(0)
                .departures(bookingRequest)
                .returns(bookingRequest)
                .build();

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .adult(bookingDetailRequest.getAdult())
                .child(bookingDetailRequest.getChild())
                .infant(bookingDetailRequest.getInfant())
                .user(user)
                .bookingType(bookingDetailRequest.getBookingType())
                .total(BigDecimal.valueOf(10000L))
                .bookingDetails(bookingDetails)
                .createdAt(LocalDateTime.now())
                .build();

        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .passenger(passenger)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .booking(booking)
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        ProcessBaggageResponse processBaggageResponse = ProcessBaggageResponse.builder()
                .price(BigDecimal.valueOf(350000L))
                .extraBagage(30)
                .build();

        AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                .seatCode("A1")
                .price(PriceResponse.builder()
                        .amount(BigDecimal.valueOf(4000000L))
                        .build())
                .build();

        ProcessPassengerResponse processPassengerResponse = ProcessPassengerResponse.builder()
                .status("departure")
                .scheduleId("random-schedule-id")
                .titel(titel)
                .baggageResponse(processBaggageResponse)
                .seatResponse(aircraftSeatResponse)
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenReturn(booking);
        Mockito.when(seatScheduleBookingRepository.checkSeatStatus(passengerRequest.getScheduleId(), passengerRequest.getAircraftSeat().getId()))
                .thenReturn(null);
        Mockito.when(aircraftSeatRepository.findById(bookingAircraftSeatRequest.getId()))
                .thenReturn(Optional.of(aircraftSeat));
        Mockito.when(scheduleRepository.findById(passengerRequest.getScheduleId()))
                .thenReturn(Optional.of(schedule));
        Mockito.when(seatScheduleBookingRepository.save(ArgumentMatchers.any(SeatScheduleBooking.class)))
                .thenReturn(seatScheduleBooking);
        Mockito.when(ageCategoryRepository.findById(passengerRequest.getAgeCategoryId()))
                .thenReturn(Optional.of(ageCategory));
        Mockito.when(countryRepository.findById(passengerRequest.getCitizenshipId()))
                .thenReturn(Optional.of(country));
        Mockito.when(countryRepository.findById(passengerRequest.getIssuingCountryId()))
                .thenReturn(Optional.of(country));
        Mockito.when(titelRepository.findById(passengerRequest.getTitelId()))
                .thenReturn(Optional.of(titel));
        Mockito.when(passengerRepository.saveAll(Mockito.any(List.class)))
                .thenReturn(passengerList);
        Mockito.when(passengerRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(passenger));
        Mockito.when(bookingDetailRepository.save(ArgumentMatchers.any(BookingDetail.class)))
                .thenReturn(bookingDetail1);
        Mockito.when(bookingRepository.findBookingDetailsById(Mockito.anyString()))
                .thenReturn(booking);
        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(baggage);

        Assertions.assertThrows(ValidationException.class, () -> {
            bookingDetailService.transaction(bookingDetailRequest, userId);
        });

    }
}