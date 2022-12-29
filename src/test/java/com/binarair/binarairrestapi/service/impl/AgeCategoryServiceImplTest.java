package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.AgeCategory;
import com.binarair.binarairrestapi.model.request.AgeCategoryRequest;
import com.binarair.binarairrestapi.model.response.AgeCategoryResponse;
import com.binarair.binarairrestapi.repository.AgeCategoryRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgeCategoryServiceImplTest {

    @Mock
    private AgeCategoryRepository ageCategoryRepository;

    @InjectMocks
    private AgeCategoryServiceImpl ageCategoryService;

    @Test
    void testSaveAgeCategorySuccess() {
        AgeCategoryRequest ageCategoryRequest = AgeCategoryRequest.builder()
                .categoryName("Adult")
                .description("adult")
                .build();

        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName(ageCategoryRequest.getCategoryName())
                .description(ageCategoryRequest.getDescription())
                .build();

        Mockito.when(ageCategoryRepository.findByCategoryName(ageCategoryRequest.getCategoryName()))
                .thenReturn(Optional.empty());
        Mockito.when(ageCategoryRepository.save(ArgumentMatchers.any(AgeCategory.class)))
                .thenReturn(ageCategory);

        AgeCategoryResponse ageCategoryResponse = ageCategoryService.save(ageCategoryRequest);
        Assertions.assertNotNull(ageCategoryResponse);
        Assertions.assertEquals(ageCategoryRequest.getCategoryName(), ageCategoryResponse.getCategoryName());
        Assertions.assertEquals(ageCategoryRequest.getDescription(), ageCategoryResponse.getDescription());

        Mockito.verify(ageCategoryRepository).findByCategoryName(ageCategoryRequest.getCategoryName());
        Mockito.verify(ageCategoryRepository).save(ArgumentMatchers.any(AgeCategory.class));
    }

    @Test
    void testSaveAgeCategoryDataAlreadyExists() {
        AgeCategoryRequest ageCategoryRequest = AgeCategoryRequest.builder()
                .categoryName("Adult")
                .description("adult")
                .build();

        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName(ageCategoryRequest.getCategoryName())
                .description(ageCategoryRequest.getDescription())
                .build();

        Mockito.when(ageCategoryRepository.findByCategoryName(ageCategoryRequest.getCategoryName()))
                .thenReturn(Optional.of(ageCategory));

        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            ageCategoryService.save(ageCategoryRequest);
        });

        Mockito.verify(ageCategoryRepository).findByCategoryName(ageCategoryRequest.getCategoryName());
    }

    @Test
    void testGetAllAgeCategorySuccess() {
        AgeCategory ageCategory1 = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("Adult")
                .description("adult")
                .build();

        AgeCategory ageCategory2 = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("Child")
                .description("child")
                .build();

        List<AgeCategory> ageCategoryList = new ArrayList<>();
        ageCategoryList.add(ageCategory1);
        ageCategoryList.add(ageCategory2);

        Mockito.when(ageCategoryRepository.findAll())
                .thenReturn(ageCategoryList);

        List<AgeCategoryResponse> ageCategoryResponses = ageCategoryService.getAll();
        Assertions.assertNotNull(ageCategoryResponses);
        Assertions.assertEquals(2, ageCategoryResponses.size());

        Mockito.verify(ageCategoryRepository).findAll();

    }

    @Test
    void testAgeCategoryByIdSucesss() {
        AgeCategory ageCategory = AgeCategory.builder()
                .id("random-age-category-id")
                .categoryName("Child")
                .description("child")
                .build();

        Mockito.when(ageCategoryRepository.findById(ageCategory.getId()))
                .thenReturn(Optional.of(ageCategory));

        AgeCategoryResponse ageCategoryResponse = ageCategoryService.findById(ageCategory.getId());
        Assertions.assertNotNull(ageCategoryResponse);
        Assertions.assertEquals(ageCategory.getCategoryName(), ageCategoryResponse.getCategoryName());
        Assertions.assertEquals(ageCategory.getDescription(), ageCategoryResponse.getDescription());

        Mockito.verify(ageCategoryRepository).findById(ageCategory.getId());
    }

    @Test
    void testAgeCategoryByIdNotFound() {
        Mockito.when(ageCategoryRepository.findById("random-id"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            ageCategoryService.findById("random-id");
        });

        Mockito.verify(ageCategoryRepository).findById("random-id");
    }

    @Test
    void testDeleteSuccess() {
        String ageCategoryId = "random-id";
        Mockito.when(ageCategoryRepository.existsById(ageCategoryId))
                .thenReturn(true);

        Boolean status = ageCategoryService.delete(ageCategoryId);
        Assertions.assertTrue(status);

        Mockito.verify(ageCategoryRepository).existsById(ageCategoryId);
        
    }

    @Test
    void testDeleteNotFound() {
        String ageCategoryId = "random-id";
        Mockito.when(ageCategoryRepository.existsById(ageCategoryId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            ageCategoryService.delete(ageCategoryId);
        });

        Mockito.verify(ageCategoryRepository).existsById(ageCategoryId);
    }
}