package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.entity.Booking;
import com.binarair.binarairrestapi.model.entity.BookingDetail;
import com.binarair.binarairrestapi.model.request.BookingAircraftSeatRequest;
import com.binarair.binarairrestapi.model.request.BookingDetailRequest;
import com.binarair.binarairrestapi.model.response.*;

import java.math.BigDecimal;
import java.util.List;

public interface BookingDetailService {

    BookingResponse transaction(BookingDetailRequest bookingDetailRequest, String userId);

    Booking createBooking(BookingDetailRequest bookingDetailRequest, String userId);

    List<ProcessPassengerResponse> insertPassenger(BookingDetailRequest bookingDetailRequest, String userId);

    AircraftSeatResponse insertSeatBooking(BookingAircraftSeatRequest bookingAircraftSeatRequest, String ScheduleId);

    ProcessBaggageResponse findBaggageByScheduleId(String scheduleId, Integer weight);

    void updateScheduleStock(String scheduleId);

    void updateTotalPaidBooking(String bookingId, BigDecimal bigDecimal);

    BookingDetail saveBookingDetail(ProcessPassengerResponse processPassengerResponses, Booking booking);

    BigDecimal getTotalPayment(String bookingId);

    BookingResponse getBookingResponse(String bookingId);




}
