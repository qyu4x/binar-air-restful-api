package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AircraftRequest;
import com.binarair.binarairrestapi.model.response.AircraftResponse;
import com.binarair.binarairrestapi.model.response.AircraftResponseDetail;

import java.util.List;

public interface AircraftService {

    AircraftResponseDetail save(AircraftRequest aircraftRequest);

    List<AircraftResponseDetail> getAll();

    AircraftResponseDetail findById(String aircraftId);

}
