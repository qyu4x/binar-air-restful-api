package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.entity.AircraftSeat;
import com.binarair.binarairrestapi.model.entity.SeatScheduleBooking;
import com.binarair.binarairrestapi.model.request.AircraftSeatRequest;
import com.binarair.binarairrestapi.model.response.AircraftSeatResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.AircraftSeatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AircraftSeatServiceImplTest {

    @Mock
    private AircraftSeatRepository aircraftSeatRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private AircraftSeatServiceImpl aircraftSeatService;

    @Test
    void testFindAvailableSeatSuccess() {
        String aircraftId = "random-aircraft-id";

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model(" 737")
                .aircraftManufacture(aircraftManufacture)
                .build();

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraft(aircraft)
                .build();

        List<AircraftSeat> aircraftSeats = new ArrayList<>();
        aircraftSeats.add(aircraftSeat);

        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(aircraftSeatRepository.findAllAvailableSeatByAircraftId(aircraftId))
                .thenReturn(aircraftSeats);

        List<AircraftSeatResponse> allAvailableSeat = aircraftSeatService.findAllAvailableSeat(aircraftId);
        Assertions.assertNotNull(allAvailableSeat);
        Assertions.assertEquals(1, allAvailableSeat.size());

        Mockito.verify(aircraftRepository).findById(aircraftId);
        Mockito.verify(aircraftSeatRepository).findAllAvailableSeatByAircraftId(aircraftId);

    }

    @Test
    void testFindAllAvailableSeatNotFound() {
        String aircraftId = "random-aircraft-id";
        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftSeatService.findAllAvailableSeat(aircraftId);
        });

        Mockito.verify(aircraftRepository).findById(aircraftId);

    }

    @Test
    void testFindAllReservedSeatSuccess() {
        String aircraftId = "random-aircraft-id";

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model(" 737")
                .aircraftManufacture(aircraftManufacture)
                .build();

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraft(aircraft)
                .build();

        List<AircraftSeat> aircraftSeats = new ArrayList<>();
        aircraftSeats.add(aircraftSeat);

        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(aircraftSeatRepository.findAllReservedSeatByAircraftId(aircraftId))
                .thenReturn(aircraftSeats);

        List<AircraftSeatResponse> allAvailableSeat = aircraftSeatService.findAllReservedSeat(aircraftId);
        Assertions.assertNotNull(allAvailableSeat);
        Assertions.assertEquals(1, allAvailableSeat.size());

        Mockito.verify(aircraftRepository).findById(aircraftId);
        Mockito.verify(aircraftSeatRepository).findAllReservedSeatByAircraftId(aircraftId);

    }

    @Test
    void testFindAllReservedSeatNotFound() {
        String aircraftId = "random-aircraft-id";
        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftSeatService.findAllReservedSeat(aircraftId);
        });

        Mockito.verify(aircraftRepository).findById(aircraftId);

    }

    @Test
    void testGetAllByAircraftIdSuccess() {
        String aircraftId = "random-aircraft-id";

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model(" 737")
                .aircraftManufacture(aircraftManufacture)
                .build();

        SeatScheduleBooking seatScheduleBooking = SeatScheduleBooking.builder()
                .id("random-seat-schedule-booking")
                .build();
        List<SeatScheduleBooking> seatScheduleBookings = new ArrayList<>();
        seatScheduleBookings.add(seatScheduleBooking);

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .seatScheduleBookings(seatScheduleBookings)
                .aircraft(aircraft)
                .build();

        List<AircraftSeat> aircraftSeats = new ArrayList<>();
        aircraftSeats.add(aircraftSeat);

        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(aircraftSeatRepository.findAllByAircraftId(aircraftId))
                .thenReturn(aircraftSeats);

        List<AircraftSeatResponse> aircraftSeatResponses = aircraftSeatService.getAllByAircraftId(aircraftId);
        Assertions.assertNotNull(aircraftSeatResponses);
        Assertions.assertEquals(1, aircraftSeatResponses.size());

        Mockito.verify(aircraftRepository).findById(aircraftId);
        Mockito.verify(aircraftSeatRepository).findAllByAircraftId(aircraftId);

    }

    @Test
    void testGetAllByAircraftIdNotFound() {
        String aircraftId = "random-aircraft-id";
        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftSeatService.getAllByAircraftId(aircraftId);
        });

        Mockito.verify(aircraftRepository).findById(aircraftId);
    }

    @Test
    void testSaveAircraftSuccess() {
        AircraftSeatRequest aircraftSeatRequest = AircraftSeatRequest.builder()
                .aircraftId("random-aircraft-id")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraftId("random-aircraft-id")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model(" 737")
                .aircraftManufacture(aircraftManufacture)
                .build();

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraft(aircraft)
                .build();

        Mockito.when(aircraftSeatRepository.findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode()))
                .thenReturn(null);
        Mockito.when(aircraftRepository.findById(aircraftSeatRequest.getAircraftId()))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(aircraftSeatRepository.save(ArgumentMatchers.any(AircraftSeat.class)))
                .thenReturn(aircraftSeat);

        AircraftSeatResponse aircraftSeatResponse = aircraftSeatService.save(aircraftSeatRequest);
        Assertions.assertNotNull(aircraftSeatResponse);

        Mockito.verify(aircraftSeatRepository).findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode());
        Mockito.verify(aircraftRepository).findById(aircraftSeatRequest.getAircraftId());
        Mockito.verify(aircraftSeatRepository).save(ArgumentMatchers.any(AircraftSeat.class));
    }

    @Test
    void testSaveAircraftAircraftSeatNotFound() {
        AircraftSeatRequest aircraftSeatRequest = AircraftSeatRequest.builder()
                .aircraftId("random-aircraft-id")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraftId("random-aircraft-id")
                .build();

        Mockito.when(aircraftSeatRepository.findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode()))
                .thenReturn(null);
        Mockito.when(aircraftRepository.findById(aircraftSeatRequest.getAircraftId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftSeatService.save(aircraftSeatRequest);
        });

        Mockito.verify(aircraftSeatRepository).findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode());
        Mockito.verify(aircraftRepository).findById(aircraftSeatRequest.getAircraftId());

    }

    @Test
    void testSaveAircraftAircraftSeatDataAlreadyExists() {
        AircraftSeatRequest aircraftSeatRequest = AircraftSeatRequest.builder()
                .aircraftId("random-aircraft-id")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraftId("random-aircraft-id")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model(" 737")
                .aircraftManufacture(aircraftManufacture)
                .build();

        AircraftSeat aircraftSeat = AircraftSeat.builder()
                .id("random-aircraft-seat")
                .seatCode("A1")
                .price(BigDecimal.valueOf(300000L))
                .aircraft(aircraft)
                .build();

        Mockito.when(aircraftSeatRepository.findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode()))
                .thenReturn(aircraftSeat);

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            aircraftSeatService.save(aircraftSeatRequest);
        });

        Mockito.verify(aircraftSeatRepository).findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode());

    }

    @Test
    void testDeleteAircraftSeatSuccess() {
        String aircraftSeatId = "random-aircraft-seat-id";
        Mockito.when(aircraftSeatRepository.existsById(aircraftSeatId))
                .thenReturn(true);

        Boolean status = aircraftSeatService.delete(aircraftSeatId);
        Assertions.assertTrue(status);

        Mockito.verify(aircraftSeatRepository).existsById(aircraftSeatId);
        Mockito.verify(aircraftSeatRepository).deleteById(aircraftSeatId);
    }

    @Test
    void testDeleteAircraftSeatNotFound() {
        String aircraftSeatId = "random-aircraft-seat-id";
        Mockito.when(aircraftSeatRepository.existsById(aircraftSeatId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftSeatService.delete(aircraftSeatId);
        });

        Mockito.verify(aircraftSeatRepository).existsById(aircraftSeatId);
    }
}