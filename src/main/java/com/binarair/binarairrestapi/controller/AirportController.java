package com.binarair.binarairrestapi.controller;



import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.response.AirportResponse;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.AirportService;
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
@RequestMapping("/api/v1/airport")
public class AirportController {

    private final static Logger log = LoggerFactory.getLogger(AirportController.class);

    private final AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<AirportResponse>> save(@Valid @RequestBody AirportRequest airportRequest) {
        log.info("call controller save - airport");
        AirportResponse airportResponse = airportService.save(airportRequest);
        log.info("successful save airport data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                airportResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<AirportResponse>>> getAll() {
        log.info("Calling controller getAll - airport");
        List<AirportResponse> airportResponses = airportService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                airportResponses
        );
        log.info("Successful get all airport data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<AirportResponse>> findByIata(@RequestParam("iata") String iata) {
        log.info("Calling controller find by id - airport");
        AirportResponse airportResponse = airportService.findByIata(iata);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                airportResponse
        );
        log.info("Successful get airport data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
