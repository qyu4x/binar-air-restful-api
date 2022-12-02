package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.TravelClass;
import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.request.TravelClassUpdateRequest;
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

    @Override
    public TravelClassResponse findById(String travelId) {
        log.info("Do get data travel class");
        TravelClass travelClass = travelClassRepository.findById(travelId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Travel class %s not found", travelId)));
        return TravelClassResponse.builder()
                .travelClassId(travelClass.getId())
                .travelClassName(travelClass.getName())
                .createdAt(travelClass.getCreatedAt())
                .build();
    }

    @Override
    public Boolean delete(String travelId) {
        boolean isExists = travelClassRepository.existsById(travelId);
        if (!isExists) {
            log.info("Travel class not found");
            throw new DataNotFoundException(String.format("Travel class %s not found", travelId));
        }
        log.info("Do delete travel class data");
        travelClassRepository.deleteById(travelId);
        log.info("Successful delete travel class data");
        return true;
    }

    @Override
    public TravelClassResponse update(TravelClassUpdateRequest travelClassRequest, String travelId) {
        TravelClass travelClass =  travelClassRepository.findById(travelId)
                .orElseThrow(() ->  new DataNotFoundException(String.format("Travel class %s not found", travelId)));
        travelClass.setName(travelClassRequest.getTravelClassName());
        log.info("Do update travel class data");
        travelClassRepository.save(travelClass);
        log.info("Successful update travel class data");
        return TravelClassResponse.builder()
                .travelClassId(travelClass.getId())
                .travelClassName(travelClass.getName())
                .createdAt(travelClass.getCreatedAt())
                .build();
    }
}
