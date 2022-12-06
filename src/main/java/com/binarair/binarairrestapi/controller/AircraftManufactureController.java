package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.AircraftManufactureRequest;
import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.response.AircraftManufactureResponse;
import com.binarair.binarairrestapi.model.response.AirportResponse;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.AircraftManufactureService;
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
@RequestMapping("/api/v1/manufacture")
public class AircraftManufactureController {

    private final static Logger log = LoggerFactory.getLogger(AircraftManufactureController.class);

    private final AircraftManufactureService aircraftManufactureService;

    @Autowired
    public AircraftManufactureController(AircraftManufactureService aircraftManufactureService) {
        this.aircraftManufactureService = aircraftManufactureService;
    }

    @Operation(summary = "save aircraft manufacture data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<AircraftManufactureResponse>> save(@Valid @RequestBody AircraftManufactureRequest aircraftManufactureRequest) {
        log.info("call controller save -  save aircraft manufacture ");
        AircraftManufactureResponse aircraftManufactureResponse = aircraftManufactureService.save(aircraftManufactureRequest);
        log.info("successful save aircraft manufacture data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                aircraftManufactureResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "get all aircraft manufacture data")
    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<AircraftManufactureResponse>>> getAll() {
        log.info("Calling controller getAll - aircraft manufacture");
        List<AircraftManufactureResponse> aircraftManufactureResponses = aircraftManufactureService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                aircraftManufactureResponses
        );
        log.info("Successful get all aircraft manufacture data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @Operation(summary = "delete data aircraft manufacture based on aircraft manufacture id")
    @DeleteMapping("/{manufactureId}")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("manufactureId") String manufactureId) {
        log.info("calling controller delete - aircraft manufacture");
        boolean deleteResponse = aircraftManufactureService.delete(manufactureId);
        log.info("successful delete aircraft manufacture data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


}
