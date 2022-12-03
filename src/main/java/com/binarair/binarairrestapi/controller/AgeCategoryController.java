package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.AgeCategoryRequest;
import com.binarair.binarairrestapi.model.response.AgeCategoryResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.AgeCategoryService;
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
@RequestMapping("/api/v1/agecategory")

public class AgeCategoryController {

    private final static Logger log = LoggerFactory.getLogger(AgeCategoryController.class);

    private final AgeCategoryService ageCategoryService;

    @Autowired
    public AgeCategoryController(AgeCategoryService ageCategoryService) {
        this.ageCategoryService = ageCategoryService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<AgeCategoryResponse>> save(@Valid @RequestBody AgeCategoryRequest ageCategoryRequest) {
        log.info("call controller save - age category");
        AgeCategoryResponse ageCategoryResponse = ageCategoryService.save(ageCategoryRequest);
        log.info("successful save age category data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                ageCategoryResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<AgeCategoryResponse>>> getAll() {
        log.info("Calling controller getAll - age category");
        List<AgeCategoryResponse> ageCategoryResponses = ageCategoryService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                ageCategoryResponses
        );
        log.info("Successful get all age category data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }
}
