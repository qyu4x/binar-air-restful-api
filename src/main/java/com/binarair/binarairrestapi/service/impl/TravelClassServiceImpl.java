package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.model.entity.TravelClass;
import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;
import com.binarair.binarairrestapi.repository.TravelClassRepository;
import com.binarair.binarairrestapi.service.TravelClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TravelClassServiceImpl implements TravelClassService {

    private final static Logger log = LoggerFactory.getLogger(TravelClassServiceImpl.class);

    private final TravelClassRepository travelClassRepository;

    @Autowired
    public TravelClassServiceImpl(TravelClassRepository travelClassRepository) {
        this.travelClassRepository = travelClassRepository;
    }

    @Override
    public TravelClassResponse save(TravelClassRequest travelClassRequest) {
        boolean isExists = travelClassRepository.existsById(travelClassRequest.getTravelClassId());
        if (isExists) {
            throw new DataAlreadyExistException(String.format("Travel class with id %s is already exists", travelClassRequest.getTravelClassId()));
        }
        TravelClass travelClass = TravelClass.builder()
                .id(travelClassRequest.getTravelClassId().toUpperCase())
                .name(travelClassRequest.getTravelClassName())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save travel data");
        travelClassRepository.save(travelClass);
        log.info("Successfull save travel data");
        return TravelClassResponse.builder()
                .travelClassId(travelClass.getId())
                .travelClassName(travelClass.getName())
                .createdAt(travelClass.getCreatedAt())
                .build();
    }

    @Override
    public List<TravelClassResponse> getAll() {
        log.info("Do get all travel data");
        List<TravelClass> travelClasses = travelClassRepository.findAll();
        List<TravelClassResponse> travelClassResponses = new ArrayList<>();
        travelClasses.stream().forEach(travelClass -> {
            TravelClassResponse travelClassResponse = TravelClassResponse.builder()
                    .travelClassId(travelClass.getId())
                    .travelClassName(travelClass.getName())
                    .createdAt(travelClass.getCreatedAt())
                    .build();
            travelClassResponses.add(travelClassResponse);
        });
        log.info("Successful get all travel data");
        return travelClassResponses;
    }
}
