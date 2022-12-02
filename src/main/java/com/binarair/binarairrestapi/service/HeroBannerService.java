package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.HeroBannerRequest;
import com.binarair.binarairrestapi.model.response.HeroBannerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HeroBannerService {

    HeroBannerResponse save(HeroBannerRequest heroBannerRequest, MultipartFile multipartFile);

    List<HeroBannerResponse> getAll();

    Boolean delete(String heroBannerId);

}
