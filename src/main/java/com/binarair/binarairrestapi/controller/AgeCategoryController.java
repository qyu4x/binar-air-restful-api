package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.AgeCategoryRequest;
import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.response.AgeCategoryResponse;
import com.binarair.binarairrestapi.model.response.AircraftDetailResponse;
import com.binarair.binarairrestapi.model.response.AirportResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.AgeCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "save age category data", responses = @ApiResponse(responseCode = "201"))
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

    @Operation(summary = "get all age category data")
    @ResponseBody
    @GetMapping("/all")
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
    @Operation(summary = "get age category data based on age category id")
    @ResponseBody
    @GetMapping
    public ResponseEntity<WebResponse<AircraftDetailResponse>> findById(@RequestParam("id") String id) {
        log.info("Calling controller find age category by id - aircraft");
        AgeCategoryResponse ageCategoryResponse = ageCategoryService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                ageCategoryResponse
        );
        log.info("Successful get age category  data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @Operation(summary = "delete age category data based on age category id")
    @DeleteMapping("/{ageCategory}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("ageCategory") String ageCategory) {
        log.info("Calling controller delete age category  - age category");
        Boolean deleteStatus = ageCategoryService.delete(ageCategory);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete age category data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }
}
