package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.repository.BookingRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRFontNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ETicketServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ETicketServiceImpl eTicketService;

    @Test
    void testCreateTicketDataNotFound() {
        String bookingId = "random-booking-id";

        Mockito.when(bookingRepository.findBookingDetailsById(bookingId))
                .thenReturn(null);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            eTicketService.createticket(bookingId);
        });
    }
    @Test
    void testCreateTicketJreNotFound()  {
        String bookingId = "random-booking-id";
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

        Titel titel = Titel.builder()
                .titelName("Ms")
                .build();
        Passenger passenger = Passenger.builder()
                .firstName("Kaguya")
                .lastName("Shinomiya")
                .passportNumber("09489023482")
                .titel(titel)
                .build();
        BookingDetail bookingDetail = BookingDetail.builder()
                .id("random-booking-id")
                .schedule(schedule)
                .passenger(passenger)
                .bookingReferenceNumber("93213K3")
                .extraBagage(20)
                .build();

        List<BookingDetail> bookingDetailList = new ArrayList<>();
        bookingDetailList.add(bookingDetail);

        User user = User.builder()
                .id("random-id")
                .fullName("Kaguya")
                .email("kaguyachan@gmail.com")
                .build();

        Booking booking = Booking.builder()
                .bookingType("One Way")
                .user(user)
                .id("random-booking-id")
                .bookingDetails(bookingDetailList)
                .build();

        Mockito.when(bookingRepository.findBookingDetailsById(bookingId))
                .thenReturn(booking);

        Assertions.assertThrows(JRFontNotFoundException.class, () -> {
            eTicketService.createticket(bookingId);
        });

        Mockito.verify(bookingRepository).findBookingDetailsById(bookingId);

    }
}