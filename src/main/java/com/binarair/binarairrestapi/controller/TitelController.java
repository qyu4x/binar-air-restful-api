package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.request.TitelRequest;
import com.binarair.binarairrestapi.model.response.AirportResponse;
import com.binarair.binarairrestapi.model.response.TitelResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.TitelService;
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
@RequestMapping("/api/v1/titel")
public class TitelController {

    private final static Logger log = LoggerFactory.getLogger(TitelController.class);

    private final TitelService titelService;

    @Autowired
    public TitelController(TitelService titelService) {
        this.titelService = titelService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<TitelResponse>> save(@Valid @RequestBody TitelRequest titelRequest) {
        log.info("call controller save - titel");
        TitelResponse titelResponse = titelService.save(titelRequest);
        log.info("successful save titel data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                titelResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<TitelResponse>>> getAll() {
        log.info("Calling controller getAll - titel");
        List<TitelResponse> titelResponses = titelService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                titelResponses
        );
        log.info("Successful get all titel data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
