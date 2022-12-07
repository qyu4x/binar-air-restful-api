package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.request.TicketJasperRequest;
import com.binarair.binarairrestapi.model.response.TicketJasperResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.TicketJasperService;
import com.google.api.client.util.IOUtils;
import io.swagger.v3.oas.annotations.Operation;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/jasperreport")
public class TicketJasperController {

    private final static Logger log = LoggerFactory.getLogger(AirportController.class);

    private final TicketJasperService ticketJasperService;

    public TicketJasperController(TicketJasperService ticketJasperService) {
        this.ticketJasperService = ticketJasperService;
    }
     @Operation(summary = "Print Jasper Report")
     @GetMapping(value = "/pdf",produces = MediaType.APPLICATION_PDF_VALUE)
//     @ResponseBody
//     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
     public void generateReport( HttpServletResponse response,@Valid @RequestBody TicketJasperRequest ticketJasperRequest) throws JRException, FileNotFoundException {
        log.info("Calling controller TicketJasper - TicketJasper");
         byte[] ticketJasperResponse = ticketJasperService.createpdf(ticketJasperRequest);
         try {
             ByteArrayInputStream invoice = new ByteArrayInputStream(ticketJasperResponse);
             response.addHeader("Content-Disposition", "attachment; filename=" + UUID.randomUUID() +".pdf");
             response.setContentType("application/octet-stream");

             IOUtils.copy(invoice, response.getOutputStream());
             response.flushBuffer();
             log.info("success create file pdf for user id {} ",ticketJasperRequest.getBookingReferenceNumber() );

         } catch (Exception exception) {
             log.error("PDF generation failed due to {} ", exception.getMessage());
         }

    }

//    public void generateReport(HttpServletResponse response, TicketJasperResponse ticketJasperResponse) {




}
