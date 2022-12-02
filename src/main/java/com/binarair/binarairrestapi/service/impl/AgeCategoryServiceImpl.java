package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.AgeCategory;
import com.binarair.binarairrestapi.model.request.AgeCategoryRequest;
import com.binarair.binarairrestapi.model.response.AgeCategoryResponse;
import com.binarair.binarairrestapi.repository.AgeCategoryRepository;
import com.binarair.binarairrestapi.service.AgeCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgeCategoryServiceImpl implements AgeCategoryService {

    private final static Logger log = LoggerFactory.getLogger(AgeCategoryServiceImpl.class);

    private final AgeCategoryRepository ageCategoryRepository;

    @Autowired
    public AgeCategoryServiceImpl(AgeCategoryRepository ageCategoryRepository) {
        this.ageCategoryRepository = ageCategoryRepository;
    }

    @Override
    public AgeCategoryResponse save(AgeCategoryRequest ageCategoryRequest) {
        Optional<AgeCategory> categoryName = ageCategoryRepository.findByCategoryName(ageCategoryRequest.getCategoryName());
        if (categoryName.isPresent()) {
            throw new DataAlreadyExistException(String.format("Category with name %s is already available", ageCategoryRequest.getCategoryName()));
        }

        AgeCategory ageCategory = AgeCategory.builder()
                .id(String.format("ac-%s", UUID.randomUUID().toString()))
                .categoryName(ageCategoryRequest.getCategoryName())
                .description(ageCategoryRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save age category data");
        ageCategoryRepository.save(ageCategory);
        log.info("Successful save age category data");
        return AgeCategoryResponse.builder()
                .id(ageCategory.getId())
                .categoryName(ageCategory.getCategoryName())
                .description(ageCategory.getDescription())
                .createdAt(ageCategory.getCreatedAt())
                .build();
    }

    @Override
    public List<AgeCategoryResponse> getAll() {
        log.info("Do get all age category data");
        List<AgeCategory> ageCategories = ageCategoryRepository.findAll();
        List<AgeCategoryResponse> ageCategoryResponses = new ArrayList<>();
        ageCategories.stream().forEach(ageCategory -> {
            AgeCategoryResponse ageCategoryResponse = AgeCategoryResponse.builder()
                    .id(ageCategory.getId())
                    .categoryName(ageCategory.getCategoryName())
                    .description(ageCategory.getDescription())
                    .createdAt(ageCategory.getCreatedAt())
                    .build();
            ageCategoryResponses.add(ageCategoryResponse);
        });
        log.info("Successfull get all age category data");
        return ageCategoryResponses;
    }

    @Override
    public AgeCategoryResponse findById(String ageCategoryId) {
        log.info("Do get data age category by id");
        AgeCategory ageCategory = ageCategoryRepository.findById(ageCategoryId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Data age category with id %s not found", ageCategoryId)));
        log.info("Successful get data age category");
        return AgeCategoryResponse.builder()
                .id(ageCategory.getId())
                .categoryName(ageCategory.getCategoryName())
                .description(ageCategory.getDescription())
                .createdAt(ageCategory.getCreatedAt())
                .build();
    }

    @Override
    public Boolean delete(String ageCategoryId) {
        boolean isExists = ageCategoryRepository.existsById(ageCategoryId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Data age category with id %s not found", ageCategoryId));
        }
        log.info("Do delete age category by id");
        ageCategoryRepository.deleteById(ageCategoryId);
        log.info("Successful delete age category");
        return true;
    }
}
