package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.config.NotificationConfiguration;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.BookingDetail;
import com.binarair.binarairrestapi.model.entity.Schedule;
import com.binarair.binarairrestapi.model.request.TicketJasperRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.repository.*;
import com.binarair.binarairrestapi.service.NotificationService;
import com.binarair.binarairrestapi.service.TicketJasperService;
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
public class TicketJasperServiceImpl implements TicketJasperService {

    private final static Logger log = LoggerFactory.getLogger(BookingDetailServiceImpl.class);

    private final BookingDetailRepository bookingDetailRepository;

    private final ScheduleRepository scheduleRepository;


    @Autowired
    public TicketJasperServiceImpl(BookingDetailRepository bookingDetailRepository, ScheduleRepository scheduleRepository) {
        this.bookingDetailRepository = bookingDetailRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public byte[] createpdf(String lastName,String bookingReferenceNumber){
        log.info("create PDF Ticket");
        BookingDetail pdfBookingDetail = bookingDetailRepository.findCheckInBookingDetail(bookingReferenceNumber,true,lastName.toUpperCase());

        if (pdfBookingDetail == null) {
            log.info("Booking Detail not secured");
            throw new DataNotFoundException("No booking has been retrieved. Please check your input details.");
        }

        String passengerFirstName = pdfBookingDetail.getPassenger().getFirstName();
        passengerFirstName = passengerFirstName.substring(0,1).toUpperCase() + passengerFirstName.substring(1).toLowerCase();

        log.info("Created PDF name {}",pdfBookingDetail.getPassenger().getFirstName());
        log.info("Booking Detail is secured with name {}",pdfBookingDetail.getPassenger().getFirstName());
        Schedule schedule = scheduleRepository.findById(pdfBookingDetail.getSchedule().getId())
                    .orElseThrow(() -> new DataNotFoundException("Schedule not found"));
        log.info("Schedule info secured");
            TicketJasperResponse jasperInformation = TicketJasperResponse.builder()
                    .id(pdfBookingDetail.getPassenger().getId())
                    .titel(pdfBookingDetail.getPassenger().getTitel().getTitelName())
                    .firstName(passengerFirstName)
                    .FromCity(schedule.getOriginIataAirportCode().getCity().getName())
                    .classType(schedule.getAircraft().getTravelClass().getName())
                    .DestinationCity(schedule.getDestIataAirportCode().getCity().getName())
                    .seatCode(pdfBookingDetail.getSeatCode())
                    .departureDate(schedule.getDepartureDate())
                    .departureTime(schedule.getDepartureTime())
                    .createdAt(schedule.getCreatedAt())
                    .updatedAt(schedule.getUpdatedAt())
                    .build();

        List<TicketJasperResponse> ticketJasperResponseList = new ArrayList<>();
        ticketJasperResponseList.add(jasperInformation);
        log.info("Building of the jasperInformation success");
        try {
            File file = ResourceUtils.getFile("classpath:jasper/BoardingPass.jrxml");
            if (file == null) {
                log.info("Jasper is not readable");
                throw new DataNotFoundException("No JRXML has been detected");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, buildParametersMap(), new JRBeanCollectionDataSource(ticketJasperResponseList));
            log.info("Successfully export report to pdf");
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException | JRException exception) {
            log.error("Unfortunately an error has occured at {}", exception.getMessage());
        }
        return null;
    }




    private Map<String, Object> buildParametersMap() {
        Map<String, Object> pdfInvoiceParams = new HashMap<>();
        pdfInvoiceParams.put("poweredby", "BEJ For The Win");
        return pdfInvoiceParams;
    }
}

