package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Bagage;
import com.binarair.binarairrestapi.model.entity.BookingDetail;
import com.binarair.binarairrestapi.model.request.CheckInRequest;
import com.binarair.binarairrestapi.model.request.CancelCheckInRequest;
import com.binarair.binarairrestapi.model.response.CancelCheckInResponse;
import com.binarair.binarairrestapi.model.response.CheckInResponse;
import com.binarair.binarairrestapi.repository.BagageRepository;
import com.binarair.binarairrestapi.repository.BookingDetailRepository;
import com.binarair.binarairrestapi.service.CheckInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInServiceImpl implements CheckInService {

    private final static Logger log = LoggerFactory.getLogger(CheckInServiceImpl.class);

    private final BookingDetailRepository bookingDetailRepository;

    private final BagageRepository bagageRepository;

    @Autowired
    public CheckInServiceImpl(BookingDetailRepository bookingDetailRepository, BagageRepository bagageRepository) {
        this.bookingDetailRepository = bookingDetailRepository;
        this.bagageRepository = bagageRepository;
    }

    @Override
    public CheckInResponse checkIn(CheckInRequest checkInRequest) {
        log.info("carry out the check-in process");
        BookingDetail checkInBookingDetail = bookingDetailRepository.findCheckInBookingDetail(checkInRequest.getBookingReferenceNumber(), false, checkInRequest.getLastName().toUpperCase());
        if (checkInBookingDetail == null) {
            throw new DataNotFoundException("No booking has been retrieved. Please check your identification details.");
        }
        checkInBookingDetail.setCheckInStatus(true);
        bookingDetailRepository.save(checkInBookingDetail);
        log.info("successful check-in process");
        List<Bagage> baggages = bagageRepository.findByAircraftId(checkInBookingDetail.getSchedule().getAircraft().getId());

        String firstName = checkInBookingDetail.getPassenger().getFirstName();
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
        String lastName = checkInBookingDetail.getPassenger().getLastName();
        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();

        return CheckInResponse.builder()
                .id(checkInBookingDetail.getId())
                .cityOrigin(checkInBookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName())
                .cityDestination(checkInBookingDetail.getSchedule().getDestIataAirportCode().getCity().getName())
                .airportOrigin(checkInBookingDetail.getSchedule().getOriginIataAirportCode().getName())
                .destinationAirport(checkInBookingDetail.getSchedule().getDestIataAirportCode().getName())
                .iataOriginAirport(checkInBookingDetail.getSchedule().getOriginIataAirportCode().getIataAirportCode())
                .iataDestinationAirport(checkInBookingDetail.getSchedule().getDestIataAirportCode().getIataAirportCode())
                .departureDate(checkInBookingDetail.getSchedule().getDepartureDate())
                .arrivalDate(checkInBookingDetail.getSchedule().getArrivalDate())
                .departureTime(checkInBookingDetail.getSchedule().getDepartureTime())
                .arrivalTime(checkInBookingDetail.getSchedule().getArrivalTime())
                .firstName(firstName)
                .lastName(lastName)
                .titel(checkInBookingDetail.getPassenger().getTitel().getTitelName())
                .bookingReferenceNumber(checkInBookingDetail.getBookingReferenceNumber())
                .baggageAllowance(baggages.get(0).getFreeBagageCapacity())
                .extraBaggage(checkInBookingDetail.getExtraBagage())
                .seatCode(checkInBookingDetail.getSeatCode())
                .travelClass(checkInBookingDetail.getSchedule().getAircraft().getTravelClass().getName())
                .checkInStatus(checkInBookingDetail.isCheckInStatus())
                .createdAt(checkInBookingDetail.getCreatedAt())
                .updatedAt(checkInBookingDetail.getUpdatedAt())
                .build();
    }

    @Override
    public CancelCheckInResponse cancelCheckIn(CancelCheckInRequest cancelCheckInRequest) {
        log.info("carry out cancel check-in process");
        BookingDetail checkInBookingDetail = bookingDetailRepository.findCheckInBookingDetail(cancelCheckInRequest.getBookingReferenceNumber(), true, cancelCheckInRequest.getLastName().toUpperCase());
        if (checkInBookingDetail == null) {
            throw new DataNotFoundException("No booking has been retrieved. Please check your identification details.");
        }
        checkInBookingDetail.setCheckInStatus(false);
        bookingDetailRepository.save(checkInBookingDetail);
        log.info("successful cancel check-in process");
        List<Bagage> baggages = bagageRepository.findByAircraftId(checkInBookingDetail.getSchedule().getAircraft().getId());

        String firstName = checkInBookingDetail.getPassenger().getFirstName();
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
        String lastName = checkInBookingDetail.getPassenger().getLastName();
        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();

        return CancelCheckInResponse.builder()
                .id(checkInBookingDetail.getId())
                .cityOrigin(checkInBookingDetail.getSchedule().getOriginIataAirportCode().getCity().getName())
                .cityDestination(checkInBookingDetail.getSchedule().getDestIataAirportCode().getCity().getName())
                .airportOrigin(checkInBookingDetail.getSchedule().getOriginIataAirportCode().getName())
                .destinationAirport(checkInBookingDetail.getSchedule().getDestIataAirportCode().getName())
                .iataOriginAirport(checkInBookingDetail.getSchedule().getOriginIataAirportCode().getIataAirportCode())
                .iataDestinationAirport(checkInBookingDetail.getSchedule().getDestIataAirportCode().getIataAirportCode())
                .departureDate(checkInBookingDetail.getSchedule().getDepartureDate())
                .arrivalDate(checkInBookingDetail.getSchedule().getArrivalDate())
                .departureTime(checkInBookingDetail.getSchedule().getDepartureTime())
                .arrivalTime(checkInBookingDetail.getSchedule().getArrivalTime())
                .firstName(firstName)
                .lastName(lastName)
                .titel(checkInBookingDetail.getPassenger().getTitel().getTitelName())
                .bookingReferenceNumber(checkInBookingDetail.getBookingReferenceNumber())
                .baggageAllowance(baggages.get(0).getFreeBagageCapacity())
                .extraBaggage(checkInBookingDetail.getExtraBagage())
                .seatCode(checkInBookingDetail.getSeatCode())
                .travelClass(checkInBookingDetail.getSchedule().getAircraft().getTravelClass().getName())
                .checkInStatus(checkInBookingDetail.isCheckInStatus())
                .createdAt(checkInBookingDetail.getCreatedAt())
                .updatedAt(checkInBookingDetail.getUpdatedAt())
                .build();
    }

}
