package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.BenefitRequest;
import com.binarair.binarairrestapi.model.request.BenefitUpdateRequest;
import com.binarair.binarairrestapi.model.request.TravelClassUpdateRequest;
import com.binarair.binarairrestapi.model.response.BenefitDetailResponse;
import com.binarair.binarairrestapi.model.response.BenefitResponse;
import com.binarair.binarairrestapi.model.response.TravelClassResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.BenefitService;
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
@RequestMapping("/api/v1/benefit")
public class BenefitController {

    private final static Logger log = LoggerFactory.getLogger(BenefitController.class);

    private final BenefitService benefitService;

    @Autowired
    public BenefitController(BenefitService benefitService) {
        this.benefitService = benefitService;
    }


    @Operation(summary = "save benefit data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<BenefitDetailResponse>> save(@Valid @RequestBody BenefitRequest benefitRequest) {
        log.info("call controller save - benefit");
        BenefitDetailResponse benefitDetailResponse = benefitService.save(benefitRequest);
        log.info("successful save benefit data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                benefitDetailResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "get all benefit data")
    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<BenefitDetailResponse>>> getAll() {
        log.info("Calling controller getAll - benefit");
        List<BenefitDetailResponse> benefitDetailResponses = benefitService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                benefitDetailResponses
        );
        log.info("Successful get all benefit data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "get all benefit data based on aircraft id")
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<BenefitDetailResponse>>> findByAircraftId(@RequestParam("aircraftid") String aircraftid) {
        log.info("Calling controller find by aircraft id - benefit");
        List<BenefitDetailResponse> benefitDetailResponses = benefitService.findByAircraftId(aircraftid);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                benefitDetailResponses
        );
        log.info("Successful get benefit data based on id aircraft");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "delete benefit data based on benefit id")
    @DeleteMapping("/{benefitId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("benefitId") String benefitId) {
        log.info("Call delete benefit - benefit");
        Boolean deleteStatus = benefitService.delete(benefitId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete benefit data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }

    @Operation(summary = "update benefit data based on benefit id")
    @PutMapping("/{benefitId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<BenefitDetailResponse>> update(@RequestBody @Valid BenefitUpdateRequest benefitUpdateRequest, @PathVariable("benefitId") String benefitId) {
        log.info("Call update controller - travel class");
        BenefitDetailResponse benefitDetailResponse = benefitService.update(benefitUpdateRequest,benefitId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                benefitDetailResponse
        );
        log.info("Successful update benefit data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK );
    }
}
