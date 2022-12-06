package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.request.TitelRequest;
import com.binarair.binarairrestapi.model.request.TravelClassUpdateRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.service.TitelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "save titel data", responses = @ApiResponse(responseCode = "201"))
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


    @Operation(summary = "get all titel data")
    @ResponseBody
    @GetMapping("/all")
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


    @Operation(summary = "find titel data based on titel id")
    @ResponseBody
    @GetMapping
    public ResponseEntity<WebResponse<UserResponse>> findTitelById(@RequestParam("id") String id) {
        log.info("Call controller find titel by id - titel");
        TitelResponse titelResponse = titelService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                titelResponse
        );
        log.info("Successful get titel data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "delete titel data based on titel id")
    @DeleteMapping("/{titelId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("titelId") String titelId) {
        log.info("Call delete titel - titel class");
        Boolean deleteStatus = titelService.delete(titelId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete titel");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }


    @Operation(summary = "update titel data based on titel id")
    @PutMapping("/{titelId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<TravelClassResponse>> update(@RequestBody @Valid TitelRequest titelRequest, @PathVariable("titelId") String titelId) {
        log.info("Call update controller - titel");
        TitelResponse titelResponse = titelService.update(titelRequest,titelId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                titelResponse
        );
        log.info("Successful update titel data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK );
    }
}
