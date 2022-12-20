package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.PromoBanner;
import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import com.binarair.binarairrestapi.repository.PromoBannerRepository;
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

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class PromoBannerServiceImplTest {

    @Mock
    private PromoBannerRepository promoBannerRepository;

    @Mock
    private FirebaseStorageFileService firebaseStorageFileService;

    @InjectMocks
    private PromoBannerServiceImpl promoBannerService;

    @Test
    void testSavePromoBannerSuccess() {
        PromoBannerRequest promoBannerRequest = PromoBannerRequest.builder()
                .title("Promo 12.12")
                .description("Promo 12.12")
                .build();

        PromoBanner promoBanner = PromoBanner.builder()
                .id("random-promobanner")
                .title(promoBannerRequest.getTitle())
                .description(promoBannerRequest.getDescription())
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("data", "image.jpg", "application/octet-stream", "some image".getBytes());

        Mockito.when(firebaseStorageFileService.doUploadFile(multipartFile))
                .thenReturn("https://promobannerimage.com?media=img");
        Mockito.when(promoBannerRepository.save(ArgumentMatchers.any(PromoBanner.class)))
                .thenReturn(promoBanner);

        PromoBannerResponse promoBannerResponse = promoBannerService.save(promoBannerRequest, multipartFile);
        Assertions.assertEquals(promoBannerRequest.getTitle(), promoBannerResponse.getTitle());
        Assertions.assertEquals(promoBannerRequest.getDescription(), promoBannerResponse.getDescription());
        Assertions.assertEquals("https://promobannerimage.com?media=img", promoBannerResponse.getImageURL());

        Mockito.verify(firebaseStorageFileService).doUploadFile(multipartFile);
        Mockito.verify(promoBannerRepository).save(ArgumentMatchers.any(PromoBanner.class));

    }

    @Test
    void testPromoBannerMultipartFileNotFound() {
        PromoBannerRequest promoBannerRequest = PromoBannerRequest.builder()
                .title("Promo 12.12")
                .description("Promo 12.12")
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("data", "", "application/octet-stream", "".getBytes());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            promoBannerService.save(promoBannerRequest, multipartFile);
        });

    }

    @Test
    void testFindScheduleById() {
        PromoBanner promoBanner = PromoBanner.builder()
                .id("random-promobanner")
                .title("promo 12.12")
                .description("promo 12.12")
                .imageURL("https://promobanner.com?media=img")
                .build();
        Mockito.when(promoBannerRepository.findById(promoBanner.getId()))
                .thenReturn(Optional.of(promoBanner));
        PromoBannerResponse promoBannerResponse = promoBannerService.findById(promoBanner.getId());
        Assertions.assertNotNull(promoBannerResponse);
        Assertions.assertEquals(promoBanner.getTitle(), promoBannerResponse.getTitle());
        Assertions.assertEquals(promoBanner.getDescription(), promoBannerResponse.getDescription());

        Mockito.verify(promoBannerRepository).findById(promoBanner.getId());

    }

    @Test
    void testFindScheduleByIdNotFound() {
        String promoBannerId = "random-promobanner";
        Mockito.when(promoBannerRepository.findById(promoBannerId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            promoBannerService.findById(promoBannerId);
        });

        Mockito.verify(promoBannerRepository).findById(promoBannerId);

    }

    @Test
    void testUpdateScheduleSuccess() {
        String promoBannerId = "random-promobanner";

        PromoBannerRequest promoBannerRequest = PromoBannerRequest.builder()
                .title("Promo 12.12")
                .description("Promo 12.12")
                .build();

        PromoBanner promoBanner = PromoBanner.builder()
                .id("random-promobanner")
                .title("random title")
                .description("random desc")
                .build();

        Mockito.when(promoBannerRepository.findById(promoBannerId))
                .thenReturn(Optional.of(promoBanner));

        PromoBannerResponse updateResponse = promoBannerService.update(promoBannerRequest, promoBannerId);
        Assertions.assertNotNull(updateResponse);
        Assertions.assertEquals(promoBannerRequest.getTitle(), updateResponse.getTitle());
        Assertions.assertEquals(promoBannerRequest.getDescription(), updateResponse.getDescription());

        Mockito.verify(promoBannerRepository).findById(promoBannerId);

    }

    @Test
    void testUpdateScheduleNotFound() {
        String promoBannerId = "random-promobanner";
        PromoBannerRequest promoBannerRequest = PromoBannerRequest.builder()
                .title("Promo 12.12")
                .description("Promo 12.12")
                .build();

        Mockito.when(promoBannerRepository.findById(promoBannerId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            promoBannerService.update(promoBannerRequest, promoBannerId);
        });

        Mockito.verify(promoBannerRepository).findById(promoBannerId);

    }

    @Test
    void testDeleteNotFound() {
        String promoBannerId = "random-promobanner";
        Mockito.when(promoBannerRepository.existsById(promoBannerId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
           promoBannerService.delete(promoBannerId);
        });

        Mockito.verify(promoBannerRepository).existsById(promoBannerId);

    }

    @Test
    void testPromoBannerSuccess() {
        String promoBannerId = "random-promobanner";
        Mockito.when(promoBannerRepository.existsById(promoBannerId))
                .thenReturn(true);

        Boolean status = promoBannerService.delete(promoBannerId);
        Assertions.assertTrue(status);

        Mockito.verify(promoBannerRepository).existsById(promoBannerId);
        Mockito.verify(promoBannerRepository).deleteById(promoBannerId);

    }
}