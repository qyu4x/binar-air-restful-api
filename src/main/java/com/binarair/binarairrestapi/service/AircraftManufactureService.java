package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AircraftManufactureRequest;
import com.binarair.binarairrestapi.model.response.AircraftManufactureResponse;

import java.util.List;

public interface AircraftManufactureService {

    AircraftManufactureResponse save(AircraftManufactureRequest aircraftManufactureRequest);

    List<AircraftManufactureResponse> getAll();

    Boolean delete(String id);

}
