package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Titel;
import com.binarair.binarairrestapi.model.request.TitelRequest;
import com.binarair.binarairrestapi.model.response.TitelResponse;
import com.binarair.binarairrestapi.repository.TitelRepository;
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
class TitelServiceImplTest {

    @Mock
    private TitelRepository titelRepository;

    @InjectMocks
    private TitelServiceImpl titelService;

    @Test
    void testSaveTitelSuccess() {
        TitelRequest titelRequest = TitelRequest.builder()
                .titelName("Anata")
                .description("Fufu(couple)")
                .build();

        Titel titel = Titel.builder()
                .id("random-uuid")
                .titelName(titelRequest.getTitelName())
                .description(titelRequest.getDescription())
                .build();
        Mockito.when(titelRepository.findByTitelName(titelRequest.getTitelName()))
                .thenReturn(Optional.empty());

        Mockito.when(titelRepository.save(ArgumentMatchers.any(Titel.class)))
                .thenReturn(titel);

        TitelResponse titelLResponse = titelService.save(titelRequest);
        Assertions.assertEquals(titelRequest.getTitelName(), titelLResponse.getTitelName());
        Assertions.assertEquals(titelRequest.getDescription(), titelLResponse.getDescription());

        Mockito.verify(titelRepository).findByTitelName(titelRequest.getTitelName());
        Mockito.verify(titelRepository).save(ArgumentMatchers.any(Titel.class));

    }

    @Test
    void testSaveTitelAlreadyExists() {
        TitelRequest titelRequest = TitelRequest.builder()
                .titelName("Anata")
                .description("Fufu(couple)")
                .build();
        Titel titel = Titel.builder()
                .id("random-uuid")
                .titelName(titelRequest.getTitelName())
                .description(titelRequest.getDescription())
                .build();

        Mockito.when(titelRepository.findByTitelName(titelRequest.getTitelName()))
                .thenReturn(Optional.of(titel));

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            titelService.save(titelRequest);
        });

        Mockito.verify(titelRepository).findByTitelName(titelRequest.getTitelName());

    }

    @Test
    void testGetAllTitelSuccess() {
        Titel anata = Titel.builder()
                .titelName("Anata")
                .description("Fufu(couple)")
                .build();
        Titel onichan = Titel.builder()
                .titelName("Onichan")
                .description("Imouto(younger sister)")
                .build();

        List<Titel> titelList = new ArrayList<>();
        titelList.add(anata);
        titelList.add(onichan);

        Mockito.when(titelRepository.findAll())
                .thenReturn(titelList);
        List<TitelResponse> titelResponses = titelService.getAll();
        Assertions.assertEquals(2, titelResponses.size());

        Mockito.verify(titelRepository).findAll();

    }

    @Test
    void testFindTitelById() {
        String titelId = "random-uuid";
        Titel titel = Titel.builder()
                .titelName("Anata")
                .description("Fufu(couple)")
                .build();

        Mockito.when(titelRepository.findById(titelId))
                .thenReturn(Optional.of(titel));
        TitelResponse titelResponse = titelService.findById(titelId);
        Assertions.assertEquals(titel.getTitelName(), titelResponse.getTitelName());
        Assertions.assertEquals(titel.getDescription(), titelResponse.getDescription());

        Mockito.verify(titelRepository).findById(titelId);

    }

    @Test
    void testFindTitelByIdNotFound() {
        String titelId = "random-uuid";
        Mockito.when(titelRepository.findById(titelId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            titelService.findById(titelId);
        });

        Mockito.verify(titelRepository).findById(titelId);

    }

    @Test
    void testDeleteTitelSuccess() {
        String titelId = "random-uuid";
        Mockito.when(titelRepository.existsById(titelId))
                .thenReturn(true);
        Boolean status = titelService.delete(titelId);

        Assertions.assertTrue(status);
        Mockito.verify(titelRepository).existsById(titelId);

    }

    @Test
    void testDeleteTitelNotFound() {
        String titelId = "random-uuid";
        Mockito.when(titelRepository.existsById(titelId))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            titelService.delete(titelId);
        });

        Mockito.verify(titelRepository).existsById(titelId);
    }

    @Test
    void testUpdateTitelSuccess() {
        String titelId = "random-uuid";
        Titel titel = Titel.builder()
                .titelName("Onichan")
                .description("Imouto(younger sister)")
                .build();

        TitelRequest titelRequest = TitelRequest.builder()
                .titelName("Anata")
                .description("Fufu(couple)")
                .build();

        Mockito.when(titelRepository.findById(titelId))
                .thenReturn(Optional.of(titel));
        Mockito.when(titelRepository.save(ArgumentMatchers.any(Titel.class)))
                .thenReturn(titel);

        TitelResponse titelResponse = titelService.update(titelRequest, titelId);
        Assertions.assertEquals(titelRequest.getTitelName(), titelResponse.getTitelName());
        Assertions.assertEquals(titelRequest.getDescription(), titelResponse.getDescription());

        Mockito.verify(titelRepository).findById(titelId);
        Mockito.verify(titelRepository).save(ArgumentMatchers.any(Titel.class));

    }

    @Test
    void testUpdateTitelNotFound() {
        String titelId = "random-uuid";
        TitelRequest titelRequest = TitelRequest.builder()
                .titelName("Anata")
                .description("Fufu(couple)")
                .build();

        Mockito.when(titelRepository.findById(titelId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            titelService.update(titelRequest, titelId);
        });

        Mockito.verify(titelRepository).findById(titelId);
    }
}