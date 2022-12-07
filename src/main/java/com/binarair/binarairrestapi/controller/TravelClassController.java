package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.TravelClassRequest;
import com.binarair.binarairrestapi.model.request.TravelClassUpdateRequest;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;
import com.binarair.binarairrestapi.model.response.UserResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.TravelClassService;
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
@RequestMapping("/api/v1/travel")
public class TravelClassController {

    private final static Logger log = LoggerFactory.getLogger(TravelClassController.class);

    private final TravelClassService travelClassService;

    @Autowired
    public TravelClassController(TravelClassService travelClassService) {
        this.travelClassService = travelClassService;
    }


    @Operation(summary = "save travel class data", responses = @ApiResponse(responseCode = "201"))
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


    @Operation(summary = "get all travel class data")
    @ResponseBody
    @GetMapping("/all")
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


    @Operation(summary = "get travel class data based on travel class id")
    @ResponseBody
    @GetMapping
    public ResponseEntity<WebResponse<UserResponse>> findTravelClassById(@RequestParam("id") String id) {
        log.info("Call controller find travel by id - travel class");
        TravelClassResponse travelClassResponse = travelClassService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                travelClassResponse
        );
        log.info("Successful get travel calss data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "delete travel class data based on travel class id")
    @DeleteMapping("/{travelId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("travelId") String travelId) {
        log.info("Call delete controller - travel class");
        Boolean deleteStatus = travelClassService.delete(travelId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete travel class");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }


    @Operation(summary = "update travel class data based on travel class id")
    @PutMapping("/{travelId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<TravelClassResponse>> update(@RequestBody @Valid TravelClassUpdateRequest travelClassUpdateRequest, @PathVariable("travelId") String travelId) {
        log.info("Call update controller - travel class");
        TravelClassResponse travelClassResponse = travelClassService.update(travelClassUpdateRequest,travelId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                travelClassResponse
        );
        log.info("Successful update travel class data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK );
    }
}
