package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.Benefit;
import com.binarair.binarairrestapi.model.request.BenefitRequest;
import com.binarair.binarairrestapi.model.request.BenefitUpdateRequest;
import com.binarair.binarairrestapi.model.response.BenefitDetailResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.BenefitRepository;
import com.binarair.binarairrestapi.service.BenefitService;
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
public class BenefitServiceImpl implements BenefitService {

    private final static Logger log = LoggerFactory.getLogger(BenefitServiceImpl.class);

    private final BenefitRepository benefitRepository;
    private final AircraftRepository aircraftRepository;

    @Autowired
    public BenefitServiceImpl(BenefitRepository benefitRepository, AircraftRepository aircraftRepository) {
        this.benefitRepository = benefitRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public BenefitDetailResponse save(BenefitRequest benefitRequest) {
        Optional<Aircraft> aircraft = aircraftRepository.findById(benefitRequest.getAircraftId());
        if (aircraft.isEmpty()) {
            throw new DataNotFoundException(String.format("Aircraft with id %s not found", benefitRequest.getAircraftId()));
        }
        Benefit benefit = Benefit.builder()
                .id(String.format("bt-%s", UUID.randomUUID().toString()))
                .name(benefitRequest.getName())
                .description(benefitRequest.getDescription())
                .status(benefitRequest.getStatus())
                .aircraft(aircraft.get())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save benefit data");
        benefitRepository.save(benefit);
        log.info("Successful save benefit data");
        return BenefitDetailResponse.builder()
                .id(benefit.getId())
                .name(benefit.getName())
                .desription(benefit.getDescription())
                .status(benefit.isStatus())
                .aircraftModel(aircraft.get().getModel())
                .aircraftManufacture(aircraft.get().getAircraftManufacture().getName())
                .build();
    }

    @Override
    public List<BenefitDetailResponse> getAll() {
        log.info("Do get all benefit data");
        List<Benefit> benefits = benefitRepository.findAll();
        List<BenefitDetailResponse> benefitDetailResponses = new ArrayList<>();
        benefits.stream().forEach(benefit -> {
            BenefitDetailResponse benefitResponse = BenefitDetailResponse.builder()
                    .id(benefit.getId())
                    .name(benefit.getName())
                    .desription(benefit.getDescription())
                    .status(benefit.isStatus())
                    .build();
            benefitDetailResponses.add(benefitResponse);
        });
        log.info("Successful get all benefit data");
        return benefitDetailResponses;
    }

    @Override
    public List<BenefitDetailResponse> findByAircraftId(String aircraftId) {
        log.info("Do get all data benefit by id");
        List<Benefit> benefitByAircrafts = benefitRepository.findBenefitByAircraftId(aircraftId);
        log.info("Successful get all benefit");
        if(benefitByAircrafts.isEmpty()) {
            throw new DataNotFoundException(String.format("Benefit aircraft with id %s not found", aircraftId));
        }
        List<BenefitDetailResponse> benefitDetailResponses = new ArrayList<>();
        benefitByAircrafts.stream().forEach(benefit -> {
            Aircraft aircraft = aircraftRepository.findById(aircraftId)
                    .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", benefit.getAircraft().getId())));
            BenefitDetailResponse benefitDetailResponse = BenefitDetailResponse.builder()
                    .id(benefit.getId())
                    .name(benefit.getName())
                    .desription(benefit.getDescription())
                    .status(benefit.isStatus())
                    .aircraftManufacture(aircraft.getAircraftManufacture().getName())
                    .aircraftModel(aircraft.getModel())
                    .createdAt(aircraft.getCreatedAt())
                    .build();
            benefitDetailResponses.add(benefitDetailResponse);
        });

        return benefitDetailResponses;
    }

    @Override
    public Boolean delete(String benefitId) {
        boolean isExists = benefitRepository.existsById(benefitId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Benefit with id %s not found", benefitId));
        }
        log.info("Do delete benefit by id");
        benefitRepository.deleteById(benefitId);
        log.info("Successful delete benefit by id");
        return true;
    }

    @Override
    public BenefitDetailResponse update(BenefitUpdateRequest benefitUpdateRequest, String benefitId) {
        Benefit benefit = benefitRepository.findById(benefitId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Benefit with id %s not found", benefitId)));
        log.info("Do update data benefit");
        benefit.setName(benefitUpdateRequest.getName());
        benefit.setDescription(benefitUpdateRequest.getDescription());
        benefit.setStatus(benefitUpdateRequest.getStatus());
        benefitRepository.save(benefit);
        log.info("Successful update benefit data");
        return BenefitDetailResponse.builder()
                .id(benefit.getId())
                .name(benefit.getName())
                .desription(benefit.getDescription())
                .status(benefit.isStatus())
                .aircraftManufacture(benefit.getAircraft().getAircraftManufacture().getName())
                .aircraftModel(benefit.getAircraft().getModel())
                .createdAt(benefit.getCreatedAt())
                .build();
    }
}
