package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.entity.Facility;
import com.binarair.binarairrestapi.model.request.FacilityRequest;
import com.binarair.binarairrestapi.model.response.FacilityDetailResponse;
import com.binarair.binarairrestapi.model.response.FacilityResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FacilityServiceImplTest {
    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    void testSaveFacilitySuccess(){
        FacilityRequest facilityRequest = FacilityRequest.builder()
                .aircraftId("random-aircraft-id")
                .name("Free Wifi")
                .desription("You get a free wifi Up to 10 KBps")
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
        Facility facility = Facility.builder()
                .id("random-facility-id")
                .name(facilityRequest.getName())
                .description(facilityRequest.getDesription())
                .status(facilityRequest.getStatus())
                .build();
        Mockito.when(aircraftRepository.findById(aircraft.getId()))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(facilityRepository.save(ArgumentMatchers.any(Facility.class)))
                .thenReturn(facility);

        FacilityDetailResponse facilityDetailResponse = facilityService.save(facilityRequest);
        Assertions.assertNotNull(facilityDetailResponse);
        Assertions.assertEquals(facilityDetailResponse.getName(),facilityRequest.getName());
        Assertions.assertEquals(facilityDetailResponse.getDesription(),facilityRequest.getDesription());

        Mockito.verify(aircraftRepository,Mockito.times(1)).findById(aircraft.getId());
        Mockito.verify(facilityRepository,Mockito.times(1)).save(ArgumentMatchers.any(Facility.class));
    }

    @Test
    void testSaveFacilityNoAircraft(){
        FacilityRequest facilityRequest = FacilityRequest.builder()
                .aircraftId("random-aircraft-id")
                .name("Free Wifi")
                .desription("You get a free wifi Up to 10 KBps")
                .status(true)
                .build();
        String aircraftId = "random-aircraft-id";
        Mockito.when(aircraftRepository.findById(aircraftId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,()->{
            facilityService.save(facilityRequest);
        });
        Mockito.verify(aircraftRepository).findById(aircraftId);
    }

    @Test
    void testGetAllFacilitySuccess(){
        Facility facility1 = Facility.builder()
                .status(true)
                .description("You can sit on it while on the flight")
                .name("You have chair")
                .build();
        Facility facility2 = Facility.builder()
                .status(true)
                .description("You can enjoy the coldness of aircon 3000 feet above")
                .name("You have AC")
                .build();
        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility1);
        facilityList.add(facility2);

        Mockito.when(facilityRepository.findAll())
                .thenReturn(facilityList);
        List<FacilityDetailResponse> facilityDetailResponseList = facilityService.getAll();
        Assertions.assertEquals(2,facilityDetailResponseList.size());

        Mockito.verify(facilityRepository).findAll();
    }

    @Test
    void testFindByAircraftIdSuccess(){
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .name("Final Boss")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .aircraftManufacture(aircraftManufacture)
                .model("666")
                .build();
        Facility facility1 = Facility.builder()
                .status(true)
                .description("You can sit on it while on the flight")
                .name("You have chair")
                .aircraft(aircraft)
                .build();
        Facility facility2 = Facility.builder()
                .status(true)
                .description("You wont get air pressure in flight")
                .name("You have roof")
                .aircraft(aircraft)
                .build();
        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility1);
        facilityList.add(facility2);
        Mockito.when(facilityRepository.findFacilitiesByAircraftId(aircraft.getId()))
                .thenReturn(facilityList);

        Mockito.when(aircraftRepository.findById(aircraft.getId()))
                .thenReturn(Optional.of(aircraft));

        List<FacilityDetailResponse> facilityDetailResponseList = facilityService.findByAircraftId(aircraft.getId());
        Assertions.assertEquals(2,facilityDetailResponseList.size());

        Mockito.verify(facilityRepository).findFacilitiesByAircraftId(aircraft.getId());
        Mockito.verify(aircraftRepository,Mockito.times(2)).findById(aircraft.getId());
    }

    @Test
    void testFindByAircraftIdNotFound(){
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .name("Final Boss")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .aircraftManufacture(aircraftManufacture)
                .model("666")
                .build();

        Facility facility = Facility.builder()
                .status(true)
                .description("You can sit on it while on the flight")
                .name("You have chair")
                .aircraft(aircraft)
                .build();
        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(facility);
        facilityList.add(facility);
        Mockito.when(facilityRepository.findFacilitiesByAircraftId(aircraft.getId()))
                .thenReturn(facilityList);
        Mockito.when(aircraftRepository.findById(aircraft.getId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,()-> {
            facilityService.findByAircraftId(aircraft.getId());
        });
        Mockito.verify(facilityRepository).findFacilitiesByAircraftId(aircraft.getId());
        Mockito.verify(aircraftRepository).findById(aircraft.getId());
    }
    @Test
    void testDeleteFacilitySuccess(){
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .name("Final Boss")
                .build();
        Aircraft aircraft = Aircraft.builder()
                .id("random-aircraft-id")
                .aircraftManufacture(aircraftManufacture)
                .model("666")
                .build();

        Facility facility = Facility.builder()
                .status(true)
                .id("facility-id")
                .description("You can sit on it while on the flight")
                .name("You have chair")
                .aircraft(aircraft)
                .build();
        Mockito.when(facilityRepository.existsById(facility.getId()))
                .thenReturn(true);
        Boolean stats = facilityService.delete(facility.getId());
        Assertions.assertTrue(stats);
        Mockito.verify(facilityRepository).existsById(facility.getId());
    }

    @Test
    void testDeleteFacilityNotExist(){
        String facilityId = "facility-id";
        Mockito.when(facilityRepository.existsById(facilityId))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class,()->{
            facilityService.delete(facilityId);
        });
        Mockito.verify(facilityRepository).existsById(facilityId);
    }

}

