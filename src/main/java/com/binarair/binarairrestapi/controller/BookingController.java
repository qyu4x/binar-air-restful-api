package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.BookingDetailRequest;
import com.binarair.binarairrestapi.model.request.CityRequest;
import com.binarair.binarairrestapi.model.response.BookingResponse;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.BookingDetailService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final static Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingDetailService bookingDetailService;

    @Autowired
    public BookingController(BookingDetailService bookingDetailService) {
        this.bookingDetailService = bookingDetailService;
    }


    @Operation(summary = "process transactions based on the user id that wants ordered the ticket")
    @PostMapping("/{userId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<BookingResponse>> save(@Valid @RequestBody BookingDetailRequest bookingDetailRequest, @PathVariable("userId") String userId) {
        log.info("call controller transaction - booking");
        BookingResponse bookingResponse = bookingDetailService.transaction(bookingDetailRequest, userId);
        log.info("successful transaction ");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                bookingResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
