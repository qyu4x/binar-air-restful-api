package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.CancelCheckInRequest;
import com.binarair.binarairrestapi.model.request.CheckInRequest;
import com.binarair.binarairrestapi.model.request.CountryRequest;
import com.binarair.binarairrestapi.model.response.CancelCheckInResponse;
import com.binarair.binarairrestapi.model.response.CheckInResponse;
import com.binarair.binarairrestapi.model.response.CountryResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/checkin")
public class CheckInController {

    private final static Logger log = LoggerFactory.getLogger(CheckInController.class);

    private final CheckInService checkInService;

    @Autowired
    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @Operation(summary = "carry out the check-in process")
    @PutMapping
    @ResponseBody
    public ResponseEntity<WebResponse<CheckInResponse>> checkIn(@Valid @RequestBody CheckInRequest checkInRequest) {
        log.info("calling controller checkin - checkin");
        CheckInResponse checkInResponse = checkInService.checkIn(checkInRequest);
        log.info("successful checkin");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                checkInResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @Operation(summary = "carry out the cancel check-in process")
    @PutMapping("/cancel")
    @ResponseBody
    public ResponseEntity<WebResponse<CancelCheckInResponse>> cancelCheckIn(@Valid @RequestBody CancelCheckInRequest cancelCheckInRequest) {
        log.info("calling controller cancel checkin - checkin");
        CancelCheckInResponse cancelCheckInResponse = checkInService.cancelCheckIn(cancelCheckInRequest);
        log.info("successful cancel checkin");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                cancelCheckInResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
