package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.PromoBanner;
import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import com.binarair.binarairrestapi.repository.PromoBannerRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import com.binarair.binarairrestapi.service.PromoBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PromoBannerResponse> getAll() {
        log.info("Is processing get all promo banner data");
        List<PromoBanner> promoBanners = promoBannerRepository.findAll();
        List<PromoBannerResponse> promoBannerResponses = new ArrayList<>();
        promoBanners.stream().forEach(promo -> {
            PromoBannerResponse promoBannerResponse = PromoBannerResponse.builder()
                    .id(promo.getId())
                    .title(promo.getTitle())
                    .description(promo.getDescription())
                    .imageURL(promo.getImageURL())
                    .createdAt(promo.getCreatedAt())
                    .build();
            promoBannerResponses.add(promoBannerResponse);
        });
        log.info("Success in getting all the promo banner data");
        return promoBannerResponses;
    }
}
