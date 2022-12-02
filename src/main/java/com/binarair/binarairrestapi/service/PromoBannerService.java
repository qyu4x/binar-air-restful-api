package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.response.PromoBannerPaggableResponse;
import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PromoBannerService {

    PromoBannerResponse save(PromoBannerRequest promoBannerRequest, MultipartFile multipartFile);

    Page<PromoBannerPaggableResponse> getAll(Pageable pageable);

    Boolean delete(String promoBannerId);

    PromoBannerResponse findById(String promoBannerId);


    PromoBannerResponse update(PromoBannerRequest promoBannerRequest, String promoBannerId);


}
