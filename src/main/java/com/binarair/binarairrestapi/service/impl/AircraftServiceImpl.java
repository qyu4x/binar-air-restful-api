package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftManufacture;
import com.binarair.binarairrestapi.model.entity.Airlines;
import com.binarair.binarairrestapi.model.entity.TravelClass;
import com.binarair.binarairrestapi.model.request.AircraftRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.repository.AircraftManufactureRepository;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.AirlineRepository;
import com.binarair.binarairrestapi.repository.TravelClassRepository;
import com.binarair.binarairrestapi.service.AircraftService;
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
public class AircraftServiceImpl implements AircraftService {

    private final static Logger log = LoggerFactory.getLogger(AircraftServiceImpl.class);

    private final AircraftRepository aircraftRepository;

    private final AircraftManufactureRepository aircraftManufactureRepository;

    private final AirlineRepository airlineRepository;

    private final TravelClassRepository travelClassRepository;

    @Autowired
    public AircraftServiceImpl(AircraftRepository aircraftRepository, AircraftManufactureRepository aircraftManufactureRepository, AirlineRepository airlineRepository, TravelClassRepository travelClassRepository) {
        this.aircraftRepository = aircraftRepository;
        this.aircraftManufactureRepository = aircraftManufactureRepository;
        this.airlineRepository = airlineRepository;
        this.travelClassRepository = travelClassRepository;
    }

    @Override
    public AircraftDetailResponse save(AircraftRequest aircraftRequest) {
        AircraftManufacture aircraftManufacture =  aircraftManufactureRepository.findById(aircraftRequest.getAirCraftManufactureId())
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft manufacture with id %s not found", aircraftRequest.getAirCraftManufactureId())));

        TravelClass travelClass =  travelClassRepository.findById(aircraftRequest.getTravelClassId())
                .orElseThrow(() -> new DataNotFoundException(String.format("Travel class with id %s not found", aircraftRequest.getTravelClassId())));

        Airlines airline = airlineRepository.findById(aircraftRequest.getAirlinesId())
                .orElseThrow(() -> new DataNotFoundException(String.format("Airlines with id %s not found", aircraftRequest.getAirlinesId())));

        Optional<Aircraft> aircraftModel = aircraftRepository.findByModel(aircraftRequest.getModel());
        if (aircraftModel.isPresent()) {
            throw new DataAlreadyExistException(String.format("Aircraft with model %s already exists", aircraftRequest.getModel()));
        }

        Aircraft aircraft = Aircraft.builder()
                .id(String.format("ar-%s", UUID.randomUUID().toString()))
                .model(aircraftRequest.getModel())
                .passangerCapacity(aircraftRequest.getPassengerCapacity())
                .totalUnit(aircraftRequest.getTotalUnit())
                .seatArrangement(aircraftRequest.getSeatArrangement())
                .distanceBetweenSeats(aircraftRequest.getDistanceBetweenSeat())
                .seatLengthUnit(aircraftRequest.getSeatLengthUnit())
                .aircraftManufacture(aircraftManufacture)
                .active(true)
                .travelClass(travelClass)
                .airlines(airline)
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save aircraft data");
        aircraftRepository.save(aircraft);
        log.info("Successful save aircraft data");
        return AircraftDetailResponse.builder()
                .id(aircraft.getId())
                .model(aircraft.getModel())
                .passengerCapacity(aircraft.getPassangerCapacity())
                .totalUnit(aircraft.getTotalUnit())
                .seatArrangement(aircraftRequest.getSeatArrangement())
                .distanceBetweenSeat(aircraft.getDistanceBetweenSeats())
                .seatLengthUnit(aircraft.getSeatLengthUnit())
                .createdAt(aircraft.getCreatedAt())
                .travel(TravelClassResponse.builder()
                        .travelClassId(travelClass.getId())
                        .travelClassName(travelClass.getName())
                        .createdAt(travelClass.getCreatedAt())
                        .build())
                .airlines(AirlineResponse.builder()
                        .id(airline.getId())
                        .airlineName(airline.getName())
                        .logoURL(airline.getLogoURL())
                        .createdAt(airline.getCreatedAt())
                        .build())
                .manufacture(AircraftManufactureResponse.builder()
                        .id(aircraftManufacture.getId())
                        .name(aircraftManufacture.getName())
                        .createdAt(aircraftManufacture.getCreatedAt())
                        .build())
                .build();
    }

    @Override
    public List<AircraftDetailResponse> getAll() {
        log.info("Do get all aircraft data");
        List<Aircraft> aircrafts = aircraftRepository.findAll();
        List<AircraftDetailResponse> aircraftDetailResponses = new ArrayList<>();
        aircrafts.stream().forEach(aircraft -> {
            AircraftDetailResponse aircraftDetailResponse =  AircraftDetailResponse.builder()
                    .id(aircraft.getId())
                    .model(aircraft.getModel())
                    .passengerCapacity(aircraft.getPassangerCapacity())
                    .totalUnit(aircraft.getTotalUnit())
                    .seatArrangement(aircraft.getSeatArrangement())
                    .distanceBetweenSeat(aircraft.getDistanceBetweenSeats())
                    .seatLengthUnit(aircraft.getSeatLengthUnit())
                    .createdAt(aircraft.getCreatedAt())
                    .travel(TravelClassResponse.builder()
                            .travelClassId(aircraft.getTravelClass().getId())
                            .travelClassName(aircraft.getTravelClass().getName())
                            .createdAt(aircraft.getTravelClass().getCreatedAt())
                            .build())
                    .airlines(AirlineResponse.builder()
                            .id(aircraft.getAirlines().getId())
                            .airlineName(aircraft.getAirlines().getName())
                            .logoURL(aircraft.getAirlines().getLogoURL())
                            .createdAt(aircraft.getCreatedAt())
                            .build())
                    .manufacture(AircraftManufactureResponse.builder()
                            .id(aircraft.getAircraftManufacture().getId())
                            .name(aircraft.getAircraftManufacture().getName())
                            .createdAt(aircraft.getAircraftManufacture().getCreatedAt())
                            .build())
                    .build();
            aircraftDetailResponses.add(aircraftDetailResponse);
        });
        log.info("Successful get all aircraft data");
        return aircraftDetailResponses;
    }

    @Override
    public AircraftDetailResponse findById(String aircraftId) {
        log.info("Do get aircraft data by id");
        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Data aircraft with id %s not found", aircraftId)));
        log.info("Successful get aircraft data by id");
        return AircraftDetailResponse.builder()
                .id(aircraft.getId())
                .model(aircraft.getModel())
                .passengerCapacity(aircraft.getPassangerCapacity())
                .totalUnit(aircraft.getTotalUnit())
                .seatArrangement(aircraft.getSeatArrangement())
                .distanceBetweenSeat(aircraft.getDistanceBetweenSeats())
                .seatLengthUnit(aircraft.getSeatLengthUnit())
                .createdAt(aircraft.getCreatedAt())
                .travel(TravelClassResponse.builder()
                        .travelClassId(aircraft.getTravelClass().getId())
                        .travelClassName(aircraft.getTravelClass().getName())
                        .createdAt(aircraft.getTravelClass().getCreatedAt())
                        .build())
                .airlines(AirlineResponse.builder()
                        .id(aircraft.getAirlines().getId())
                        .airlineName(aircraft.getAirlines().getName())
                        .logoURL(aircraft.getAirlines().getLogoURL())
                        .createdAt(aircraft.getCreatedAt())
                        .build())
                .manufacture(AircraftManufactureResponse.builder()
                        .id(aircraft.getAircraftManufacture().getId())
                        .name(aircraft.getAircraftManufacture().getName())
                        .createdAt(aircraft.getAircraftManufacture().getCreatedAt())
                        .build())
                .build();
    }

    @Override
    public Boolean delete(String aircraftId) {
        boolean isExists = aircraftRepository.existsById(aircraftId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Aircraft with id %s not found", aircraftId));
        }
        log.info("Do delete aircraft data");
        aircraftRepository.deleteById(aircraftId);
        log.info("Successful delete aircraft data");
        return true;
    }
}
