package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Booking;
import com.binarair.binarairrestapi.model.entity.BookingDetail;
import com.binarair.binarairrestapi.model.response.ETicketResponse;
import com.binarair.binarairrestapi.repository.BookingDetailRepository;
import com.binarair.binarairrestapi.repository.BookingRepository;
import com.binarair.binarairrestapi.repository.ScheduleRepository;
import com.binarair.binarairrestapi.service.ETicketService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class ETicketServiceImpl implements ETicketService {
    private final static Logger log = LoggerFactory.getLogger(BookingDetailServiceImpl.class);

    private final BookingRepository bookingRepository;


    @Autowired
    public ETicketServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public byte[] createticket(String bookingId) throws JRException, FileNotFoundException {

        log.info("Starting the creation of E-Ticket");
        Booking pdfBookingDetail = bookingRepository.findBookingDetailsById(bookingId);
        if (pdfBookingDetail == null) {
            log.error("Booking repository is empty");
            throw new DataNotFoundException("No booking has been found, please recheck your input details");
        }
        log.info("Successfully get booking repository with user name {}", pdfBookingDetail.getUser().getFullName());
        List<ETicketResponse> eTicketResponseList = new ArrayList<>();
        log.info("pdfBookingDetail berisi {}",pdfBookingDetail.getBookingDetails().size());

        pdfBookingDetail.getBookingDetails().stream().forEach(bookingDetail -> {
            ETicketResponse eTicketResponse = ETicketResponse.builder()
                    .bookingId(pdfBookingDetail.getId())
                    .destinationCity(bookingDetail.getSchedule().getDestIataAirportCode().getCity().getName())
                    .departureDate(bookingDetail.getSchedule().getDepartureDate())
                    .arrivalTime(bookingDetail.getSchedule().getArrivalTime())
                    .departureTime(bookingDetail.getSchedule().getDepartureTime())
                    .fromCity(bookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName())
                    .bookingReferenceNumber(bookingDetail.getBookingReferenceNumber())
                    .departureAirport(bookingDetail.getSchedule().getOriginIataAirportCode().getName())
                    .arrivalAirport(bookingDetail.getSchedule().getDestIataAirportCode().getName())
                    .firstName(bookingDetail.getPassenger().getFirstName().toUpperCase())
                    .lastName(bookingDetail.getPassenger().getLastName().toUpperCase())
                    .passportNumber(bookingDetail.getPassenger().getPassportNumber())
                    .baggage(bookingDetail.getExtraBagage())
                    .build();
            eTicketResponseList.add(eTicketResponse);
            log.info("Booking id nya adalah {}",bookingDetail.getId());
        });

        log.info("Total data EticketResponseList : {}",eTicketResponseList.size());
        log.info("successful input to the jasper");
        try {
            File files = ResourceUtils.getFile("classpath:jasper/ETicket.jrxml");
            if (files == null) {
                log.info("Jasper is not readable");
                throw new DataNotFoundException("No JRXML has been detected");
            }
            log.info("Jasper information get");
            JasperReport jasperReport = JasperCompileManager.compileReport(files.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, buildParametersMap(), new JRBeanCollectionDataSource(eTicketResponseList));
            log.info("Successfully export report to pdf");
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException | JRException exception) {
            log.error("Unfortunately an error has been occurred at {}", exception.getMessage());
        }

        return null;
    }


    private Map<String, Object> buildParametersMap() {
        Map<String, Object> pdfInvoiceParams = new HashMap<>();
        pdfInvoiceParams.put("poweredby", "BEJ For The Win");
        return pdfInvoiceParams;
    }
}
