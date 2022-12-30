package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.request.AircraftManufactureRequest;
import com.binarair.binarairrestapi.model.response.AircraftManufactureResponse;
import com.binarair.binarairrestapi.repository.AircraftManufactureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AircraftManufactureServiceImplTest {

    @Mock
    private AircraftManufactureRepository aircraftManufactureRepository;

    @InjectMocks
    private AircraftManufactureServiceImpl aircraftManufactureService;

    @Test
    void testSaveAircraftManufactureSuccess() {
        AircraftManufactureRequest aircraftManufactureRequest = AircraftManufactureRequest.builder()
                .name("Kororo")
                .build();
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name(aircraftManufactureRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(aircraftManufactureRepository.findByName(aircraftManufactureRequest.getName()))
                .thenReturn(Optional.empty());
        Mockito.when(aircraftManufactureRepository.save(ArgumentMatchers.any(AircraftManufacture.class)))
                .thenReturn(aircraftManufacture);

        AircraftManufactureResponse aircraftManufactureResponse = aircraftManufactureService.save(aircraftManufactureRequest);
        Assertions.assertNotNull(aircraftManufactureResponse);
        Assertions.assertEquals(aircraftManufactureRequest.getName(), aircraftManufactureResponse.getName());

        Mockito.verify(aircraftManufactureRepository).findByName(aircraftManufactureRequest.getName());
        Mockito.verify(aircraftManufactureRepository).save(ArgumentMatchers.any(AircraftManufacture.class));
    }

    @Test
    void testSaveAircraftManufactureDataAlreadyExists() {
        AircraftManufactureRequest aircraftManufactureRequest = AircraftManufactureRequest.builder()
                .name("Kororo")
                .build();
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name(aircraftManufactureRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(aircraftManufactureRepository.findByName(aircraftManufactureRequest.getName()))
                .thenReturn(Optional.of(aircraftManufacture));

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            aircraftManufactureService.save(aircraftManufactureRequest);
        });

        Mockito.verify(aircraftManufactureRepository).findByName(aircraftManufactureRequest.getName());

    }

    @Test
    void testGetAllAircraftManfuactureSuccess() {
        AircraftManufacture aircraftManufacture1 = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kokoro")
                .createdAt(LocalDateTime.now())
                .build();

        AircraftManufacture aircraftManufacture2 = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("Kirara")
                .createdAt(LocalDateTime.now())
                .build();

        List<AircraftManufacture> aircraftManufactureList = new ArrayList<>();
        aircraftManufactureList.add(aircraftManufacture1);
        aircraftManufactureList.add(aircraftManufacture2);

        Mockito.when(aircraftManufactureRepository.findAll())
                .thenReturn(aircraftManufactureList);

        List<AircraftManufactureResponse> aircraftManufactureResponses = aircraftManufactureService.getAll();
        Assertions.assertNotNull(aircraftManufactureResponses);
        Assertions.assertEquals(2, aircraftManufactureResponses.size());

        Mockito.verify(aircraftManufactureRepository).findAll();

    }

    @Test
    void testDeleteSuccess() {
        String aircraftManufactureId = "random-manufacture-id";
        Mockito.when(aircraftManufactureRepository.existsById(aircraftManufactureId))
                .thenReturn(true);

        Boolean status = aircraftManufactureService.delete(aircraftManufactureId);
        Assertions.assertTrue(status);

        Mockito.verify(aircraftManufactureRepository).existsById(aircraftManufactureId);
        Mockito.verify(aircraftManufactureRepository).deleteById(aircraftManufactureId);
    }

    @Test
    void testDeleteNotFound() {
        String aircraftManufactureId = "random-manufacture-id";
        Mockito.when(aircraftManufactureRepository.existsById(aircraftManufactureId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            aircraftManufactureService.delete(aircraftManufactureId);
        });

        Mockito.verify(aircraftManufactureRepository).existsById(aircraftManufactureId);

    }
}