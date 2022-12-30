package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.model.request.BagageRequest;
import com.binarair.binarairrestapi.model.response.BagageResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.BagageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BagageServiceImplTest {

    @Mock
    private BagageRepository bagageRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private BagageServiceImpl bagageService;

    @Test
    void testSaveBagageSuccess(){
        BagageRequest bagageRequest = BagageRequest.builder()
                .aircraftId("random-aircraft-id")
                .weight(10)
                .price(BigDecimal.valueOf(300000))
                .freeBagageCapacity(10)
                .freeCabinCapacity(10)
                .build();
        Bagage bagage = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(bagageRequest.getFreeCabinCapacity())
                .freeBagageCapacity(bagageRequest.getFreeBagageCapacity())
                .weight(bagageRequest.getWeight())
                .build();

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .name("Maou")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .aircraftManufacture(aircraftManufacture)
                .model("666")
                .build();
        Mockito.when(aircraftRepository.findById(bagageRequest.getAircraftId()))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(bagageRepository.save(ArgumentMatchers.any(Bagage.class)))
                .thenReturn(bagage);

        BagageResponse bagageResponse = bagageService.save(bagageRequest);
        Assertions.assertNotNull(bagageResponse);
        Assertions.assertEquals(bagageResponse.getFreeBagageCapacity(),bagageRequest.getFreeBagageCapacity());
        Assertions.assertEquals(bagageResponse.getPrice(),bagageRequest.getPrice());
        Assertions.assertEquals(bagageResponse.getWeight(),bagageRequest.getWeight());
        Assertions.assertEquals(bagageResponse.getFreeCabinCapacity(),bagageRequest.getFreeCabinCapacity());

        Mockito.verify(aircraftRepository,Mockito.times(1)).findById(bagageRequest.getAircraftId());
        Mockito.verify(bagageRepository,Mockito.times(1)).save(ArgumentMatchers.any(Bagage.class));

    }

    @Test
    void testSaveBagageAircraftNotFound(){
        BagageRequest bagageRequest = BagageRequest.builder()
                .aircraftId("random-aircraft-id")
                .weight(10)
                .price(BigDecimal.valueOf(300000))
                .freeBagageCapacity(10)
                .freeCabinCapacity(10)
                .build();
        Mockito.when(aircraftRepository.findById(bagageRequest.getAircraftId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bagageService.save(bagageRequest);
        });
        Mockito.verify(aircraftRepository).findById(bagageRequest.getAircraftId());
    }


    @Test
    void testFindBagageByAircraftIdSuccess(){
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("HEMM yes")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("666")
                .aircraftManufacture(aircraftManufacture)
                .build();

        Bagage bagage = Bagage.builder()
                .id("random-baggage-id")
                .freeCabinBagageCapacity(10)
                .aircraft(aircraft)
                .freeBagageCapacity(10)
                .weight(10)
                .build();
        List<Bagage> bagageList = new ArrayList<>();
        bagageList.add(bagage);
        bagageList.add(bagage);

        Mockito.when(bagageRepository.findByAircraftId(aircraft.getId()))
                .thenReturn(bagageList);


        List<BagageResponse> bagageResponseList = bagageService.findBagageByAircraftId(aircraft.getId());
        Assertions.assertNotNull(bagageResponseList);


        Assertions.assertEquals(2,bagageResponseList.size());

        Mockito.verify(bagageRepository).findByAircraftId(aircraft.getId());

    }
    @Test
    void testFindBagageByAircraftIdNotFound(){

        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("666")
                .build();

        Mockito.when(bagageRepository.findByAircraftId(aircraft.getId()))
                .thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> {
            bagageService.findBagageByAircraftId(aircraft.getId());
        });

        Mockito.verify(bagageRepository).findByAircraftId(aircraft.getId());
    }

    @Test
    void testDeleteBagageSuccess(){
        String aircraftId = "random-aircraft-id";
        Mockito.when(bagageRepository.existsById(aircraftId))
                .thenReturn(true);
        Boolean stats = bagageService.delete(aircraftId);

        Assertions.assertTrue(stats);
        Mockito.verify(bagageRepository).existsById(aircraftId);
    }

    @Test
    void testDeleteBagageNotFound(){
        String aircraftId = "random-aircraft-id";
        Mockito.when(bagageRepository.existsById(aircraftId))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            bagageService.delete(aircraftId);
        });

        Mockito.verify(bagageRepository).existsById(aircraftId);
    }





}
