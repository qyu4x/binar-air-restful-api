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
import java.util.Collections;
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
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", bagageRequest.getAircraftId())));
        Bagage bagage = Bagage.builder()
                .id(String.format("bg-%s", UUID.randomUUID().toString()))
                .weight(bagageRequest.getWeight())
                .price(bagageRequest.getPrice())
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
                .weight(bagage.getWeight())
                .price(bagage.getPrice())
                .freeCabinCapacity(bagage.getFreeCabinBagageCapacity())
                .freeBagageCapacity(bagage.getFreeBagageCapacity())
                .aircraftModel(aircraft.getModel())
                .aircraftManufacture(aircraft.getAircraftManufacture().getName())
                .createdAt(bagage.getCreatedAt())
                .build();
    }

    @Override
    public List<BagageResponse> findBagageByAircraftId(String id) {
        List<BagageResponse> bagageResponses = new ArrayList<>();
        log.info("Do get all data from bagage with aircraft id {} ", id);
        List<Bagage> bagages = bagageRepository.findByAircraftId(id);
        bagages.stream().forEach(bagage -> {
            BagageResponse bagageResponse = BagageResponse.builder()
                    .id(bagage.getId())
                    .weight(bagage.getWeight())
                    .price(bagage.getPrice())
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
    public Boolean delete(String baggageId) {
        boolean isExists = bagageRepository.existsById(baggageId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Baggage with id %s not found", baggageId));
        }
        log.info("Do delete baggage data");
        bagageRepository.deleteById(baggageId);
        log.info("Successful delete baggage data");
        return true;
    }
}
