package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import com.binarair.binarairrestapi.model.response.RoundTripTicketResponse;
import com.binarair.binarairrestapi.model.response.TicketResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flight")
public class TicketFilterController {

    private final static Logger log = LoggerFactory.getLogger(TicketFilterController.class);

    private final ScheduleService scheduleService;

    @Autowired
    public TicketFilterController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/fulltwosearch")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<RoundTripTicketResponse>> fullTwoSearch(@RequestParam("ap") String ap, @RequestParam("dt") String dt,
                                                                              @RequestParam("ps") String ps, @RequestParam("sc")String sc) {
        log.info("call contoller fullTwoSearch");
        RoundTripTicketResponse roundTripTicketResponse = scheduleService.filterFullTwoSearch(ap, dt, ps, sc);
        log.info("Successful get all ticket round trip");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                roundTripTicketResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @GetMapping("/fullsearch")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    public ResponseEntity<WebResponse<List<TicketResponse>>> fullSearch(@RequestParam("ap") String ap, @RequestParam("dt") String dt,
                                                                        @RequestParam("ps") String ps, @RequestParam("sc")String sc) {
        log.info("call controller fullSearch");
        List<TicketResponse> ticketResponses = scheduleService.filterFullSearch(ap, dt, ps, sc);
        log.info("Successful get all ticket");
        WebResponse webResponse = new WebResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                ticketResponses
        );
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

}
