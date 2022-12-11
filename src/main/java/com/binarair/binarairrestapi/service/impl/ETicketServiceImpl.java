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
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ETicketServiceImpl implements ETicketService {
    private final static Logger log = LoggerFactory.getLogger(BookingDetailServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingDetailRepository bookingDetailRepository;

    private final ScheduleRepository scheduleRepository;

    public ETicketServiceImpl(BookingRepository bookingRepository, BookingDetailRepository bookingDetailRepository, ScheduleRepository scheduleRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.scheduleRepository = scheduleRepository;
    }


//    @Override
//    public byte[] createticketinfo(String bookingId) throws JRException, FileNotFoundException {
//        log.info("Started creation of E-Ticket");
//        Booking pdfBookingDetail = bookingRepository.findBookingDetailsById(bookingId);
//        if (pdfBookingDetail == null) {
//            log.info("Booking Detail not secured");
//            throw new DataNotFoundException("No booking has been retrieved. Please check your input details.");
//        }
//        log.info("Created E-Ticket with user name {}",pdfBookingDetail.getUser().getFullName());
//        List<BookingDetail> bookingDetails = pdfBookingDetail.getBookingDetails();
//        List<ETicketResponse> eTicketResponses = new ArrayList<>();
//        bookingDetails.stream().forEach(bookingDetail -> {
//            ETicketResponse eTicketResponse = ETicketResponse.builder();
//        .DestinationCity(bookingDetail.getSchedule().getDestIataAirportCode().getCity().getName())
//        .departureDate(bookingDetail.getSchedule().getDepartureDate())
//        .arrivalTime(bookingDetail.getSchedule().getArrivalTime())
//        .departureTime(bookingDetail.getSchedule().getDepartureTime())
//        .FromCity(bookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName())
//            return null;
//    }


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
                    .bookingId(bookingDetail.getId())
                    .destinationCity(bookingDetail.getSchedule().getDestIataAirportCode().getCity().getName())
                    .departureDate(bookingDetail.getSchedule().getDepartureDate())
                    .arrivalTime(bookingDetail.getSchedule().getArrivalTime())
                    .departureTime(bookingDetail.getSchedule().getDepartureTime())
                    .fromCity(bookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName())
                    .titel(bookingDetail.getPassenger().getTitel().getTitelName())
                    .fullName(bookingDetail.getPassenger().getUser().getFullName().toUpperCase())
                    .bookingReferenceNumber(bookingDetail.getBookingReferenceNumber())
                    .ageCategory(bookingDetail.getPassenger().getAgeCategory())
                    .aircraftType(bookingDetail.getSchedule().getAircraft().getModel())
                    .departureAirport(bookingDetail.getSchedule().getOriginIataAirportCode().getName())
                    .arrivalAirport(bookingDetail.getSchedule().getDestIataAirportCode().getName())
                    .classType(bookingDetail.getBooking().getBookingType())
                    .baggage(bookingDetail.getExtraBagage())
                    .build();
            eTicketResponseList.add(eTicketResponse);
        });
        log.info("Total data EticketResponseList : {}",eTicketResponseList.size());
        log.info("successful input to the jasper");
        try {
            File files = ResourceUtils.getFile("classpath:jasper/ETicket.jrxml");
            if (files == null) {
                log.info("Jasper is not readable");
                throw new DataNotFoundException("No JRXML has been detected");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(files.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, buildParametersMap(), new JRBeanCollectionDataSource(eTicketResponseList));
            log.info("Successfully export report to pdf");
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException | JRException exception) {
            log.error("Unfortunately an error has been occurred at {}", exception.getMessage());
        }

        return null;
    }


    //    @Override
//    public byte[] createticket(String bookingId) throws JRException, FileNotFoundException {
//        log.info("Started creation of E-Ticket");
//       Booking pdfBookingDetail = bookingRepository.findBookingDetailsById(bookingId);
//        if (pdfBookingDetail == null) {
//            log.info("Booking Detail not secured");
//            throw new DataNotFoundException("No booking has been retrieved. Please check your input details.");
//        }
//        log.info("Created E-Ticket with user name {}",pdfBookingDetail.getUser().getFullName());
//        List<BookingDetail> bookingDetails = pdfBookingDetail.getBookingDetails();
//        List<ETicketResponse> eTicketResponses = new ArrayList<>();
//        bookingDetails.stream().forEach(bookingDetail -> {
//        ETicketResponse eTicketResponse = ETicketResponse.builder()
//                .bookingId(bookingDetail.getId())
//                .DestinationCity(bookingDetail.getSchedule().getDestIataAirportCode().getCity().getName())
//                .departureDate(bookingDetail.getSchedule().getDepartureDate())
//                .arrivalTime(bookingDetail.getSchedule().getArrivalTime())
//                .departureTime(bookingDetail.getSchedule().getDepartureTime())
//                .FromCity(bookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName())
//                .titel(bookingDetail.getPassenger().getTitel().getTitelName())
//                .fullName(bookingDetail.getPassenger().getUser().getFullName().toUpperCase())
//                .bookingReferenceNumber(bookingDetail.getBookingReferenceNumber())
//                .ageCategory(bookingDetail.getPassenger().getAgeCategory())
//                .aircraftType(bookingDetail.getSchedule().getAircraft().getModel())
//                .departureAirport(bookingDetail.getSchedule().getOriginIataAirportCode().getName())
//                .arrivalAirport(bookingDetail.getSchedule().getDestIataAirportCode().getName())
//                .classType(bookingDetail.getBooking().getBookingType())
//                .baggage(bookingDetail.getExtraBagage())
//                .build();
//        log.info("Departure city name is {}",bookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName());
//        log.info("arrival city name is {}",bookingDetail.getSchedule().getDestIataAirportCode().getCity().getName());
//        eTicketResponses.add(eTicketResponse);
//
//    });
//        log.info("Building of the jasper success");
//        try {
//            File files = ResourceUtils.getFile("classpath:jasper/ETicket.jrxml");
//            if (files == null) {
//                log.info("Jasper is not readable");
//                throw new DataNotFoundException("No JRXML has been detected");
//            }
//            JasperReport jasperReport = JasperCompileManager.compileReport(files.getAbsolutePath());
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, buildParametersMap(), new JRBeanCollectionDataSource(eTicketResponses));
//            log.info("Successfully export report to pdf");
//            return JasperExportManager.exportReportToPdf(jasperPrint);
//        } catch (IOException | JRException exception) {
//            log.error("Unfortunately an error has been occurred at {}", exception.getMessage());
//        }
//        return null;
//    }
    private Map<String, Object> buildParametersMap() {
        Map<String, Object> pdfInvoiceParams = new HashMap<>();
        pdfInvoiceParams.put("poweredby", "BEJ For The Win");
        return pdfInvoiceParams;
    }
}
