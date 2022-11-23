package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PromoBannerService {

    PromoBannerResponse save(PromoBannerRequest promoBannerRequest, MultipartFile multipartFile);

    List<PromoBannerResponse> getAll();

}
