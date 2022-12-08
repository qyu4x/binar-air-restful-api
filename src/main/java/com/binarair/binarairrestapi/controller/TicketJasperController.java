package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
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
     @ResponseBody
     @GetMapping(value = "/pdf",
             produces = MediaType.APPLICATION_PDF_VALUE)

     public void generateReport( HttpServletResponse response,@Valid @RequestBody TicketJasperRequest ticketJasperRequest) throws JRException, FileNotFoundException {
        log.info("Calling controller TicketJasper - TicketJasper");
         byte[] ticketJasperResponse = ticketJasperService.createpdf(ticketJasperRequest);
         try {
             log.info("Initialization on the controller");
             ByteArrayInputStream invoice = new ByteArrayInputStream(ticketJasperResponse);
             if (invoice == null) {
                 log.info("Invoice is null");
                 throw new DataNotFoundException("fail to find the response");
             }
             response.addHeader("Content-Disposition", "attachment; filename=" + UUID.randomUUID() +".pdf");
             response.setContentType("application/octet-stream");
             log.info("successfully added header and content type");
             IOUtils.copy(invoice, response.getOutputStream());
             response.flushBuffer();
             log.info("success create file pdf for booking id {} ",ticketJasperRequest.getBookingReferenceNumber() );

         } catch (Exception exception) {
             log.error("PDF generation failed due to {} ", exception.getMessage());
         }

    }

//    public void generateReport(HttpServletResponse response, TicketJasperResponse ticketJasperResponse) {




}
