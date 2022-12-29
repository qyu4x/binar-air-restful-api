package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.request.CityRequest;
import com.binarair.binarairrestapi.model.response.CityResponse;

import java.util.List;

public interface CityService {

    CityResponse save(CityRequest cityRequest);

    boolean delete(String cityCode);

    List<CityResponse> getAll();

    CityResponse findByCode(String cityId);

}
