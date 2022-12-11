package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.response.HistoryResponse;
import com.binarair.binarairrestapi.model.response.NotificationDetailResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    private final static Logger log = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Operation(summary = "get all history booking data based on user id")
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse< List<HistoryResponse>>> getAll(@RequestParam("userid") @Parameter(name = "userid", description = "current user id") String userid,
                                                                      @RequestParam("sort") @Parameter(name = "sort", description = "sort type ASC or DESC ", example = "DESC") String sort) {
        log.info("Calling controller getAll - history");
        List<HistoryResponse> notificationDetailResponse = historyService.findHistoryBookingByUserId(userid, sort);
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                notificationDetailResponse
        );
        log.info("Successful get all history booking data based on user id");
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
