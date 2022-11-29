package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.response.PromoBannerPaggableResponse;
import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.PromoBannerService;
import com.binarair.binarairrestapi.util.MapperHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promobanner")
public class PromoBannerController {

    private final static Logger log = LoggerFactory.getLogger(PromoBannerController.class);


    private final PromoBannerService promoBannerService;

    @Autowired
    public PromoBannerController(PromoBannerService promoBannerService) {
        this.promoBannerService = promoBannerService;
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<PromoBannerResponse>> upload(@Valid @RequestPart("promoBannerRequest")String promoBannerRequest, @RequestPart("promoBannerImageRequest") MultipartFile promoBannerImageRequest) throws JsonProcessingException {
        PromoBannerRequest promoBanner = MapperHelper.mapperToPromoBanner(promoBannerRequest);
        log.info("Calling controller upload - promo banner");
        PromoBannerResponse promoBannerResponse = promoBannerService.save(promoBanner, promoBannerImageRequest);
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                promoBannerResponse
        );
        log.info("Successful upload promo banner");
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<Page<PromoBannerPaggableResponse>>> getAll(Pageable pageable) {
        log.info("Calling controller getAll - promo banner");
        Page<PromoBannerPaggableResponse> promoBannerResponses = promoBannerService.getAll(pageable);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                promoBannerResponses
        );
        log.info("Successful get all data promo banner");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
