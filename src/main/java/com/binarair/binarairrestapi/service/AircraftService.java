package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AircraftRequest;
import com.binarair.binarairrestapi.model.response.AircraftDetailResponse;

import java.util.List;

public interface AircraftService {

    AircraftDetailResponse save(AircraftRequest aircraftRequest);

    List<AircraftDetailResponse> getAll();

    AircraftDetailResponse findById(String aircraftId);

    Boolean delete(String aircraftId);

}
