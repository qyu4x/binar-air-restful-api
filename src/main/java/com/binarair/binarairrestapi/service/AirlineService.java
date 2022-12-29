package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.response.AirlineResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AirlineService {

    AirlineResponse save(AirlineRequest airlineRequest, MultipartFile multipartFile);
    List<AirlineResponse> getAll();

    AirlineResponse update(AirlineRequest airlineRequest, String airlineId);

    Boolean delete(String airlineId);

    AirlineResponse findById(String airlineId);

}
