package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;

import java.util.List;

public interface TravelClassService {
    TravelClassResponse save(TravelClassRequest travelClassRequest);

    List<TravelClassResponse> getAll();

}
