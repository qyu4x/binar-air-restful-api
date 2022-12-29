package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.FacilityRequest;
import com.binarair.binarairrestapi.model.response.FacilityDetailResponse;

import java.util.List;

public interface FacilityService {

    FacilityDetailResponse save(FacilityRequest facilityRequest);

    List<FacilityDetailResponse> getAll();

    List<FacilityDetailResponse> findByAircraftId(String aircraftId);

    Boolean delete(String facilityId);


}
