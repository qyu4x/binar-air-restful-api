package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.ScheduleRequest;
import com.binarair.binarairrestapi.model.response.ScheduleResponse;
import com.binarair.binarairrestapi.model.response.UserResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.ScheduleService;
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
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final static Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @Operation(summary = "save schedule data", responses = @ApiResponse(responseCode = "201"))
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<ScheduleResponse>> save(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        log.info("call controller save - schedule");
        ScheduleResponse scheduleResponse = scheduleService.save(scheduleRequest);
        log.info("successful save schedule data");
        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                scheduleResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "get all schedule data")
    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<ScheduleResponse>>> getAll() {
        log.info("Calling controller getAll - schedule");
        List<ScheduleResponse> scheduleResponses = scheduleService.getAll();
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                scheduleResponses
        );
        log.info("Successful get all schedule data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @Operation(summary = "delete schedule data based on schedule id")
    @DeleteMapping("/{scheduleId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("scheduleId") String scheduleId) {
        log.info("Call delete schedule - schedule");
        Boolean deleteStatus = scheduleService.delete(scheduleId);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteStatus
        );
        log.info("Successful delete schedule data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK  );
    }

    @Operation(summary = "get schedule data based on schedule id")
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<ScheduleResponse>> findScheduleById(@RequestParam("id") String id) {
        log.info("Call controller find schedule by id - schedule");
        ScheduleResponse scheduleResponse = scheduleService.findById(id);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                scheduleResponse
        );
        log.info("Successful get schedule data");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
