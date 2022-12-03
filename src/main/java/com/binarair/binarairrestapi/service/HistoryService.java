package com.binarair.binarairrestapi.service;


import com.binarair.binarairrestapi.model.request.HistoryRequest;
import com.binarair.binarairrestapi.model.response.HistoryResponse;

import java.util.List;

public interface HistoryService {
    HistoryResponse save(HistoryRequest historyRequest);

    List<HistoryResponse> getAll();
}
