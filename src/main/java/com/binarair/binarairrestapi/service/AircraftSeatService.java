package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AircraftRequest;
import com.binarair.binarairrestapi.model.request.AircraftSeatRequest;
import com.binarair.binarairrestapi.model.response.AircraftResponse;
import com.binarair.binarairrestapi.model.response.AircraftSeatResponse;

import java.util.List;

public interface AircraftSeatService {

    List<AircraftSeatResponse> findAllAvailableSeat(String aircraftId);

    List<AircraftSeatResponse> findAllReservedSeat(String aircraftId);

    List<AircraftSeatResponse> getAllByAircraftId(String aircraftId);

    AircraftSeatResponse save(AircraftSeatRequest aircraftSeatRequest);

    Boolean delete(String id);
}
