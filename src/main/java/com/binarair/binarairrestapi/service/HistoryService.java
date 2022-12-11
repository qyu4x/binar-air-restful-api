package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.response.HistoryResponse;

import java.util.List;

public interface HistoryService {

    List<HistoryResponse> findHistoryBookingByUserId(String userId, String sort);

}
