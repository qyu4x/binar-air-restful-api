package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.request.AircraftManufactureRequest;
import com.binarair.binarairrestapi.model.response.AircraftManufactureResponse;
import com.binarair.binarairrestapi.repository.AircraftManufactureRepository;
import com.binarair.binarairrestapi.service.AircraftManufactureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AircraftManufactureServiceImpl implements AircraftManufactureService {

    private final static Logger log = LoggerFactory.getLogger(AircraftManufactureServiceImpl.class);

    private final AircraftManufactureRepository aircraftManufactureRepository;

    @Autowired
    public AircraftManufactureServiceImpl(AircraftManufactureRepository aircraftManufactureRepository) {
        this.aircraftManufactureRepository = aircraftManufactureRepository;
    }


    @Override
    public AircraftManufactureResponse save(AircraftManufactureRequest aircraftManufactureRequest) {
        Optional<AircraftManufacture> airCraftManufacture = aircraftManufactureRepository.findByName(aircraftManufactureRequest.getName());
        if (airCraftManufacture.isPresent()) {
            throw new DataAlreadyExistException(String.format("Data aircraft manufacture with name %s already exists", aircraftManufactureRequest.getName()));
        }
        AircraftManufacture aircraftManufacture = AircraftManufacture.builder()
                .id(String.format("am-%s", UUID.randomUUID().toString()))
                .name(aircraftManufactureRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save data aircraft manufacture into database");
        aircraftManufactureRepository.save(aircraftManufacture);
        log.info("Successful save data aircraft manufacures");
        return AircraftManufactureResponse.builder()
                .id(aircraftManufacture.getId())
                .name(aircraftManufacture.getName())
                .createdAt(aircraftManufacture.getCreatedAt())
                .build();
    }

    @Override
    public List<AircraftManufactureResponse> getAll() {
        log.info("Do get all data aircraft manufacture");
        List<AircraftManufacture> aircraftManufactures = aircraftManufactureRepository.findAll();
        List<AircraftManufactureResponse> aircraftManufactureResponses = new ArrayList<>();
        aircraftManufactures.stream().forEach(aircraftManufacture -> {
            AircraftManufactureResponse aircraftManufactureResponse = AircraftManufactureResponse.builder()
                    .id(aircraftManufacture.getId())
                    .name(aircraftManufacture.getName())
                    .createdAt(aircraftManufacture.getCreatedAt())
                    .build();
            aircraftManufactureResponses.add(aircraftManufactureResponse);
        });
        log.info("Successful get all data aircraft manufacutes");
        return aircraftManufactureResponses;
    }

    @Override
    public Boolean delete(String id) {
        boolean isExists = aircraftManufactureRepository.existsById(id);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Aircraft manufacture with id %s not found", id));
        }
        log.info("Do delete aircraft manufacture data");
        aircraftManufactureRepository.deleteById(id);
        log.info("Successful delete aircraft manufacture data");
        return true;
    }
}
