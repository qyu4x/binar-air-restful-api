package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.response.AirportResponse;

import java.util.List;

public interface AirportService {

    AirportResponse save(AirportRequest airportRequest);

    AirportResponse findByIata(String iata);
    List<AirportResponse> getAll();

    Boolean delete(String iata);

}
