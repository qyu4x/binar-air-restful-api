package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.Facility;
import com.binarair.binarairrestapi.model.request.FacilityRequest;
import com.binarair.binarairrestapi.model.response.FacilityDetailResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.FacilityRepository;
import com.binarair.binarairrestapi.service.FacilityService;
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
public class FacilityServiceImpl implements FacilityService {

    private final static Logger log = LoggerFactory.getLogger(FacilityServiceImpl.class);

    private final FacilityRepository facilityRepository;

    private final AircraftRepository aircraftRepository;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository, AircraftRepository aircraftRepository) {
        this.facilityRepository = facilityRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public FacilityDetailResponse save(FacilityRequest facilityRequest) {
        Optional<Aircraft> aircraft = aircraftRepository.findById(facilityRequest.getAircraftId());
        if (aircraft.isEmpty()) {
            throw new DataNotFoundException(String.format("Aircraft with id %s not found", facilityRequest.getAircraftId()));
        }
        Facility facility = Facility.builder()
                .id(String.format("fy-%s", UUID.randomUUID().toString()))
                .name(facilityRequest.getName())
                .description(facilityRequest.getDesription())
                .status(facilityRequest.getStatus())
                .aircraft(aircraft.get())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save facility data");
        facilityRepository.save(facility);
        log.info("Succesful to save facility data");
        return FacilityDetailResponse.builder()
                .id(facility.getId())
                .name(facility.getName())
                .desription(facility.getDescription())
                .status(facility.isStatus())
                .aircraftManufacture(aircraft.get().getAircraftManufacture().getName())
                .aircraftModel(aircraft.get().getModel())
                .createdAt(facility.getCreatedAt())
                .build();
    }

    @Override
    public List<FacilityDetailResponse> getAll() {
        log.info("Do get all facility data");
        List<Facility> facilities = facilityRepository.findAll();
        log.info("Successful get all facility data");
        List<FacilityDetailResponse> facilityDetailResponses = new ArrayList<>();
        facilities.stream().forEach(facility -> {
            FacilityDetailResponse facilityDetailResponse = FacilityDetailResponse.builder()
                    .id(facility.getId())
                    .name(facility.getName())
                    .desription(facility.getDescription())
                    .status(facility.isStatus())
                    .createdAt(facility.getCreatedAt())
                    .build();
            facilityDetailResponses.add(facilityDetailResponse);
        });
        return facilityDetailResponses;
    }

    @Override
    public List<FacilityDetailResponse> findByAircraftId(String aircraftId) {
        log.info("Do get all data facility by id");
        List<Facility> facilitiesByAircraft = facilityRepository.findFacilitiesByAircraftId(aircraftId);
        log.info("Successful get all data facility");
        if(facilitiesByAircraft.isEmpty()) {
            throw new DataNotFoundException(String.format("Facility with id %s not found", aircraftId));
        }
        List<FacilityDetailResponse> facilityDetailResponses = new ArrayList<>();
        facilitiesByAircraft.stream().forEach(facility -> {
            Aircraft aircraft = aircraftRepository.findById(aircraftId)
                    .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", facility.getAircraft().getId())));
            FacilityDetailResponse facilityDetailResponse = FacilityDetailResponse.builder()
                    .id(facility.getId())
                    .name(facility.getName())
                    .desription(facility.getDescription())
                    .status(facility.isStatus())
                    .aircraftManufacture(aircraft.getAircraftManufacture().getName())
                    .aircraftModel(aircraft.getModel())
                    .createdAt(aircraft.getCreatedAt())
                    .build();
            facilityDetailResponses.add(facilityDetailResponse);
        });

        return facilityDetailResponses;
    }

    @Override
    public Boolean delete(String facilityId) {
        boolean isExists = facilityRepository.existsById(facilityId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Facility with id %s not found", facilityId));
        }
        log.info("Do delete facility data");
        facilityRepository.deleteById(facilityId);
        log.info("Successful delete facility data");
        return true;
    }
}
