package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.entity.TravelClass;
import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.request.TravelClassUpdateRequest;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;

import java.util.List;

public interface TravelClassService {

    TravelClassResponse save(TravelClassRequest travelClassRequest);

    List<TravelClassResponse> getAll();

    TravelClassResponse findById(String travelId);

    Boolean delete(String travelId);

    TravelClassResponse update(TravelClassUpdateRequest travelClassRequest, String travelId);

}
