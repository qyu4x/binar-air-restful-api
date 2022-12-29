package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.BagageRequest;
import com.binarair.binarairrestapi.model.response.BagageResponse;

import java.util.List;

public interface BagageService {

    BagageResponse save(BagageRequest bagageRequest);

    List<BagageResponse> findBagageByAircraftId(String id);


    Boolean delete(String baggageId);

}
