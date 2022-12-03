package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.AircraftRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.service.AircraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/aircraft")
public class AircraftController {

    private static final Logger log = LoggerFactory.getLogger(AircraftController.class);

    private final AircraftService aircraftService;

    @Autowired
    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<AircraftDetailResponse>> save(@Valid @RequestBody AircraftRequest aircraftRequest) {
        log.info("call controller save - aircraft");
        AircraftDetailResponse aircraftResponse = aircraftService.save(aircraftRequest);
        log.info("successful save aircraft data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                aircraftResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<AircraftDetailResponse>>> getAll() {
        log.info("Calling controller getAll - aircraft");
        List<AircraftDetailResponse> aircraftDetailResponses = aircraftService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                aircraftDetailResponses
        );
        log.info("Successful get all aircraft data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<AircraftDetailResponse>> findById(@RequestParam("id") String id) {
        log.info("Calling controller find by id - aircraft");
        AircraftDetailResponse aircraftDetailResponse = aircraftService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                aircraftDetailResponse
        );
        log.info("Successful get aircraft data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
