package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Country;
import com.binarair.binarairrestapi.model.request.CountryRequest;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.model.response.CountryDetailResponse;
import com.binarair.binarairrestapi.model.response.CountryResponse;
import com.binarair.binarairrestapi.repository.CityRepository;
import com.binarair.binarairrestapi.repository.CountryRepository;
import com.binarair.binarairrestapi.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final static Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    private final CityRepository cityRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public CountryResponse save(CountryRequest countryRequest) {
        boolean isExists = countryRepository.existsById(countryRequest.getCountryCodeId());
        if (isExists) {
            throw new DataAlreadyExistException("Country is is already available in the database");
        }
        Country country = Country.builder()
                .countryCode(countryRequest.getCountryCodeId())
                .name(countryRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();
        countryRepository.save(country);
        log.info("Successfully save country data");
        return CountryResponse.builder()
                .countryCodeId(country.getCountryCode())
                .name(country.getName())
                .createdAt(country.getCreatedAt())
                .build();
    }

    @Override
    public List<CountryDetailResponse> getAll() {
        List<Country> countryResponse = countryRepository.findAll();
        List<CountryDetailResponse> countryDetailResponses = new ArrayList<>();
        countryResponse.stream().forEach(country -> {
            List<City> cities = cityRepository.findAllByCountry(country.getCountryCode());
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
            CountryDetailResponse countryDetail = CountryDetailResponse.builder()
                    .countryCodeId(country.getCountryCode())
                    .name(country.getName())
                    .cityResponses(cityResponses)
                    .createdAt(country.getCreatedAt())
                    .build();
            countryDetailResponses.add(countryDetail);
        });
        return countryDetailResponses;
    }

    @Override
    public boolean delete(String countryCodeId) {
        boolean isExists = countryRepository.existsById(countryCodeId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Country data with code %s not found", countryCodeId));
        }
        countryRepository.deleteById(countryCodeId);
        return true;
    }

    @Override
    public CountryDetailResponse findByCodeId(String countryCodeId) {
        log.info("Do get country by id");
        Country country = countryRepository.findById(countryCodeId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Country data with code %s not found", countryCodeId)));
        List<City> cities = cityRepository.findAllByCountry(country.getCountryCode());
        List<CityResponse> cityResponses = new ArrayList<>();
        cities.forEach(city -> {
            CityResponse cityResponse = CityResponse.builder()
                    .cityId(city.getCodeId())
                    .cityName(city.getName())
                    .countryCodeId(city.getCountry().getCountryCode())
                    .countryName(city.getCountry().getName())
                    .createdAt(city.getCreatedAt())
                    .build();
            cityResponses.add(cityResponse);
        });
        log.info("Successful get data country");
        return CountryDetailResponse.builder()
                .countryCodeId(country.getCountryCode())
                .name(country.getName())
                .cityResponses(cityResponses)
                .createdAt(country.getCreatedAt())
                .build();
    }

}
