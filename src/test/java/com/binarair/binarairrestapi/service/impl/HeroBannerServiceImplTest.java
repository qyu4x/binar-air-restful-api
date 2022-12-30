package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.HeroBanner;
import com.binarair.binarairrestapi.model.request.HeroBannerRequest;
import com.binarair.binarairrestapi.model.response.HeroBannerResponse;
import com.binarair.binarairrestapi.repository.HeroBannerRepository;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class HeroBannerServiceImplTest {

    @Mock
    private HeroBannerRepository heroBannerRepository;

    @Mock
    private FirebaseStorageFileService firebaseStorageFileService;

    @InjectMocks
    private HeroBannerServiceImpl heroBannerService;

    @Test
    void testSaveHeroBannerSuccess() {
        HeroBannerRequest heroBannerRequest = HeroBannerRequest.builder()
                .title("Test hero banner")
                .description("Test hero banner desc")
                .build();

        HeroBanner heroBanner = HeroBanner.builder()
                .id("random-hero-banner-id")
                .title(heroBannerRequest.getTitle())
                .description(heroBannerRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("data", "image.jpg", "application/octet-stream", "some image".getBytes());

        Mockito.when(firebaseStorageFileService.doUploadFile(multipartFile))
                .thenReturn("https://yourimage.com?media=image");
        Mockito.when(heroBannerRepository.save(ArgumentMatchers.any(HeroBanner.class)))
                .thenReturn(heroBanner);

        HeroBannerResponse heroBannerResponse = heroBannerService.save(heroBannerRequest, multipartFile);
        Assertions.assertNotNull(heroBannerResponse);
        Assertions.assertEquals(heroBannerRequest.getTitle(), heroBannerResponse.getTitle());
        Assertions.assertEquals(heroBannerRequest.getDescription(), heroBannerResponse.getDescription());

        Mockito.verify(firebaseStorageFileService).doUploadFile(multipartFile);
        Mockito.verify(heroBannerRepository).save(ArgumentMatchers.any(HeroBanner.class));

    }

    @Test
    void testSaveHeroBannerMultipartfileNotFound() {
        HeroBannerRequest heroBannerRequest = HeroBannerRequest.builder()
                .title("Test hero banner")
                .description("Test hero banner desc")
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("data", "", "application/octet-stream", "".getBytes());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            heroBannerService.save(heroBannerRequest, multipartFile);
        });
    }

    @Test
    void testGetAllHeroBannerSuccess() {
        HeroBanner heroBanner1 = HeroBanner.builder()
                .id("random-hero-banner-id")
                .title("title")
                .description("test title")
                .imageURL("https://testimageurl.com?media=image")
                .createdAt(LocalDateTime.now())
                .build();

        HeroBanner heroBanner2 = HeroBanner.builder()
                .id("random-hero-banner-id")
                .title("title2")
                .description("test title3")
                .imageURL("https://testimageurl.com?media=image")
                .createdAt(LocalDateTime.now())
                .build();

        List<HeroBanner> heroBanners = new ArrayList<>();
        heroBanners.add(heroBanner1);
        heroBanners.add(heroBanner2);

        Mockito.when(heroBannerRepository.findAll())
                .thenReturn(heroBanners);

        List<HeroBannerResponse> heroBannerResponses = heroBannerService.getAll();
        Assertions.assertNotNull(heroBannerResponses);
        Assertions.assertEquals(2, heroBannerResponses.size());

        Mockito.verify(heroBannerRepository).findAll();

    }

    @Test
    void testDeleteHeroBannerSuccess() {
        String heroBannerId = "random-herobanner-id";
        Mockito.when(heroBannerRepository.existsById(heroBannerId))
                .thenReturn(true);

        Boolean status = heroBannerService.delete(heroBannerId);
        Assertions.assertTrue(status);

        Mockito.verify(heroBannerRepository).deleteById(heroBannerId);
    }

    @Test
    void testDeleteHeroBannerNotFound() {
        String heroBannerId = "random-herobanner-id";

        Mockito.when(heroBannerRepository.existsById(heroBannerId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            heroBannerService.delete(heroBannerId);
        });
    }
}