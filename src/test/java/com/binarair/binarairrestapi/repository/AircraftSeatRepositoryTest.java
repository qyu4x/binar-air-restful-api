package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.AircraftSeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AircraftSeatRepositoryTest {

    @Autowired
    private AircraftSeatRepository aircraftSeatRepository;

    @Test
    void testAvailableSeat() {

        List<AircraftSeat> available = aircraftSeatRepository.findAllAvailableSeatByAircraftId("AI001");
        System.out.println(available.size());

    }

    @Test
    void testReservedSeat() {

        List<AircraftSeat> available = aircraftSeatRepository.findAllReservedSeatByAircraftId("AI001");
        System.out.println(available.size());

    }

}