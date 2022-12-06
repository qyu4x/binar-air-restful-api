package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.FacilityRequest;
import com.binarair.binarairrestapi.model.response.AircraftDetailResponse;
import com.binarair.binarairrestapi.model.response.FacilityDetailResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.FacilityService;
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
@RequestMapping("/api/v1/facility")
public class FacilityController {

    private final static Logger log = LoggerFactory.getLogger(FacilityController.class);

    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }


    @Operation(summary = "save facility data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<FacilityDetailResponse>> save(@Valid @RequestBody FacilityRequest facilityRequest) {
        log.info("call controller save - facility");
        FacilityDetailResponse facilityDetailResponse = facilityService.save(facilityRequest);
        log.info("successful save facility data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                facilityDetailResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "get all facility data")
    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<FacilityDetailResponse>>> getAll() {
        log.info("Calling controller getAll - facility");
        List<FacilityDetailResponse> facilityDetailResponses = facilityService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                facilityDetailResponses
        );
        log.info("Successful get all facility data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "get facility data based on aircraft id")
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<FacilityDetailResponse>>> findByAircraftId(@RequestParam("aircraftid") String aircraftid) {
        log.info("Calling controller find by aircraht id - facility");
        List<FacilityDetailResponse> facilityDetailResponses = facilityService.findByAircraftId(aircraftid);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                facilityDetailResponses
        );
        log.info("Successful get facility data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "delete facility data based on facility id")
    @DeleteMapping("/{facilityId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("facilityId") String facilityId) {
        log.info("Call delete facility - facility");
        Boolean deleteStatus = facilityService.delete(facilityId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete facility data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }
}
