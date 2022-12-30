package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Airlines;
import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.response.AirlineResponse;
import com.binarair.binarairrestapi.repository.AirlineRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private AirlineServiceImpl airlineService;

    @Mock
    private FirebaseStorageFileService firebaseStorageFileService;

    @Test
    void testSaveAirlineSuccess(){
        AirlineRequest airlineRequest = AirlineRequest.builder()
                .airlineName("Flying Airline")
                .description("I Fly to the sky")
                .build();
        MockMultipartFile file = new MockMultipartFile("fakeData", "image.jpg", "application/octet-stream", "some image".getBytes());

        Airlines airlines = Airlines.builder()
                .id("random-airline-uuid")
                .name(airlineRequest.getAirlineName())
                .logoURL("random-url")
                .description(airlineRequest.getDescription())
                .build();

        Mockito.when(firebaseStorageFileService.doUploadFile(file))
                .thenReturn("https://fakeimage.firebase.com");
        Mockito.when(airlineRepository.save(ArgumentMatchers.any(Airlines.class)))
                .thenReturn(airlines);
        AirlineResponse airlineResponse = airlineService.save(airlineRequest,file);
        Assertions.assertNotNull(airlineResponse);
        Assertions.assertEquals("https://fakeimage.firebase.com",airlineResponse.getLogoURL());
        Assertions.assertEquals(airlines.getName(),airlineResponse.getAirlineName());
        Mockito.verify(firebaseStorageFileService).doUploadFile(file);
        Mockito.verify(airlineRepository).save(ArgumentMatchers.any(Airlines.class));

    }
    @Test
    void testAirlineGetAll(){
        Airlines airlines1 = Airlines.builder()
                .name("Zukaze Air")
                .description("Fly like da wind")
                .build();
        Airlines airlines2 = Airlines.builder()
                .name("kaze Air")
                .description("Fly faster then da wind")
                .build();
        List<Airlines> airlinesList = new ArrayList<>();
        airlinesList.add(airlines1);
        airlinesList.add(airlines2);

        Mockito.when(airlineRepository.findAll())
                .thenReturn(airlinesList);

        List<AirlineResponse> airlineResponseList = airlineService.getAll();

        Assertions.assertEquals(2,airlineResponseList.size());
        Mockito.verify(airlineRepository).findAll();
    }

    @Test
    void testAirlineUpdateSuccess(){
        String airlineId="good-id";
        Airlines airline = Airlines.builder()
                .name("Kaze")
                .description("Like da windo")
                .id("good-id")
                .build();
        AirlineRequest airlineRequest = AirlineRequest.builder()
                .airlineName("Better Kaze")
                .description("Even faster than da windo")
                .build();
        Mockito.when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        AirlineResponse airlineResponse = airlineService.update(airlineRequest,airlineId);
        Assertions.assertNotNull(airlineResponse);
        Assertions.assertEquals(airlineResponse.getAirlineName(),airlineRequest.getAirlineName());
        Assertions.assertEquals(airlineResponse.getDescription(),airlineRequest.getDescription());

        Mockito.verify(airlineRepository).findById(airline.getId());
    }

    @Test
    void testAirlineUpdateDataNotFound(){
        String airlineId="good-id";
        Mockito.when(airlineRepository.findById(airlineId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,()-> {
            airlineService.findById(airlineId);
        });
        Mockito.verify(airlineRepository).findById(airlineId);
    }

    @Test
    void testAirlineDeleteSuccess(){
        String airlineId="random-id";
        Mockito.when(airlineRepository.existsById(airlineId))
                .thenReturn(true);
        Boolean stats = airlineService.delete(airlineId);
        Assertions.assertTrue(stats);
        Mockito.verify(airlineRepository).existsById(airlineId);
    }

    @Test
    void testAirlineDeleteNotFound(){
        String airlineId="random-id";
        Mockito.when(airlineRepository.existsById(airlineId))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class,()->{
            airlineService.delete(airlineId);
        });
        Mockito.verify(airlineRepository).existsById(airlineId);
    }

    @Test
    void testFindAirlineByIdSuccess(){
        String airlineId = "good-id";
        Airlines airline = Airlines.builder()
                .name("Kaze")
                .description("Like da windo")
                .id("good-id")
                .build();
        Mockito.when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        AirlineResponse airlineResponse = airlineService.findById(airlineId);
        Assertions.assertNotNull(airlineResponse);
        Assertions.assertEquals(airlineResponse.getAirlineName(),airline.getName());
        Assertions.assertEquals(airlineResponse.getDescription(),airline.getDescription());
        Mockito.verify(airlineRepository).findById(airline.getId());
    }

    @Test
    void testFindAirlineByIdNotFound(){
        String airlineId = "good-id";
        Mockito.when(airlineRepository.findById(airlineId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,()->{
            airlineService.findById(airlineId);
        });
        Mockito.verify(airlineRepository).findById(airlineId);
    }


}
