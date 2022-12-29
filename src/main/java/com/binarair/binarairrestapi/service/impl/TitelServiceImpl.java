package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Titel;
import com.binarair.binarairrestapi.model.request.TitelRequest;
import com.binarair.binarairrestapi.model.response.TitelResponse;
import com.binarair.binarairrestapi.repository.TitelRepository;
import com.binarair.binarairrestapi.service.TitelService;
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
public class TitelServiceImpl implements TitelService {

    private final static Logger log = LoggerFactory.getLogger(TitelServiceImpl.class);

    private final TitelRepository titelRepository;

    @Autowired
    public TitelServiceImpl(TitelRepository titelRepository) {
        this.titelRepository = titelRepository;
    }

    @Override
    public TitelResponse save(TitelRequest titelRequest) {
        Optional<Titel> titelName = titelRepository.findByTitelName(titelRequest.getTitelName());
        if (titelName.isPresent()) {

            throw new DataAlreadyExistException(String.format("Titel with name %s is already available", titelRequest.getTitelName()));
        }
        Titel titel = Titel.builder()
                .id(String.format("tl-%s", UUID.randomUUID().toString()))
                .titelName(titelRequest.getTitelName())
                .description(titelRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Do save titel data");
        titelRepository.save(titel);
        log.info("Successful save titel data");
        return TitelResponse.builder()
                .id(titel.getId())
                .titelName(titel.getTitelName())
                .description(titel.getDescription())
                .createdAt(titel.getCreatedAt())
                .build();
    }

    @Override
    public List<TitelResponse> getAll() {
        log.info("Do get all titel data");
        List<Titel> titels = titelRepository.findAll();
        List<TitelResponse> titelResponses = new ArrayList<>();
        titels.stream().forEach(titel -> {
            TitelResponse titelResponse = TitelResponse.builder()
                    .id(titel.getId())
                    .titelName(titel.getTitelName())
                    .description(titel.getDescription())
                    .createdAt(titel.getCreatedAt())
                    .build();
            titelResponses.add(titelResponse);
        });
        log.info("successful get all titel data");
        return titelResponses;
    }

    @Override
    public TitelResponse findById(String titelId) {
        log.info("Do get titel by id");
        Titel titel =  titelRepository.findById(titelId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Titel with id %s not found", titelId)));
        log.info("Successful get titel by id");
        return TitelResponse.builder()
                .id(titel.getId())
                .titelName(titel.getTitelName())
                .description(titel.getDescription())
                .createdAt(titel.getCreatedAt())
                .build();
    }

    @Override
    public Boolean delete(String titelId) {
        boolean isExists = titelRepository.existsById(titelId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Titel with id %s not found", titelId));
        }
        titelRepository.deleteById(titelId);
        return true;
    }

    @Override
    public TitelResponse update(TitelRequest titelRequest, String titelId) {
        Titel titel = titelRepository.findById(titelId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Titel with id %s not found", titelId)));
        titel.setTitelName(titelRequest.getTitelName());
        titel.setDescription(titelRequest.getDescription());
        log.info("Do update titel data");
        titelRepository.save(titel);
        log.info("Successful update titel data");
        return TitelResponse.builder()
                .id(titel.getId())
                .titelName(titel.getTitelName())
                .description(titel.getDescription())
                .createdAt(titel.getCreatedAt())
                .build();
    }
}
