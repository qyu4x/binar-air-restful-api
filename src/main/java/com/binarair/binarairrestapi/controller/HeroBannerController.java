package com.binarair.binarairrestapi.controller;


import com.binarair.binarairrestapi.model.request.HeroBannerRequest;
import com.binarair.binarairrestapi.model.response.HeroBannerResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.HeroBannerService;
import com.binarair.binarairrestapi.util.MapperHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/herobanner")
public class HeroBannerController {

    private final static Logger log = LoggerFactory.getLogger(HeroBannerController.class);

    private final HeroBannerService heroBannerService;

    @Autowired
    public HeroBannerController(HeroBannerService heroBannerService) {
        this.heroBannerService = heroBannerService;
    }


    @Operation(summary = "save hero banner data using form data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<HeroBannerResponse>> upload(@Valid @RequestPart("heroBannerRequest")String heroBannerRequest, @RequestPart("heroBannerImageRequest") MultipartFile heroBannerImageRequest) throws JsonProcessingException {
        HeroBannerRequest heroBanner = MapperHelper.mapperToHeroBanner(heroBannerRequest);
        log.info("Calling controller upload - hero banner");
        HeroBannerResponse heroBannerResponse = heroBannerService.save(heroBanner, heroBannerImageRequest);
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                heroBannerResponse
        );
        log.info("Successful upload hero banner");
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "get all hero banner data")
    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<WebResponse<List<HeroBannerResponse>>> getAll() {
        log.info("Calling controller getAll - hero banner");
        List<HeroBannerResponse> heroBannerRespones = heroBannerService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                heroBannerRespones
        );
        log.info("Successful get all data hero banner");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }


    @Operation(summary = "delete hero banner data based on hero banner id")
    @DeleteMapping("/{heroBannerId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("heroBannerId") String heroBannerId) {
        log.info("Call delete hero banner - hero banner");
        Boolean deleteStatus = heroBannerService.delete(heroBannerId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete hero banner data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }
}
