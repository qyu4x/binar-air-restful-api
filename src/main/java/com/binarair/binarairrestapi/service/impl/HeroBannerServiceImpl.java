package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.HeroBanner;
import com.binarair.binarairrestapi.model.request.HeroBannerRequest;
import com.binarair.binarairrestapi.model.response.HeroBannerResponse;
import com.binarair.binarairrestapi.repository.HeroBannerRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import com.binarair.binarairrestapi.service.HeroBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HeroBannerServiceImpl implements HeroBannerService {

    private final static Logger log = LoggerFactory.getLogger(HeroBannerServiceImpl.class);

    private final HeroBannerRepository heroBannerRepository;

    private final FirebaseStorageFileService firebaseStorageFileService;

    public HeroBannerServiceImpl(HeroBannerRepository heroBannerRepository, FirebaseStorageFileService firebaseStorageFileService) {
        this.heroBannerRepository = heroBannerRepository;
        this.firebaseStorageFileService = firebaseStorageFileService;
    }

    @Override
    public HeroBannerResponse save(HeroBannerRequest heroBannerRequest, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new DataNotFoundException("Opps, please choose a picture first");
        }
        HeroBanner heroBanner = HeroBanner.builder()
                .id(String.format("hb-%s", UUID.randomUUID().toString()))
                .title(heroBannerRequest.getTitle())
                .description(heroBannerRequest.getDescription())
                .imageURL(firebaseStorageFileService.doUploadFile(multipartFile))
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
        log.info("Is processing entering hero banner data");
        heroBannerRepository.save(heroBanner);
        log.info("Success save data hero banner");
        return HeroBannerResponse.builder()
                .id(heroBanner.getId())
                .title(heroBanner.getTitle())
                .description(heroBanner.getDescription())
                .imageURL(heroBanner.getImageURL())
                .createdAt(heroBanner.getCreatedAt())
                .build();
    }

    @Override
    public List<HeroBannerResponse> getAll() {
        log.info("Is processing get all hero banner data");
        List<HeroBanner> heroBanners = heroBannerRepository.findAll();
        List<HeroBannerResponse> heroBannerResponses = new ArrayList<>();
        heroBanners.stream().forEach(banner -> {
            HeroBannerResponse heroBanner = HeroBannerResponse.builder()
                    .id(banner.getId())
                    .title(banner.getTitle())
                    .description(banner.getDescription())
                    .imageURL(banner.getImageURL())
                    .createdAt(banner.getCreatedAt())
                    .build();
            heroBannerResponses.add(heroBanner);
        });
        log.info("Success in getting all the hero banner data");
        return heroBannerResponses;
    }

    @Override
    public Boolean delete(String heroBannerId) {
        boolean isExists = heroBannerRepository.existsById(heroBannerId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Hero banner with id %s not found", heroBannerId));
        }
        log.info("Do delete hero banner data");
        heroBannerRepository.deleteById(heroBannerId);
        log.info("Successful hero  banner data");
        return true;
    }
}
