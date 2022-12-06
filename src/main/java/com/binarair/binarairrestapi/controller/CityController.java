package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.CityRequest;
import com.binarair.binarairrestapi.model.response.AircraftDetailResponse;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.model.response.HeroBannerResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
public class CityController {

    private final static Logger log = LoggerFactory.getLogger(CityController.class);

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }


    @Operation(summary = "save city data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<CityResponse>> save(@Valid @RequestBody CityRequest cityRequest) {
        log.info("call controller save - city");
        CityResponse cityResponse = cityService.save(cityRequest);
        log.info("successful save city data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                cityResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "delete city data based on city code id")
    @DeleteMapping("/{codeId}")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("codeId") String codeId) {
        log.info("calling controller delete - city");
        boolean deleteResonse = cityService.delete(codeId);
        log.info("successful delete city data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResonse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "get all city data")
    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<WebResponse<List<CityResponse>>> getAll() {
        log.info("Calling controller getAll - city");
        List<CityResponse> cityResponses = cityService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                cityResponses
        );
        log.info("Successful get all city data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "find city data based on city code id")
    @ResponseBody
    @GetMapping
    public ResponseEntity<WebResponse<CityResponse>> findById(@RequestParam("code") String code) {
        log.info("Calling controller city  - city");
        CityResponse cityResponse = cityService.findByCode(code);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                cityResponse
        );
        log.info("Successful get city data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
