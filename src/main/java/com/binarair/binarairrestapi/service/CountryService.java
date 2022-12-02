package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.CountryRequest;
import com.binarair.binarairrestapi.model.response.CountryDetailResponse;
import com.binarair.binarairrestapi.model.response.CountryResponse;

import java.util.List;

public interface CountryService {

    CountryResponse save(CountryRequest countryRequest);

    List<CountryDetailResponse> getAll();

    boolean delete(String countryCodeId);

    CountryDetailResponse findByCodeId(String countryCodeId);

}
