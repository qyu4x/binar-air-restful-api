package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.CountryRequest;
import com.binarair.binarairrestapi.model.response.CountryDetailResponse;
import com.binarair.binarairrestapi.model.response.CountryResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.CountryService;
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
@RequestMapping("/api/v1/country")
public class CountryController {

    private final static Logger log = LoggerFactory.getLogger(CountryController.class);

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<CountryResponse>> save(@Valid @RequestBody CountryRequest countryRequest) {
        log.info("calling controller save - country");
        CountryResponse countryResponse = countryService.save(countryRequest);
        log.info("successful save country data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                countryResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @DeleteMapping
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@RequestParam("code") String code) {
        log.info("calling controller delete - country");
        boolean deleteResonse = countryService.delete(code);
        log.info("successful delete country data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResonse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<CountryDetailResponse>>> getAll() {
        log.info("Calling controller getAll - country service");
        List<CountryDetailResponse> countryResponse = countryService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                countryResponse
        );
        log.info("Successful get all country data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


}
