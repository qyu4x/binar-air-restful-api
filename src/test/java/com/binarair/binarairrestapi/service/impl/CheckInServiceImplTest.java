package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.model.enums.RoleType;
import com.binarair.binarairrestapi.model.request.CancelCheckInRequest;
import com.binarair.binarairrestapi.model.request.CheckInRequest;
import com.binarair.binarairrestapi.model.response.CancelCheckInResponse;
import com.binarair.binarairrestapi.model.response.CheckInResponse;
import com.binarair.binarairrestapi.repository.BagageRepository;
import com.binarair.binarairrestapi.repository.BookingDetailRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckInServiceImplTest {

    @Mock
    private BookingDetailRepository bookingDetailRepository;

    @Mock
    private BagageRepository baggageRepository;

    @InjectMocks
    private CheckInServiceImpl checkInService;

    @Test
    void testCheckInServiceSuccess() {
        CheckInRequest checkInRequest = CheckInRequest.builder()
                .bookingReferenceNumber("AJDFDF")
                .lastName("Shinomiya")
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .user(user)
                .total(BigDecimal.valueOf(10000L))
                .createdAt(LocalDateTime.now())
                .build();

        TravelClass travelClass = TravelClass.builder()
                .name("++")
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
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
                .aircraft(aircraft)
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        Bagage baggage1 = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        Bagage baggage2 = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        List<Bagage> baggages = new ArrayList<>();
        baggages.add(baggage1);
        baggages.add(baggage2);

        Titel titel = Titel.builder()
                .titelName("MC Anime")
                .build();

        Passenger passenger = Passenger.builder()
                .firstName("Shinomiya")
                .lastName("Kaguya")
                .titel(titel)
                .build();

        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .passenger(passenger)
                .booking(booking)
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(false)
                .status("departure")
                .build();

        Mockito.when(bookingDetailRepository.findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
                .thenReturn(bookingDetail);
        Mockito.when(bookingDetailRepository.save(ArgumentMatchers.any(BookingDetail.class)))
                .thenReturn(bookingDetail);
        Mockito.when(baggageRepository.findByAircraftId(bookingDetail.getSchedule().getAircraft().getId()))
                .thenReturn(baggages);

        CheckInResponse checkInResponse = checkInService.checkIn(checkInRequest);
        Assertions.assertNotNull(checkInResponse);

        Mockito.verify(bookingDetailRepository).findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());
        Mockito.verify(bookingDetailRepository).save(ArgumentMatchers.any(BookingDetail.class));
        Mockito.verify(baggageRepository).findByAircraftId(bookingDetail.getSchedule().getAircraft().getId());

    }

    @Test
    void testCheckInServiceNotFound() {
        CheckInRequest checkInRequest = CheckInRequest.builder()
                .bookingReferenceNumber("AJDFDF")
                .lastName("Shinomiya")
                .build();

        Mockito.when(bookingDetailRepository.findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
                .thenReturn(null);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            checkInService.checkIn(checkInRequest);
        });

        Mockito.verify(bookingDetailRepository).findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());
    }

    @Test
    void testCacelCheckInSuccess() {
        CancelCheckInRequest cancelCheckInRequest = CancelCheckInRequest.builder()
                .bookingReferenceNumber("AJDFDF")
                .lastName("Shinomiya")
                .build();

        Role buyer = new Role();
        buyer.setRole(RoleType.BUYER);

        User user = User.builder()
                .id("random-id")
                .role(buyer)
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        Booking booking = Booking.builder()
                .id("random-booking-id")
                .user(user)
                .total(BigDecimal.valueOf(10000L))
                .createdAt(LocalDateTime.now())
                .build();

        TravelClass travelClass = TravelClass.builder()
                .name("++")
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
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
                .aircraft(aircraft)
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destAirport)
                .departureDate(LocalDate.of(2022, 12, 12))
                .arrivalDate(LocalDate.of(2022, 12, 13))
                .departureTime(LocalTime.now())
                .arrivalTime(LocalTime.now().plusHours(24))
                .price(BigDecimal.valueOf(7000000L))
                .stock(200)
                .sold(0)
                .createdAt(LocalDateTime.now())
                .build();

        Bagage baggage1 = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        Bagage baggage2 = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(20)
                .freeBagageCapacity(0)
                .price(BigDecimal.valueOf(400000L))
                .build();

        List<Bagage> baggages = new ArrayList<>();
        baggages.add(baggage1);
        baggages.add(baggage2);

        Titel titel = Titel.builder()
                .titelName("MC Anime")
                .build();

        Passenger passenger = Passenger.builder()
                .firstName("Shinomiya")
                .lastName("Kaguya")
                .titel(titel)
                .build();

        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-bookingdetail-id")
                .schedule(schedule)
                .quantity(1)
                .aircraftPrice(BigDecimal.valueOf(7000000L))
                .seatCode("A1")
                .passenger(passenger)
                .booking(booking)
                .extraBagage(10)
                .bagagePrice(BigDecimal.valueOf(500000L))
                .seatPrice(BigDecimal.valueOf(600000L))
                .bookingReferenceNumber("KASDJS")
                .checkInStatus(true)
                .status("departure")
                .build();

        Mockito.when(bookingDetailRepository.findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
                .thenReturn(bookingDetail);
        Mockito.when(bookingDetailRepository.save(ArgumentMatchers.any(BookingDetail.class)))
                .thenReturn(bookingDetail);
        Mockito.when(baggageRepository.findByAircraftId(bookingDetail.getSchedule().getAircraft().getId()))
                .thenReturn(baggages);

        CancelCheckInResponse cancelCheckInResponse = checkInService.cancelCheckIn(cancelCheckInRequest);
        Assertions.assertNotNull(cancelCheckInResponse);

        Mockito.verify(bookingDetailRepository).findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());
        Mockito.verify(bookingDetailRepository).save(ArgumentMatchers.any(BookingDetail.class));
        Mockito.verify(baggageRepository).findByAircraftId(bookingDetail.getSchedule().getAircraft().getId());

    }

    @Test
    void testCacelCheckInNotFound() {
        CancelCheckInRequest cancelCheckInRequest = CancelCheckInRequest.builder()
                .bookingReferenceNumber("AJDFDF")
                .lastName("Shinomiya")
                .build();

        Mockito.when(bookingDetailRepository.findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
                .thenReturn(null);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            checkInService.cancelCheckIn(cancelCheckInRequest);
        });

        Mockito.verify(bookingDetailRepository).findCheckInBookingDetail(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());

    }

}