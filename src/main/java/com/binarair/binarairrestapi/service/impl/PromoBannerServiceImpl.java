package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.PromoBanner;
import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.response.PromoBannerPaggableResponse;
import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import com.binarair.binarairrestapi.repository.PromoBannerRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import com.binarair.binarairrestapi.service.PromoBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PromoBannerServiceImpl implements PromoBannerService {

    private final static Logger log = LoggerFactory.getLogger(PromoBannerServiceImpl.class);

    private final PromoBannerRepository promoBannerRepository;

    private final FirebaseStorageFileService firebaseStorageFileService;

    @Autowired
    public PromoBannerServiceImpl(PromoBannerRepository promoBannerRepository, FirebaseStorageFileService firebaseStorageFileService) {
        this.promoBannerRepository = promoBannerRepository;

        this.firebaseStorageFileService = firebaseStorageFileService;
    }

    @Override
    public PromoBannerResponse save(PromoBannerRequest promoBannerRequest, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new DataNotFoundException("Opps, please choose a picture first");
        }
        PromoBanner promoBanner = PromoBanner.builder()
                .id(String.format("pb-%s", UUID.randomUUID().toString()))
                .title(promoBannerRequest.getTitle())
                .description(promoBannerRequest.getDescription())
                .imageURL(firebaseStorageFileService.doUploadFile(multipartFile))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Is processing entering promo banner data");
        promoBannerRepository.save(promoBanner);
        log.info("Success save data promo banner");
        return PromoBannerResponse.builder()
                .id(promoBanner.getId())
                .title(promoBannerRequest.getTitle())
                .description(promoBannerRequest.getDescription())
                .imageURL(promoBanner.getImageURL())
                .createdAt(promoBanner.getCreatedAt())
                .build();
    }

    @Override
    public Page<PromoBannerPaggableResponse> getAll(Pageable pageable) {
        log.info("Is processing get all promo banner data");
        String field = "createdAt";
        Page<PromoBanner> promoBanners = promoBannerRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()).withSort(Sort.Direction.DESC, field));
        Page<PromoBannerPaggableResponse> promoBannerResponses = promoBanners.map(promoBanner -> new PromoBannerPaggableResponse(promoBanner));
        log.info("Success in getting all the promo banner data");
        return promoBannerResponses;
    }

    @Override
    public Boolean delete(String promoBannerId) {
        boolean isExists = promoBannerRepository.existsById(promoBannerId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Promo banner with id %s not found", promoBannerId));
        }
        log.info("Do delete promo banner data");
        promoBannerRepository.deleteById(promoBannerId);
        log.info("Successful delete promo banner data");
        return true;
    }

    @Override
    public PromoBannerResponse findById(String promoBannerId) {
        log.info("Do get promo banner data");
        PromoBanner promoBanner =  promoBannerRepository.findById(promoBannerId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Promo banner with id %s not found", promoBannerId)));
        log.info("Success get promo banner data");
        return PromoBannerResponse.builder()
                .id(promoBanner.getId())
                .title(promoBanner.getTitle())
                .description(promoBanner.getDescription())
                .imageURL(promoBanner.getImageURL())
                .createdAt(promoBanner.getCreatedAt())
                .build();
    }

    @Override
    public PromoBannerResponse update(PromoBannerRequest promoBannerRequest, String promoBannerId) {
        PromoBanner promoBanner = promoBannerRepository.findById(promoBannerId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Promo banner with id %s not found", promoBannerId)));
        promoBanner.setTitle(promoBannerRequest.getTitle());
        promoBanner.setDescription(promoBannerRequest.getDescription());
        log.info("Do update promo banner");
        promoBannerRepository.save(promoBanner);
        log.info("Successful update promo banner data");
        return PromoBannerResponse.builder()
                .id(promoBanner.getId())
                .title(promoBanner.getTitle())
                .description(promoBanner.getDescription())
                .imageURL(promoBanner.getImageURL())
                .createdAt(promoBanner.getCreatedAt())
                .build();
    }
}
