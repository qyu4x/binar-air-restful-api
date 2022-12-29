package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.response.PromoBannerResponse;
import com.binarair.binarairrestapi.model.response.RoundTripTicketResponse;
import com.binarair.binarairrestapi.model.response.TicketResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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


    @Operation(summary = "get filter results for round trip")
    @GetMapping("/fulltwosearch")
    @ResponseBody
    public ResponseEntity<WebResponse<RoundTripTicketResponse>> fullTwoSearch(@RequestParam("ap") @Parameter(name = "iata", description = "IATA airport origin and destination", example = "CGK.HND") String ap,
                                                                              @RequestParam("dt") @Parameter(name = "departure", description = "Date of departure and return", example = "26-11-2022.29-11-2022") String dt,
                                                                              @RequestParam("ps") @Parameter(name = "passenger", description = "Total of adult passengers, children and infants", example = "2.1.1")String ps,
                                                                              @RequestParam("sc") @Parameter(name = "service class", description = "Selected service class", example = "BUSINESS")String sc) {
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


    @Operation(summary = "get filter results for one way trip")
    @GetMapping("/fullsearch")
    @ResponseBody
    public ResponseEntity<WebResponse<List<TicketResponse>>> fullSearch(@RequestParam("ap") @Parameter(name = "iata", description = "IATA airport origin and destination", example = "CGK.HND") String ap,
                                                                        @RequestParam("dt") @Parameter(name = "departure", description = "Date of departure, return value = NA (Not Available)", example = "26-11-2022.NA") String dt,
                                                                        @RequestParam("ps") @Parameter(name = "passenger", description = "Total of adult passengers, children and infants", example = "2.1.1")String ps,
                                                                        @RequestParam("sc") @Parameter(name = "service class", description = "Selected service class", example = "BUSINESS")String sc) {
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
