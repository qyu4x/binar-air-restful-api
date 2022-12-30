package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.entity.Benefit;
import com.binarair.binarairrestapi.model.request.BenefitRequest;
import com.binarair.binarairrestapi.model.request.BenefitUpdateRequest;
import com.binarair.binarairrestapi.model.response.BenefitDetailResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.BenefitRepository;
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
class BenefitServiceImplTest {

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private BenefitServiceImpl benefitService;

    @Test
    void testSaveBenefitSuccess(){
        BenefitRequest benefitRequest = BenefitRequest.builder()
                .aircraftId("random-aircraft-id")
                .description("Fix to the death")
                .name("Very happy")
                .status(true)
                .build();
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .name("Final Boss")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .aircraftManufacture(aircraftManufacture)
                .model("666")
                .build();
        Benefit benefit = Benefit.builder()
                .name(benefitRequest.getName())
                .description(benefitRequest.getDescription())
                .status(true)
                .aircraft(aircraft)
                .build();
        Mockito.when(aircraftRepository.findById(aircraft.getId()))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(benefitRepository.save(ArgumentMatchers.any(Benefit.class)))
                .thenReturn(benefit);

        BenefitDetailResponse benefitDetailResponse = benefitService.save(benefitRequest);
        Assertions.assertNotNull(benefitDetailResponse);
        Assertions.assertEquals(benefitDetailResponse.getName(),benefitRequest.getName());
        Assertions.assertEquals(benefitDetailResponse.getDesription(),benefitRequest.getDescription());
        Assertions.assertEquals(benefitDetailResponse.getStatus(),benefitRequest.getStatus());

        Mockito.verify(aircraftRepository,Mockito.times(1)).findById(benefitRequest.getAircraftId());
        Mockito.verify(benefitRepository,Mockito.times(1)).save(ArgumentMatchers.any(Benefit.class));
    }

    @Test
    void testSaveBenefitDetailsNotFound(){
        BenefitRequest benefitRequest = BenefitRequest.builder()
                .aircraftId("random-aircraft-id")
                .description("Fix to the death")
                .name("Very happy")
                .status(true)
                .build();
        Mockito.when(aircraftRepository.findById(benefitRequest.getAircraftId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            benefitService.save(benefitRequest);
        });
        Mockito.verify(aircraftRepository).findById(benefitRequest.getAircraftId());
    }


    @Test
    void testGetAllBenefitSuccess(){
        Benefit benefit1 = Benefit.builder()
                .status(true)
                .name("Benefit 1")
                .description("Benefit pertama ini sehat sekali")
                .build();
        Benefit benefit2 = Benefit.builder()
                .status(true)
                .name("Benefit 2")
                .description("Benefit kedua ini kurang sekali")
                .build();

        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit1);
        benefitList.add(benefit2);


        Mockito.when(benefitRepository.findAll())
                .thenReturn(benefitList);
        List<BenefitDetailResponse> benefitDetailResponseList = benefitService.getAll();
        Assertions.assertEquals(2,benefitDetailResponseList.size());

        Mockito.verify(benefitRepository).findAll();
    }

    @Test
    void testFindBenefitByAircraftIdSuccess(){
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("HEMM yes")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("666")
                .aircraftManufacture(aircraftManufacture)
                .build();

        Benefit benefit1 = Benefit.builder()
                .aircraft(aircraft)
                .description("Help")
                .name("Your Problem")
                .status(true)
                .build();
        Benefit benefit2 = Benefit.builder()
                .aircraft(aircraft)
                .description("UDIN")
                .name("Your name")
                .status(true)
                .build();
        List<Benefit> benefitList = new ArrayList<>();
        benefitList.add(benefit1);
        benefitList.add(benefit2);

        Mockito.when(aircraftRepository.findById(aircraft.getId()))
                .thenReturn(Optional.of(aircraft));

        Mockito.when(benefitRepository.findBenefitByAircraftId(aircraft.getId()))
                .thenReturn(benefitList);

        List<BenefitDetailResponse> benefitDetailResponseList = benefitService.findByAircraftId(aircraft.getId());
        Assertions.assertNotNull(benefitDetailResponseList);

        Assertions.assertEquals(2,benefitDetailResponseList.size());

        Mockito.verify(benefitRepository).findBenefitByAircraftId(aircraft.getId());
    }

    @Test
    void testFindBenefitByAircraftIdNotFound(){
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("666")
                .build();
        Mockito.when(benefitRepository.findBenefitByAircraftId(aircraft.getId()))
                .thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> {
            benefitService.findByAircraftId(aircraft.getId());
        });
        Mockito.verify(benefitRepository).findBenefitByAircraftId(aircraft.getId());
    }

    @Test
    void testDeleteBenefitSuccess(){
        String aircraftId = "random-aircraft-id";
        Mockito.when(benefitRepository.existsById(aircraftId))
                .thenReturn(true);
        Boolean stats = benefitService.delete(aircraftId);

        Assertions.assertTrue(stats);
        Mockito.verify(benefitRepository).existsById(aircraftId);
    }

    @Test
    void testDeleteBenefitNotFound(){
        String aircraftId = "random-aircraft-id";
        Mockito.when(benefitRepository.existsById(aircraftId))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class, ()-> {
            benefitService.delete(aircraftId);
        });

        Mockito.verify(benefitRepository).existsById(aircraftId);
    }

    @Test
    void testUpdateBenefitSuccess(){
        String benefitId = "benefitIdDesuyo";

        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id("random-aircraft-manufacture-id")
                .name("HEMM yes")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .model("666")
                .aircraftManufacture(aircraftManufacture)
                .build();

        BenefitUpdateRequest benefitUpdateRequest = BenefitUpdateRequest.builder()
                .name("I want to be A HERO")
                .description("KILLED THE TOASTER")
                .status(true)
                .build();

        Benefit benefit1 = Benefit.builder()
                .id("This is the one I want")
                .status(true)
                .name("Dota 2 is the way of life")
                .description("Defence of The Ancient is da best")
                .aircraft(aircraft)
                .build();

        Mockito.when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.of(benefit1));
        Mockito.when(benefitRepository.save(benefit1))
                .thenReturn(benefit1);

        BenefitDetailResponse updateResponse = benefitService.update(benefitUpdateRequest,benefitId);
        Assertions.assertNotNull(updateResponse);
        Assertions.assertEquals(benefitUpdateRequest.getName(),updateResponse.getName());
        Assertions.assertEquals(benefitUpdateRequest.getDescription(),updateResponse.getDesription());

        Mockito.verify(benefitRepository).findById(benefitId);
    }

    @Test
    void testUpdateBenefitNotFound(){
        String benefitId = "benefit-id";
        BenefitUpdateRequest benefitUpdateRequest = BenefitUpdateRequest.builder()
                .name("I want to be A HERO")
                .description("KILLED THE TOASTER")
                .status(true)
                .build();
        Mockito.when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            benefitService.update(benefitUpdateRequest,benefitId);
        });

        Mockito.verify(benefitRepository).findById(benefitId);
    }






}
