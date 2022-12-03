package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.model.entity.History;
import com.binarair.binarairrestapi.model.request.HistoryRequest;
import com.binarair.binarairrestapi.model.response.HistoryResponse;
import com.binarair.binarairrestapi.repository.HistoryRepository;
import com.binarair.binarairrestapi.service.HistoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final static Logger log = (Logger) LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public HistoryResponse save(HistoryRequest historyRequest) {
        Optional<History> historyName = historyRepository.findHistoryById(historyRequest.getData());
        if (historyName.isPresent()) {
            throw new DataAlreadyExistException(String.format("History with name %s is al ready available", historyRequest.getData()));
        }
        return new HistoryResponse();
    }
    @Override
    public List<HistoryResponse> getAll() {
        log.info("Do get all history data");
        List<History> histories = historyRepository.findAll();
        List<HistoryResponse> historyResponses = new ArrayList<>();
        histories.stream();
        historyResponses.stream().toList();

        log.info("succesfull get all history data");
        return historyResponses;
    }
}
