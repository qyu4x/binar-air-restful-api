package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.response.AirlineResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AirlineService {

    AirlineResponse save(AirlineRequest airlineRequest, MultipartFile multipartFile);

}
