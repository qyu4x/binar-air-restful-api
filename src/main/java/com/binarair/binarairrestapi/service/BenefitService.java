package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.BenefitRequest;
import com.binarair.binarairrestapi.model.request.BenefitUpdateRequest;
import com.binarair.binarairrestapi.model.response.BenefitDetailResponse;

import java.util.List;

public interface BenefitService {

    BenefitDetailResponse save(BenefitRequest benefitRequest);

    List<BenefitDetailResponse> getAll();

    List<BenefitDetailResponse> findByAircraftId(String aircraftId);

    Boolean delete(String benefitId);

    BenefitDetailResponse update(BenefitUpdateRequest benefitUpdateRequest, String benefitId);
}
