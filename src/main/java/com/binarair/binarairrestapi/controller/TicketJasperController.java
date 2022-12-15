package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.service.ETicketService;
import com.binarair.binarairrestapi.service.TicketJasperService;
import com.google.api.client.util.IOUtils;
import io.swagger.v3.oas.annotations.Operation;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/jasperreport")
public class TicketJasperController {

    private final static Logger log = LoggerFactory.getLogger(TicketJasperController.class);

    private final TicketJasperService ticketJasperService;
    private final ETicketService eTicketService;

    @Autowired
    public TicketJasperController(TicketJasperService ticketJasperService, ETicketService eTicketService) {
        this.ticketJasperService = ticketJasperService;
        this.eTicketService = eTicketService;
    }
     @Operation(summary = "Print Boarding Pass")
     @ResponseBody
     @GetMapping(value = "/boardingpass/{lastName}/{bookingReferenceNumber}",
             produces = MediaType.APPLICATION_PDF_VALUE)
     public void generateReport( HttpServletResponse response,@PathVariable("lastName")String lastName,@PathVariable("bookingReferenceNumber") String bookingReferenceNumber ) throws JRException, FileNotFoundException {
        log.info("Calling controller TicketJasper - TicketJasper");
         byte[] ticketJasperResponse = ticketJasperService.createpdf(lastName,bookingReferenceNumber);
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
             log.info("success create file pdf for booking id {} ",bookingReferenceNumber);

         } catch (Exception exception) {
             log.error("PDF generation failed due to {} ", exception.getMessage());
         }

    }
    @Operation(summary = "Print E-ticket")
    @ResponseBody
    @GetMapping(value = "/eticket/{bookingId}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public void generateTicket( HttpServletResponse response,@PathVariable("bookingId") String bookingId ) throws JRException, FileNotFoundException {
        log.info("Calling controller jasperreport - Eticket");
        byte[] EticketResponse = eTicketService.createticket(bookingId);
        try {
            log.info("Initialization on the E-Ticket Controller");
            ByteArrayInputStream eticket = new ByteArrayInputStream(EticketResponse);
            if (eticket == null) {
                log.info("Invoice is null");
                throw new DataNotFoundException("fail to find the response");
            }
            response.addHeader("Content-Disposition", "attachment; filename=" + UUID.randomUUID() +".pdf");
            response.setContentType("application/octet-stream");
            log.info("successfully added header and content type");
            IOUtils.copy(eticket, response.getOutputStream());
            response.flushBuffer();
            log.info("success create E-Ticket for booking id {} ",bookingId);

        } catch (Exception exception) {
            log.error("E-Ticket generation failed due to {} ", exception.getMessage());
        }

    }




}
