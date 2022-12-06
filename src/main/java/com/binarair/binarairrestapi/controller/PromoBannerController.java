package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.binarair.binarairrestapi.model.request.TitelRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.service.PromoBannerService;
import com.binarair.binarairrestapi.util.MapperHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "save promo banner data using form data", responses = @ApiResponse(responseCode = "201"))
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


    @Operation(summary = "get all promo banner data")
    @ResponseBody
    @GetMapping("/all")
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


    @Operation(summary = "delete promo banner data based on promo banner id")
    @DeleteMapping("/{promoBannerId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("promoBannerId") String promoBannerId) {
        log.info("Call delete promo banner - promo banner");
        Boolean deleteStatus = promoBannerService.delete(promoBannerId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete promo banner data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }

    @Operation(summary = "get promo banner data based on promo banner id")
    @ResponseBody
    @GetMapping
    public ResponseEntity<WebResponse<PromoBannerResponse>> findPromoBannerById(@RequestParam("id") String id) {
        log.info("Call controller find promo banner by id - travel class");
        PromoBannerResponse promoBannerResponse = promoBannerService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                promoBannerResponse
        );
        log.info("Successful get promo banner data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "update promo banner data based on promo banner id")
    @PutMapping("/{promoBannerId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<PromoBannerResponse>> update(@RequestBody @Valid PromoBannerRequest promoBannerRequest, @PathVariable("promoBannerId") String promoBannerId) {
        log.info("Call update controller - promo banner");
        PromoBannerResponse promoBannerResponse = promoBannerService.update(promoBannerRequest,promoBannerId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                promoBannerResponse
        );
        log.info("Successful update promo banner data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK );
    }

}
