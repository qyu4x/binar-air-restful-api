package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Airport;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.response.AirportResponse;
import com.binarair.binarairrestapi.repository.AirportRepository;
import com.binarair.binarairrestapi.repository.CityRepository;
import com.binarair.binarairrestapi.service.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {

    private final static Logger log = LoggerFactory.getLogger(AirportServiceImpl.class);

    private final AirportRepository airportRepository;

    private final CityRepository cityRepository;

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository, CityRepository cityRepository) {
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public AirportResponse save(AirportRequest airportRequest) {
        log.info("do save airport data");
        boolean isExistsAirport = airportRepository.existsById(airportRequest.getIata());
        City cityResponse = cityRepository.findById(airportRequest.getCityCode())
                .orElseThrow(() ->  new DataNotFoundException(String.format("City data with code %s not found", airportRequest.getCityCode())));

        if (isExistsAirport) {
            throw new DataAlreadyExistException(String.format("Airport data with iata %s already exists", airportRequest.getIata()));
        }
        Airport airport = Airport.builder()
                .iataAirportCode(airportRequest.getIata())
                .name(airportRequest.getName())
                .city(cityResponse)
                .createdAt(LocalDateTime.now())
                .build();
        airportRepository.save(airport);
        log.info("Successful save airport data");
        return AirportResponse.builder()
                .iata(airport.getIataAirportCode())
                .name(airport.getName())
                .cityCode(cityResponse.getCodeId())
                .city(airport.getCity().getName())
                .countryCode(cityResponse.getCountry().getCountryCode())
                .country(cityResponse.getCountry().getName())
                .createdAt(airport.getCreatedAt())
                .build();
    }

    @Override
    public AirportResponse findByIata(String iata) {
       Airport airport = airportRepository.findById(iata)
                .orElseThrow(() -> new DataNotFoundException("Data airport not found"));
        return AirportResponse.builder()
                .iata(airport.getIataAirportCode())
                .name(airport.getName())
                .cityCode(airport.getCity().getCodeId())
                .city(airport.getCity().getName())
                .countryCode(airport.getCity().getCountry().getCountryCode())
                .country(airport.getCity().getCountry().getName())
                .createdAt(airport.getCreatedAt())
                .build();
    }

    @Override
    public List<AirportResponse> getAll() {
        log.info("Do get all airport data");
        List<Airport> airports = airportRepository.findAll();
        List<AirportResponse> airportResponses = new ArrayList<>();
        airports.stream().forEach(airport -> {
            AirportResponse airportResponse = AirportResponse.builder()
                    .iata(airport.getIataAirportCode())
                    .name(airport.getName())
                    .cityCode(airport.getCity().getCodeId())
                    .city(airport.getCity().getName())
                    .countryCode(airport.getCity().getCountry().getCountryCode())
                    .country(airport.getCity().getCountry().getName())
                    .createdAt(airport.getCreatedAt())
                    .build();
            airportResponses.add(airportResponse);
        });
        log.info("Successfull get all airport data");
        return airportResponses;
    }

    @Override
    public Boolean delete(String iata) {
        boolean isExists = airportRepository.existsById(iata);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Data airport with iata %s not found", iata));
        }
        log.info("Do delete data airport");
        airportRepository.deleteById(iata);
        log.info("Successful delete airport data");
        return true;
    }
}
