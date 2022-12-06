package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Airlines;
import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.response.AirlineResponse;
import com.binarair.binarairrestapi.repository.AirlineRepository;
import com.binarair.binarairrestapi.service.AirlineService;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AirlineServiceImpl implements AirlineService {

    private final static Logger log = LoggerFactory.getLogger(AirlineServiceImpl.class);

    private final AirlineRepository airlineRepository;

    private final FirebaseStorageFileService firebaseStorageFileService;

    @Autowired
    public AirlineServiceImpl(AirlineRepository airlineRepository, FirebaseStorageFileService firebaseStorageFileService) {
        this.airlineRepository = airlineRepository;
        this.firebaseStorageFileService = firebaseStorageFileService;
    }

    @Override
    public AirlineResponse save(AirlineRequest airlineRequest, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new DataNotFoundException("Opps, please choose a picture first");
        }
        Airlines airline = Airlines.builder()
                .id(String.format("al-%s", UUID.randomUUID().toString()))
                .name(airlineRequest.getAirlineName())
                .description(airlineRequest.getDescription())
                .logoURL(firebaseStorageFileService.doUploadFile(multipartFile))
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save airline data into database");
        airlineRepository.save(airline);
        log.info("Successful save airline data");
        return AirlineResponse.builder()
                .id(airline.getId())
                .airlineName(airline.getName())
                .description(airline.getDescription())
                .logoURL(airline.getLogoURL())
                .createdAt(airline.getCreatedAt())
                .build();
    }

    @Override
    public List<AirlineResponse> getAll() {
        log.info("Do get all data from database");
        List<Airlines> airlines = airlineRepository.findAll();
        log.info("Successful get all data from database");
        List<AirlineResponse> airlineResponses = new ArrayList<>();
        airlines.stream().forEach(airline -> {
            AirlineResponse airlineResponse = AirlineResponse.builder()
                    .id(airline.getId())
                    .airlineName(airline.getName())
                    .description(airline.getDescription())
                    .logoURL(airline.getLogoURL())
                    .createdAt(airline.getCreatedAt())
                    .build();
            airlineResponses.add(airlineResponse);
        });
        return airlineResponses;
    }

    @Override
    public AirlineResponse update(AirlineRequest airlineRequest, String airlineId) {
        Airlines airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Airline with id %s not found", airlineId)));
        airline.setName(airlineRequest.getAirlineName());
        log.info("Do update airline data");
        airline.setDescription(airlineRequest.getDescription());
        log.info("Successful update airline data");
        airlineRepository.save(airline);
        return AirlineResponse.builder()
                .id(airline.getId())
                .airlineName(airline.getName())
                .description(airline.getDescription())
                .logoURL(airline.getLogoURL())
                .createdAt(airline.getCreatedAt())
                .build();
    }

    @Override
    public Boolean delete(String airlineId) {
        boolean isExists = airlineRepository.existsById(airlineId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Airline with id %s not found", airlineId));
        }
        log.info("Do delete data aircraft");
        airlineRepository.deleteById(airlineId);
        log.info("Successful delete data airline");
        return true;
    }

    @Override
    public AirlineResponse findById(String airlineId) {
        log.info("Do get data airline by id");
        Airlines airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Airline with id %s not found", airlineId)));
        log.info("Successful get airline data");
        return AirlineResponse.builder()
                .id(airline.getId())
                .airlineName(airline.getName())
                .description(airline.getDescription())
                .logoURL(airline.getLogoURL())
                .createdAt(airline.getCreatedAt())
                .build();
    }
}
