package com.binarair.binarairrestapi;

import com.binarair.binarairrestapi.model.entity.BookingDetail;
import com.binarair.binarairrestapi.model.request.TicketJasperRequest;
import com.binarair.binarairrestapi.repository.BookingDetailRepository;
import com.binarair.binarairrestapi.service.TicketJasperService;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

@SpringBootTest
public class JasperReportTest {


    @Autowired
    private TicketJasperService ticketJasperService;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Test
    void test() throws JRException, FileNotFoundException {
        TicketJasperRequest ticketJasperRequest = new TicketJasperRequest();
        ticketJasperRequest.setLastName("Shinomiya");
        ticketJasperRequest.setBookingReferenceNumber("BB819D");
        var testing =ticketJasperService.createpdf(ticketJasperRequest);
        Assertions.assertNotNull(testing);
    }
    @Test
    void test2(){
        BookingDetail bookingDetails = bookingDetailRepository.findCheckInBookingDetail("BB819D",true,"Shinomiya");
        Assertions.assertNotNull(bookingDetails);
    }
    @Test
    void test3() throws JRException, FileNotFoundException {
        TicketJasperRequest ticketJasperRequest = new TicketJasperRequest();
        ticketJasperRequest.setLastName("Shinomiya");
        ticketJasperRequest.setBookingReferenceNumber("1234");
        var testing =ticketJasperService.createpdf(ticketJasperRequest);
        Assertions.assertNotNull(testing);
    }

}
