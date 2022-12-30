package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.entity.Airlines;
import com.binarair.binarairrestapi.model.entity.TravelClass;
import com.binarair.binarairrestapi.model.request.AircraftRequest;
import com.binarair.binarairrestapi.model.response.AircraftDetailResponse;
import com.binarair.binarairrestapi.repository.AircraftManufactureRepository;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.AirlineRepository;
import com.binarair.binarairrestapi.repository.TravelClassRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AircraftServiceImplTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private AircraftManufactureRepository aircraftManufactureRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private TravelClassRepository travelClassRepository;

    @InjectMocks
    private AircraftServiceImpl aircraftService;

    @Test
    void testSaveAircraftTravelClassNotFound() {
        AircraftRequest aircraftRequest = AircraftRequest.builder()
                .model("737")
                .passengerCapacity(200)
                .totalUnit(1)
                .seatArrangement("3-2-3")
                .distanceBetweenSeat(34)
                .seatLengthUnit("inch")
                .airCraftManufactureId("random-aircraft-manufacture-id")
                .travelClassId("random-travel-class-id")
                .airlinesId("random-airlines-id")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(aircraftManufactureRepository.findById(aircraftRequest.getAirCraftManufactureId()))
                .thenReturn(Optional.of(aircraftManufacture));
        Mockito.when(travelClassRepository.findById(aircraftRequest.getTravelClassId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftService.save(aircraftRequest);
        });

        Mockito.verify(aircraftManufactureRepository).findById(aircraftRequest.getAirCraftManufactureId());
        Mockito.verify(travelClassRepository).findById(aircraftRequest.getTravelClassId());

    }

    @Test
    void testSaveAircraftAircraftManufactureNotFound() {
        AircraftRequest aircraftRequest = AircraftRequest.builder()
                .model("737")
                .passengerCapacity(200)
                .totalUnit(1)
                .seatArrangement("3-2-3")
                .distanceBetweenSeat(34)
                .seatLengthUnit("inch")
                .airCraftManufactureId("random-aircraft-manufacture-id")
                .travelClassId("random-travel-class-id")
                .airlinesId("random-airlines-id")
                .build();

        Mockito.when(aircraftManufactureRepository.findById(aircraftRequest.getAirCraftManufactureId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftService.save(aircraftRequest);
        });

        Mockito.verify(aircraftManufactureRepository).findById(aircraftRequest.getAirCraftManufactureId());

    }

    @Test
    void testSaveAircraftDataAlreadyExists() {
        AircraftRequest aircraftRequest = AircraftRequest.builder()
                .model("737")
                .passengerCapacity(200)
                .totalUnit(1)
                .seatArrangement("3-2-3")
                .distanceBetweenSeat(34)
                .seatLengthUnit("inch")
                .airCraftManufactureId("random-aircraft-manufacture-id")
                .travelClassId("random-travel-class-id")
                .airlinesId("random-airlines-id")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("MC_ANIME")
                .name("Mc Anime")
                .build();

        Airlines airlines = Airlines.builder()
                .id("random-airlines-id")
                .name("Wakaranai")
                .description("wakaranai")
                .logoURL("https://image.com?media=image")
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .build();

        Mockito.when(aircraftManufactureRepository.findById(aircraftRequest.getAirCraftManufactureId()))
                .thenReturn(Optional.of(aircraftManufacture));
        Mockito.when(travelClassRepository.findById(aircraftRequest.getTravelClassId()))
                .thenReturn(Optional.of(travelClass));
        Mockito.when(airlineRepository.findById(aircraftRequest.getAirlinesId()))
                .thenReturn(Optional.of(airlines));
        Mockito.when(aircraftRepository.findByModel(aircraftRequest.getModel()))
                .thenReturn(Optional.of(aircraft));

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            aircraftService.save(aircraftRequest);
        });

        Mockito.verify(aircraftManufactureRepository).findById(aircraftRequest.getAirCraftManufactureId());
        Mockito.verify(travelClassRepository).findById(aircraftRequest.getTravelClassId());
        Mockito.verify(airlineRepository).findById(aircraftRequest.getAirlinesId());
        Mockito.verify(aircraftRepository).findByModel(aircraftRequest.getModel());

    }

    @Test
    void testSaveAircraftSuccess() {
        AircraftRequest aircraftRequest = AircraftRequest.builder()
                .model("737")
                .passengerCapacity(200)
                .totalUnit(1)
                .seatArrangement("3-2-3")
                .distanceBetweenSeat(34)
                .seatLengthUnit("inch")
                .airCraftManufactureId("random-aircraft-manufacture-id")
                .travelClassId("random-travel-class-id")
                .airlinesId("random-airlines-id")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("MC_ANIME")
                .name("Mc Anime")
                .build();

        Airlines airlines = Airlines.builder()
                .id("random-airlines-id")
                .name("Wakaranai")
                .description("wakaranai")
                .logoURL("https://image.com?media=image")
                .build();

        Mockito.when(aircraftManufactureRepository.findById(aircraftRequest.getAirCraftManufactureId()))
                .thenReturn(Optional.of(aircraftManufacture));
        Mockito.when(travelClassRepository.findById(aircraftRequest.getTravelClassId()))
                .thenReturn(Optional.of(travelClass));
        Mockito.when(airlineRepository.findById(aircraftRequest.getAirlinesId()))
                .thenReturn(Optional.of(airlines));
        Mockito.when(aircraftRepository.findByModel(aircraftRequest.getModel()))
                .thenReturn(Optional.empty());

        AircraftDetailResponse aircraftDetailResponse = aircraftService.save(aircraftRequest);
        Assertions.assertNotNull(aircraftDetailResponse);
        Assertions.assertEquals("737", aircraftDetailResponse.getModel());

        Mockito.verify(aircraftManufactureRepository).findById(aircraftRequest.getAirCraftManufactureId());
        Mockito.verify(travelClassRepository).findById(aircraftRequest.getTravelClassId());
        Mockito.verify(airlineRepository).findById(aircraftRequest.getAirlinesId());
        Mockito.verify(aircraftRepository).findByModel(aircraftRequest.getModel());

    }

    @Test
    void testGetAllAircraft() {
        TravelClass travelClass = TravelClass.builder()
                .id("MC_ANIME")
                .name("Mc Anime")
                .build();

        Airlines airlines = Airlines.builder()
                .id("random-airlines-id")
                .name("Wakaranai")
                .description("wakaranai")
                .logoURL("https://image.com?media=image")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .aircraftManufacture(aircraftManufacture)
                .build();

        List<Aircraft> aircraftList = new ArrayList<>();
        aircraftList.add(aircraft);
        aircraftList.add(aircraft);

        Mockito.when(aircraftRepository.findAll())
                .thenReturn(aircraftList);

        List<AircraftDetailResponse> aircraftDetailResponses = aircraftService.getAll();
        Assertions.assertNotNull(aircraftDetailResponses);
        Assertions.assertEquals(2, aircraftDetailResponses.size());

        Mockito.verify(aircraftRepository).findAll();

    }

    @Test
    void testFindByIdSuccess() {
        String aircraftId = "random-aircraft-id";
        TravelClass travelClass = TravelClass.builder()
                .id("MC_ANIME")
                .name("Mc Anime")
                .build();

        Airlines airlines = Airlines.builder()
                .id("random-airlines-id")
                .name("Wakaranai")
                .description("wakaranai")
                .logoURL("https://image.com?media=image")
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("737")
                .airlines(airlines)
                .travelClass(travelClass)
                .aircraftManufacture(aircraftManufacture)
                .build();

        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.of(aircraft));

        AircraftDetailResponse aircraftDetailResponse = aircraftService.findById(aircraftId);
        Assertions.assertNotNull(aircraftDetailResponse);
        Assertions.assertEquals(aircraft.getModel(),aircraftDetailResponse.getModel());

        Mockito.verify(aircraftRepository).findById(aircraftId);

    }

    @Test
    void testFindByIdNotFound() {
        String aircraftId = "random-aircraft-id";
        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftService.findById(aircraftId);
        });

        Mockito.verify(aircraftRepository).findById(aircraftId);

    }

    @Test
    void testDeleteSuccess() {
        String aircraftId = "random-aircraft-id";

        Mockito.when(aircraftRepository.existsById(aircraftId))
                .thenReturn(true);

        Boolean status = aircraftService.delete(aircraftId);
        Assertions.assertTrue(status);

        Mockito.verify(aircraftRepository).existsById(aircraftId);
        Mockito.verify(aircraftRepository).deleteById(aircraftId);

    }

    @Test
    void testDeleteNotFound() {
        String aircraftId = "random-aircraft-id";

        Mockito.when(aircraftRepository.existsById(aircraftId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftService.delete(aircraftId);
        });

        Mockito.verify(aircraftRepository).existsById(aircraftId);

    }
}