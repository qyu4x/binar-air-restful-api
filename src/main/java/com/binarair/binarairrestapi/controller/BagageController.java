package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.BagageRequest;
import com.binarair.binarairrestapi.model.request.BenefitRequest;
import com.binarair.binarairrestapi.model.response.BagageResponse;
import com.binarair.binarairrestapi.model.response.BenefitDetailResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.BagageService;
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
@RequestMapping("/api/v1/bagage")
public class BagageController {

    private final static Logger log = LoggerFactory.getLogger(BagageController.class);

    private final BagageService bagageService;

    @Autowired
    public BagageController(BagageService bagageService) {
        this.bagageService = bagageService;
    }


    @Operation(summary = "save baggage data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<BagageResponse>> save(@Valid @RequestBody BagageRequest bagageRequest) {
        log.info("call controller save - bagage");
        BagageResponse bagageResponse = bagageService.save(bagageRequest);
        log.info("successful save bagage data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                bagageResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "get all baggage data based on aircraft id")
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<BagageResponse>>> getAllById(@RequestParam("aircraftId") String aircraftId) {
        log.info("Calling controller getAll - bagage");
        List<BagageResponse> bagageResponses = bagageService.findBagageByAircraftId(aircraftId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                bagageResponses
        );
        log.info("Successful get all bagage data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "delete baggage data based on baggage id")
    @DeleteMapping("/{baggageId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("baggageId") String baggageId) {
        log.info("Call delete baggage - baggage");
        Boolean deleteStatus = bagageService.delete(baggageId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete baggage data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
