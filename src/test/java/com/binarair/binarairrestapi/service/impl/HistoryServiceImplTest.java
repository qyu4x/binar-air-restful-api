package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.exception.ValidationException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.model.response.HistoryResponse;
import com.binarair.binarairrestapi.repository.BagageRepository;
import com.binarair.binarairrestapi.repository.BookingRepository;
import com.binarair.binarairrestapi.repository.ScheduleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
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
class HistoryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private BagageRepository baggageRepository;

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Test
    void testFindHistoryBookingByUserIdAscSuccess() {
        String userId = "random-user-id";
        String sort = "ASC";

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("Tokyo")
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

        Booking userBookingHistory = Booking.builder()
                .user(user)
                .bookingDetails(bookingDetails)
                .child(1)
                .adult(1)
                .infant(0)
                .total(BigDecimal.valueOf(7500000L))
                .bookingType("One way")
                .createdAt(LocalDateTime.now())
                .build();

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(userBookingHistory);

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.when(bookingRepository.findHistoryBookingByUserIdAsc(userId))
                .thenReturn(bookingList);

        Mockito.when(scheduleRepository.findById(schedule.getId()))
                .thenReturn(Optional.of(schedule));

        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail1.getExtraBagage()))
                .thenReturn(baggage);

        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail2.getExtraBagage()))
                .thenReturn(baggage);

        List<HistoryResponse> historyBookingResponses = historyService.findHistoryBookingByUserId(userId, sort);
        Assertions.assertNotNull(historyBookingResponses);
        Assertions.assertEquals("One way", historyBookingResponses.get(0).getBookingType());

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(bookingRepository).findHistoryBookingByUserIdAsc(userId);
        Mockito.verify(scheduleRepository, Mockito.times(2)).findById(schedule.getId());
        Mockito.verify(baggageRepository, Mockito.times(2)).findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail1.getExtraBagage());
        Mockito.verify(baggageRepository, Mockito.times(2)).findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail2.getExtraBagage());

    }

    @Test
    void testFindHistoryBookingByUserIdDescSuccess() {
        String userId = "random-user-id";
        String sort = "DESC";

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("Tokyo")
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

        Booking userBookingHistory = Booking.builder()
                .user(user)
                .bookingDetails(bookingDetails)
                .child(1)
                .adult(1)
                .infant(0)
                .total(BigDecimal.valueOf(7500000L))
                .bookingType("One way")
                .createdAt(LocalDateTime.now())
                .build();

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(userBookingHistory);

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.when(bookingRepository.findHistoryBookingByUserIdDesc(userId))
                .thenReturn(bookingList);

        Mockito.when(scheduleRepository.findById(schedule.getId()))
                .thenReturn(Optional.of(schedule));

        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail1.getExtraBagage()))
                .thenReturn(baggage);

        Mockito.when(baggageRepository.findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail2.getExtraBagage()))
                .thenReturn(baggage);

        List<HistoryResponse> historyBookingResponses = historyService.findHistoryBookingByUserId(userId, sort);
        Assertions.assertNotNull(historyBookingResponses);
        Assertions.assertEquals("One way", historyBookingResponses.get(0).getBookingType());

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(bookingRepository).findHistoryBookingByUserIdDesc(userId);
        Mockito.verify(scheduleRepository, Mockito.times(2)).findById(schedule.getId());
        Mockito.verify(baggageRepository, Mockito.times(2)).findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail1.getExtraBagage());
        Mockito.verify(baggageRepository, Mockito.times(2)).findByAircraftIdAndBaggageWeight(aircraft.getId(), bookingDetail2.getExtraBagage());

    }

    @Test
    void testFindHistoryBookingByUserIdScheduleNotFound() {
        String userId = "random-user-id";
        String sort = "DESC";

        Country country = Country.builder()
                .countryCode("JP")
                .name("Japan")
                .build();

        City jakarta = City.builder()
                .name("Tokyo")
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

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        Booking userBookingHistory = Booking.builder()
                .user(user)
                .bookingDetails(bookingDetails)
                .child(1)
                .adult(1)
                .infant(0)
                .total(BigDecimal.valueOf(7500000L))
                .bookingType("One way")
                .createdAt(LocalDateTime.now())
                .build();

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(userBookingHistory);

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.when(bookingRepository.findHistoryBookingByUserIdDesc(userId))
                .thenReturn(bookingList);

        Mockito.when(scheduleRepository.findById(schedule.getId()))
                .thenReturn(Optional.empty());


        Assertions.assertThrows(DataNotFoundException.class, () -> {
            historyService.findHistoryBookingByUserId(userId, sort);
        });

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(bookingRepository).findHistoryBookingByUserIdDesc(userId);
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(schedule.getId());

    }

    @Test
    void testFindHistoryBookingByUserIdHistoryNotFound() {
        String userId = "random-user-id";
        String sort = "DESC";

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        List<Booking> bookingList = new ArrayList<>();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.when(bookingRepository.findHistoryBookingByUserIdDesc(userId))
                .thenReturn(bookingList);


        Assertions.assertThrows(DataNotFoundException.class, () -> {
            historyService.findHistoryBookingByUserId(userId, sort);
        });

        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(bookingRepository).findHistoryBookingByUserIdDesc(userId);
    }

    @Test
    void testFindHistoryBookingByUserIdUserNotFound() {
        String userId = "random-user-id";
        String sort = "ASC";

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(false);


        Assertions.assertThrows(DataNotFoundException.class, () -> {
            historyService.findHistoryBookingByUserId(userId, sort);
        });

        Mockito.verify(userRepository).existsById(userId);

    }

    @Test
    void testFindHistoryBookingByUserIdSortingNotFound() {
        String userId = "random-user-id";
        String sort = "OPppS";

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Assertions.assertThrows(ValidationException.class, () -> {
            historyService.findHistoryBookingByUserId(userId, sort);
        });

        Mockito.verify(userRepository).existsById(userId);

    }
}