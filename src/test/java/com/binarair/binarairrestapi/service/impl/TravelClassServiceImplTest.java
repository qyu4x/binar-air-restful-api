package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.TravelClass;
import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.request.TravelClassUpdateRequest;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;
import com.binarair.binarairrestapi.repository.TravelClassRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class TravelClassServiceImplTest {

    @Mock
    private TravelClassRepository travelClassRepository;

    @InjectMocks
    private TravelClassServiceImpl travelClassService;

    @Test
    void testSaveTravelClassSuccess() {
        TravelClassRequest travelClassRequest = TravelClassRequest.builder()
                .travelClassId("ECONOMY")
                .travelClassName("Economy")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id(travelClassRequest.getTravelClassId())
                .name(travelClassRequest.getTravelClassName())
                .build();

        Mockito.when(travelClassRepository.existsById(travelClassRequest.getTravelClassId()))
                .thenReturn(false);

        Mockito.when(travelClassRepository.save(ArgumentMatchers.any(TravelClass.class)))
                .thenReturn(travelClass);

        TravelClassResponse travelClassResponse = travelClassService.save(travelClassRequest);
        Assertions.assertEquals(travelClassRequest.getTravelClassId(), travelClassResponse.getTravelClassId());
        Assertions.assertEquals(travelClassRequest.getTravelClassName(), travelClassResponse.getTravelClassName());

        Mockito.verify(travelClassRepository).save(ArgumentMatchers.any(TravelClass.class));
        Mockito.verify(travelClassRepository).existsById(travelClassRequest.getTravelClassId());

    }

    @Test
    void testSaveTravelClassAlreadyExists() {
        TravelClassRequest travelClassRequest = TravelClassRequest.builder()
                .travelClassId("ECONOMY")
                .travelClassName("Economy")
                .build();

        Mockito.when(travelClassRepository.existsById(travelClassRequest.getTravelClassId()))
                .thenReturn(true);

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            travelClassService.save(travelClassRequest);
        });

        Mockito.verify(travelClassRepository).existsById(travelClassRequest.getTravelClassId());
    }

    @Test
    void testGetAllTravelClassSuccess() {
        TravelClass economy = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();
        TravelClass business = TravelClass.builder()
                .id("BUSINESS")
                .name("Business")
                .build();
        List<TravelClass> travelClassList = new ArrayList<>();
        travelClassList.add(economy);
        travelClassList.add(business);

        Mockito.when(travelClassRepository.findAll())
                .thenReturn(travelClassList);

        List<TravelClassResponse> listTravelClass = travelClassService.getAll();

        Assertions.assertEquals(2, listTravelClass.size());
        Mockito.verify(travelClassRepository).findAll();

    }

    @Test
    void testFindTravelClassByIdSuccess() {
        String travelClassId = "ECONOMY";
        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy")
                .build();

        Mockito.when(travelClassRepository.findById(travelClassId))
                .thenReturn(Optional.of(travelClass));
        TravelClassResponse travelClassResponse = travelClassService.findById(travelClassId);
        Assertions.assertEquals(travelClassId, travelClassResponse.getTravelClassId());

        Mockito.verify(travelClassRepository).findById(travelClassId);

    }

    @Test
    void testFindTravelClassByIdNotFound() {
        String travelClassId = "ECONOMY";

        Mockito.when(travelClassRepository.findById(travelClassId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            travelClassService.findById(travelClassId);
        });

        Mockito.verify(travelClassRepository).findById(travelClassId);

    }

    @Test
    void testDeleteTravelClasSuccess() {
        String travelClassId = "ECONOMY";

        Mockito.when(travelClassRepository.existsById(travelClassId))
                .thenReturn(true);
        Boolean status = travelClassService.delete(travelClassId);
        Assertions.assertTrue(status);

        Mockito.verify(travelClassRepository).existsById(travelClassId);
        Mockito.verify(travelClassRepository).deleteById(travelClassId);

    }

    @Test
    void testDeleteTravelClassNotFound() {
        String travelClassId = "ECONOMY";

        Mockito.when(travelClassRepository.existsById(travelClassId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            travelClassService.delete(travelClassId);
        });

        Mockito.verify(travelClassRepository).existsById(travelClassId);

    }

    @Test
    void testUpdateTravelClassSuccess() {
        String travelClassId = "ECONOMY";

        TravelClassUpdateRequest travelClassRequest = TravelClassUpdateRequest.builder()
                .travelClassName("Premium Economy")
                .build();

        TravelClass travelClass = TravelClass.builder()
                .id("ECONOMY")
                .name("Economy Plus")
                .build();

        Mockito.when(travelClassRepository.findById(travelClassId))
                .thenReturn(Optional.of(travelClass));
        TravelClassResponse updateResponse = travelClassService.update(travelClassRequest,travelClassId);
        Assertions.assertEquals(travelClassRequest.getTravelClassName(), updateResponse.getTravelClassName());

        Mockito.verify(travelClassRepository).findById(travelClassId);
        Mockito.verify(travelClassRepository).save(ArgumentMatchers.any(TravelClass.class));

    }

    @Test
    void testUpdateTravelClassNotFound() {
        String travelClassId = "ECONOMY";

        TravelClassUpdateRequest travelClassRequest = TravelClassUpdateRequest.builder()
                .travelClassName("Premium Economy")
                .build();


        Mockito.when(travelClassRepository.findById(travelClassId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            travelClassService.update(travelClassRequest,travelClassId);
        });

        Mockito.verify(travelClassRepository).findById(travelClassId);
    }
}