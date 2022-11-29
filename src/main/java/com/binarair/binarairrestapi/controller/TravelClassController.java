package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.TravelClassService;
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
@RequestMapping("/api/v1/travel")
public class TravelClassController {

    private final static Logger log = LoggerFactory.getLogger(TravelClassController.class);

    private final TravelClassService travelClassService;

    @Autowired
    public TravelClassController(TravelClassService travelClassService) {
        this.travelClassService = travelClassService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<TravelClassResponse>> save(@Valid @RequestBody TravelClassRequest travelClassRequest) {
        log.info("call controller save - travel class");
        TravelClassResponse travelClassResponse = travelClassService.save(travelClassRequest);
        log.info("successful save city data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                travelClassResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<TravelClassResponse>>> getAll() {
        log.info("Calling controller getAll - travel clas");
        List<TravelClassResponse> travelClassResponses = travelClassService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                travelClassResponses
        );
        log.info("Successful get all travel class data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
