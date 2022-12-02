package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Country;
import com.binarair.binarairrestapi.model.request.CityRequest;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.repository.CityRepository;
import com.binarair.binarairrestapi.repository.CountryRepository;
import com.binarair.binarairrestapi.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final static Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;

    private final CountryRepository countryRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public CityResponse save(CityRequest cityRequest) {
        log.info("do save city data");
        Country countryResponse =  countryRepository.findById(cityRequest.getCountryCodeId())
                        .orElseThrow(() -> new DataNotFoundException(String.format("Country with id %s not found", cityRequest.getCountryCodeId())));
        City city = City.builder()
                .codeId(cityRequest.getCityId())
                .name(cityRequest.getName())
                .country(countryResponse)
                .createdAt(LocalDateTime.now())
                .build();
        cityRepository.save(city);
        log.info("successful save city data");
        return CityResponse.builder()
                .cityId(city.getCodeId())
                .cityName(city.getName())
                .countryCodeId(city.getCountry().getCountryCode())
                .countryName(countryResponse.getName())
                .createdAt(city.getCreatedAt())
                .build();
    }

    @Override
    public boolean delete(String cityCode) {
        boolean isExists = cityRepository.existsById(cityCode);
        if (!isExists) {
            throw new DataNotFoundException(String.format("City data with code id %s not found", cityCode));
        }
        cityRepository.deleteById(cityCode);
        return true;
    }

    @Override
    public List<CityResponse> getAll() {
        log.info("do get all data city");
        List<City> cities = cityRepository.findAll();
        List<CityResponse> cityResponses = new ArrayList<>();
        cities.stream().forEach(city -> {
            CityResponse cityResponse = CityResponse.builder()
                    .cityId(city.getCodeId())
                    .cityName(city.getName())
                    .countryCodeId(city.getCountry().getCountryCode())
                    .countryName(city.getCountry().getName())
                    .createdAt(city.getCreatedAt())
                    .build();
            cityResponses.add(cityResponse);
        });
        log.info("successful get all city data");
        return cityResponses;
    }

    @Override
    public CityResponse findByCode(String cityCode) {
        log.info("Do get all data city");
        City city = cityRepository.findById(cityCode)
                .orElseThrow(() -> new DataNotFoundException(String.format("City data with code id %s not found", cityCode)));
        log.info("Successful get all data city");
        return CityResponse.builder()
                .cityId(city.getCodeId())
                .cityName(city.getName())
                .countryCodeId(city.getCountry().getCountryCode())
                .countryName(city.getCountry().getName())
                .createdAt(city.getCreatedAt())
                .build();
    }
}
