package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Booking;
import com.binarair.binarairrestapi.model.entity.BookingDetail;
import com.binarair.binarairrestapi.model.response.ETicketResponse;
import com.binarair.binarairrestapi.repository.BookingDetailRepository;
import com.binarair.binarairrestapi.repository.BookingRepository;
import com.binarair.binarairrestapi.repository.ScheduleRepository;
import com.binarair.binarairrestapi.service.ETicketService;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

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

    @Override
    public byte[] createticket(String bookingId) throws JRException, FileNotFoundException {
        log.info("Started creation of E-Ticket");
       Booking pdfBookingDetail = bookingRepository.findBookingDetailsById(bookingId);

        if (pdfBookingDetail == null) {
            log.info("Booking Detail not secured");
            throw new DataNotFoundException("No booking has been retrieved. Please check your input details.");
        }
        log.info("Created E-Ticket with user name {}",pdfBookingDetail.getUser().getFullName());
        List<BookingDetail> bookingDetails = pdfBookingDetail.getBookingDetails();
        bookingDetails.stream().forEach(bookingDetail -> {
        ETicketResponse eTicketResponse = ETicketResponse.builder()
                .titel(bookingDetail.getPassenger().getTitel().getTitelName())
                .fullName()
                .FromCity()
                .bookingReferenceNumber()
                .DestinationCity()
                .departureDate()
                .seatCode()
                .departureDate()
                .departureTime()
                .createdAt()
                .updatedAt()








                .build();
    }
}
