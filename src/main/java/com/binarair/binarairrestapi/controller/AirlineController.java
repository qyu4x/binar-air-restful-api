package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.request.BenefitUpdateRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.service.AirlineService;
import com.binarair.binarairrestapi.util.MapperHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "save airline data using form data", responses = @ApiResponse(responseCode = "201"))
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

    @Operation(summary = "get all airline data")
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

    @Operation(summary = "delete airline data based on airline id")
    @DeleteMapping("/{airlineId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("airlineId") String airlineId) {
        log.info("Calling controlelr delete airline  - airline");
        Boolean deleteStatus = airlineService.delete(airlineId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete airline data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }

    @Operation(summary = "update airline data based on airline id")
    @PutMapping("/{airlineId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<AirlineResponse>> update(@RequestBody @Valid AirlineRequest airlineRequest, @PathVariable("airlineId") String airlineId) {
        log.info("Call update controller - airline");
        AirlineResponse airlineResponse = airlineService.update(airlineRequest,airlineId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                airlineResponse
        );
        log.info("Successful update airline data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK );
    }


    @Operation(summary = "get airline data based on airline id")
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<AirlineResponse>> findAirlineById(@RequestParam("id") String id) {
        log.info("Call controller find airline by id - airlines");
        AirlineResponse airlineResponse = airlineService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                airlineResponse
        );
        log.info("Successful get airline data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
