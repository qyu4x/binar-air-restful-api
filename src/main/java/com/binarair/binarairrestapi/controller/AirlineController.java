package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.response.AirlineResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.AirlineService;
import com.binarair.binarairrestapi.util.MapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/airline")
public class AirlineController {

    private final static Logger log = LoggerFactory.getLogger(AirlineController.class);

    private final AirlineService airlineService;

    @Autowired
    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }


    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<AirlineResponse>> save(@Valid @RequestPart("airlineRequest")String airlineRequest, @RequestPart("airlineImageRequest") MultipartFile airlineImageRequest) {
        AirlineRequest airline = MapperHelper.mapperToAirline(airlineRequest);
        log.info("Calling controller save - airline");
        AirlineResponse airlineResponse = airlineService.save(airline, airlineImageRequest);
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                airlineResponse
        );
        log.info("Successful save airline logo and airline data");
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }
    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<AirlineResponse>>> getAll() {
        log.info("Calling controller getAll - airline");
        List<AirlineResponse> airlineResponses = airlineService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                airlineResponses
        );
        log.info("Successful get all airlines data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
