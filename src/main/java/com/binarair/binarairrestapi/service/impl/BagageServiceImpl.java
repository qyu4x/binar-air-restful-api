package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.Bagage;
import com.binarair.binarairrestapi.model.request.BagageRequest;
import com.binarair.binarairrestapi.model.response.BagageResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.BagageRepository;
import com.binarair.binarairrestapi.service.BagageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BagageServiceImpl implements BagageService {

    private final static Logger log = LoggerFactory.getLogger(BagageServiceImpl.class);

    private final BagageRepository bagageRepository;

    private final AircraftRepository aircraftRepository;

    @Autowired
    public BagageServiceImpl(BagageRepository bagageRepository, AircraftRepository aircraftRepository) {
        this.bagageRepository = bagageRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public BagageResponse save(BagageRequest bagageRequest) {
        Aircraft aircraft = aircraftRepository.findById(bagageRequest.getAircraftId())
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found")));
        Bagage isExists = bagageRepository.findByAircraftId(bagageRequest.getAircraftId());
        if (isExists != null) {
            throw new DataAlreadyExistException(String.format("Bagage with aircraft id %s already exists", bagageRequest.getAircraftId()));
        }
        Bagage bagage = Bagage.builder()
                .id(String.format("bg-%s", UUID.randomUUID().toString()))
                .bagagePricePer5kg(bagageRequest.getBagagePricePer5kg())
                .freeBagageCapacity(bagageRequest.getFreeBagageCapacity())
                .freeCabinBagageCapacity(bagageRequest.getFreeCabinCapacity())
                .createdAt(LocalDateTime.now())
                .aircraft(aircraft)
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save bagage data");
        bagageRepository.save(bagage);
        log.info("Successful save bagage data");
        return BagageResponse.builder()
                .id(bagage.getId())
                .bagagePricePer5kg(bagage.getBagagePricePer5kg())
                .freeCabinCapacity(bagage.getFreeCabinBagageCapacity())
                .freeBagageCapacity(bagage.getFreeBagageCapacity())
                .aircraftModel(aircraft.getModel())
                .aircraftManufacture(aircraft.getAircraftManufacture().getName())
                .createdAt(bagage.getCreatedAt())
                .build();
    }

    @Override
    public List<BagageResponse> getAll() {
        List<BagageResponse> bagageResponses = new ArrayList<>();
        log.info("Do get all data from bagage");
        List<Bagage> bagages = bagageRepository.findAll();
        bagages.stream().forEach(bagage -> {
            BagageResponse bagageResponse = BagageResponse.builder()
                    .id(bagage.getId())
                    .bagagePricePer5kg(bagage.getBagagePricePer5kg())
                    .freeCabinCapacity(bagage.getFreeCabinBagageCapacity())
                    .freeBagageCapacity(bagage.getFreeBagageCapacity())
                    .createdAt(bagage.getCreatedAt())
                    .build();
            bagageResponses.add(bagageResponse);
        });
        log.info("Sucessful get all data from bagage");
        return bagageResponses;
    }

    @Override
    public BagageResponse findBagageByAircraftId(String aircraftId) {
        log.info("Do get bagage by id aircraft");
        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found")));
        Bagage bagage = bagageRepository.findByAircraftId(aircraftId);
        return BagageResponse.builder()
                .id(bagage.getId())
                .bagagePricePer5kg(bagage.getBagagePricePer5kg())
                .freeCabinCapacity(bagage.getFreeCabinBagageCapacity())
                .freeBagageCapacity(bagage.getFreeBagageCapacity())
                .createdAt(bagage.getCreatedAt())
                .aircraftModel(aircraft.getModel())
                .aircraftManufacture(aircraft.getAircraftManufacture().getName())
                .build();
    }
}
