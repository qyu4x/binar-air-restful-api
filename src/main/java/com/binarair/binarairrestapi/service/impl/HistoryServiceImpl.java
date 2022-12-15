package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.exception.ValidationException;
import com.binarair.binarairrestapi.model.entity.Bagage;
import com.binarair.binarairrestapi.model.entity.Booking;
import com.binarair.binarairrestapi.model.entity.Schedule;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.repository.BagageRepository;
import com.binarair.binarairrestapi.repository.BookingRepository;
import com.binarair.binarairrestapi.repository.ScheduleRepository;
import com.binarair.binarairrestapi.repository.UserRepository;
import com.binarair.binarairrestapi.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final static Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final ScheduleRepository scheduleRepository;

    private final BagageRepository bagageRepository;

    @Autowired
    public HistoryServiceImpl(UserRepository userRepository, BookingRepository bookingRepository, ScheduleRepository scheduleRepository, BagageRepository bagageRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.scheduleRepository = scheduleRepository;
        this.bagageRepository = bagageRepository;
    }

    @Override
    public List<HistoryResponse> findHistoryBookingByUserId(String userId, String sort) {
        boolean isExists = userRepository.existsById(userId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("User with id %s not found", userId));
        }

        sort = sort.toUpperCase();
        if (sort.equals("ASC") || sort.equals("DESC")) {

            List<Booking> userBookingHistories = null;
            if (sort.equals("ASC")) {
                userBookingHistories = bookingRepository.findHistoryBookingByUserIdAsc(userId);
            }

            if (sort.equals("DESC")) {
                userBookingHistories = bookingRepository.findHistoryBookingByUserIdDesc(userId);
            }

            if (userBookingHistories.isEmpty()) {
                log.info("User with id {} do not have a booking history", userId);
                throw new DataNotFoundException("No Purchase Found");
            }

            List<HistoryResponse> historyResponses = new ArrayList<>();
            log.info("size  {} ", userBookingHistories.size());
            userBookingHistories.forEach(booking -> {
                List<PassengerBookingResponse> departures = new ArrayList<>();
                List<PassengerBookingResponse> returns = new ArrayList<>();
                booking.getBookingDetails().stream().forEach(bookingDetail -> {
                    if (bookingDetail.getStatus().equals("departure")) {
                        Schedule schedule = scheduleRepository.findById(bookingDetail.getSchedule().getId())
                                .orElseThrow(() -> new DataNotFoundException("Schedule not found"));
                        Bagage baggage = bagageRepository.findByAircraftIdAndBaggageWeight(schedule.getAircraft().getId(), bookingDetail.getExtraBagage());

                        String firstName = bookingDetail.getPassenger().getFirstName();
                        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();

                        String lastName = bookingDetail.getPassenger().getLastName();
                        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();

                        PassengerBookingResponse departureBookingResponse = PassengerBookingResponse.builder()
                                .passengerId(bookingDetail.getPassenger().getId())
                                .titel(bookingDetail.getPassenger().getTitel().getTitelName())
                                .ageCategory(bookingDetail.getPassenger().getAgeCategory().getCategoryName())
                                .firstName(firstName)
                                .lastName(lastName)
                                .bookingReferenceNumber(bookingDetail.getBookingReferenceNumber())
                                .checkInStatus(bookingDetail.isCheckInStatus())
                                .citizenship(bookingDetail.getPassenger().getCityzenship().getName())
                                .birthDate(bookingDetail.getPassenger().getBirthDate())
                                .passportNumber(bookingDetail.getPassenger().getPassportNumber())
                                .issuingCountry(bookingDetail.getPassenger().getIssuingCountry().getName())
                                .schedule(ScheduleResponse.builder()
                                        .id(schedule.getId())
                                        .originAirport(AirportResponse.builder()
                                                .iata(schedule.getOriginIataAirportCode().getIataAirportCode())
                                                .name(schedule.getOriginIataAirportCode().getName())
                                                .cityCode(schedule.getOriginIataAirportCode().getCity().getCodeId())
                                                .countryCode(schedule.getOriginIataAirportCode().getCity().getCountry().getCountryCode())
                                                .city(schedule.getOriginIataAirportCode().getCity().getName())
                                                .country(schedule.getOriginIataAirportCode().getCity().getCountry().getName())
                                                .createdAt(schedule.getOriginIataAirportCode().getCreatedAt())
                                                .build())
                                        .destinationAirport(AirportResponse.builder()
                                                .iata(schedule.getDestIataAirportCode().getIataAirportCode())
                                                .name(schedule.getDestIataAirportCode().getName())
                                                .cityCode(schedule.getDestIataAirportCode().getCity().getCodeId())
                                                .countryCode(schedule.getDestIataAirportCode().getCity().getCountry().getCountryCode())
                                                .city(schedule.getDestIataAirportCode().getCity().getName())
                                                .country(schedule.getDestIataAirportCode().getCity().getCountry().getName())
                                                .createdAt(schedule.getDestIataAirportCode().getCreatedAt())
                                                .build())
                                        .aircraft(AircraftResponse.builder()
                                                .id(schedule.getAircraft().getId())
                                                .type(schedule.getAircraft().getModel())
                                                .seatArrangement(schedule.getAircraft().getSeatArrangement())
                                                .distanceBetweenSeats(schedule.getAircraft().getDistanceBetweenSeats())
                                                .seatLengthUnit(schedule.getAircraft().getSeatLengthUnit())
                                                .build())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(schedule.getPrice()))
                                                .amount(schedule.getPrice())
                                                .build())
                                        .departureDate(schedule.getDepartureDate())
                                        .arrivalDate(schedule.getArrivalDate())
                                        .departureTime(schedule.getDepartureTime())
                                        .arrivalTime(schedule.getArrivalTime())
                                        .stock(schedule.getStock())
                                        .createdAt(schedule.getCreatedAt())
                                        .updatedAt(schedule.getUpdatedAt())
                                        .build())
                                .aircraftSeat(AircraftSeatResponse.builder()
                                        .seatCode(bookingDetail.getSeatCode())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(bookingDetail.getSeatPrice()))
                                                .amount(bookingDetail.getSeatPrice())
                                                .build())
                                        .build())
                                .bagage(BaggageBookingResponse.builder()
                                        .extraBagage(bookingDetail.getExtraBagage())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(baggage.getPrice()))
                                                .amount(baggage.getPrice())
                                                .build())
                                        .freeCabinCapacity(baggage.getFreeCabinBagageCapacity())
                                        .freeBagageCapacity(baggage.getFreeBagageCapacity())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(bookingDetail.getBagagePrice()))
                                                .amount(bookingDetail.getBagagePrice())
                                                .build())
                                        .build())
                                .createdAt(bookingDetail.getCreatedAt())
                                .build();
                        departures.add(departureBookingResponse);
                    }else {
                        Schedule schedule = scheduleRepository.findById(bookingDetail.getSchedule().getId())
                                .orElseThrow(() -> new DataNotFoundException("Schedule not found"));
                        Bagage baggage = bagageRepository.findByAircraftIdAndBaggageWeight(schedule.getAircraft().getId(), bookingDetail.getExtraBagage());

                        String firstName = bookingDetail.getPassenger().getFirstName();
                        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();

                        String lastName = bookingDetail.getPassenger().getLastName();
                        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();

                        PassengerBookingResponse returnsBookingResponse = PassengerBookingResponse.builder()
                                .passengerId(bookingDetail.getPassenger().getId())
                                .titel(bookingDetail.getPassenger().getTitel().getTitelName())
                                .ageCategory(bookingDetail.getPassenger().getAgeCategory().getCategoryName())
                                .firstName(firstName)
                                .lastName(lastName)
                                .bookingReferenceNumber(bookingDetail.getBookingReferenceNumber())
                                .checkInStatus(bookingDetail.isCheckInStatus())
                                .citizenship(bookingDetail.getPassenger().getCityzenship().getName())
                                .birthDate(bookingDetail.getPassenger().getBirthDate())
                                .passportNumber(bookingDetail.getPassenger().getPassportNumber())
                                .issuingCountry(bookingDetail.getPassenger().getIssuingCountry().getName())
                                .schedule(ScheduleResponse.builder()
                                        .id(schedule.getId())
                                        .originAirport(AirportResponse.builder()
                                                .iata(schedule.getOriginIataAirportCode().getIataAirportCode())
                                                .name(schedule.getOriginIataAirportCode().getName())
                                                .cityCode(schedule.getOriginIataAirportCode().getCity().getCodeId())
                                                .countryCode(schedule.getOriginIataAirportCode().getCity().getCountry().getCountryCode())
                                                .city(schedule.getOriginIataAirportCode().getCity().getName())
                                                .country(schedule.getOriginIataAirportCode().getCity().getCountry().getName())
                                                .createdAt(schedule.getOriginIataAirportCode().getCreatedAt())
                                                .build())
                                        .destinationAirport(AirportResponse.builder()
                                                .iata(schedule.getDestIataAirportCode().getIataAirportCode())
                                                .name(schedule.getDestIataAirportCode().getName())
                                                .cityCode(schedule.getDestIataAirportCode().getCity().getCodeId())
                                                .countryCode(schedule.getDestIataAirportCode().getCity().getCountry().getCountryCode())
                                                .city(schedule.getDestIataAirportCode().getCity().getName())
                                                .country(schedule.getDestIataAirportCode().getCity().getCountry().getName())
                                                .createdAt(schedule.getDestIataAirportCode().getCreatedAt())
                                                .build())
                                        .aircraft(AircraftResponse.builder()
                                                .id(schedule.getAircraft().getId())
                                                .type(schedule.getAircraft().getModel())
                                                .seatArrangement(schedule.getAircraft().getSeatArrangement())
                                                .distanceBetweenSeats(schedule.getAircraft().getDistanceBetweenSeats())
                                                .seatLengthUnit(schedule.getAircraft().getSeatLengthUnit())
                                                .build())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(schedule.getPrice()))
                                                .amount(schedule.getPrice())
                                                .build())
                                        .departureDate(schedule.getDepartureDate())
                                        .arrivalDate(schedule.getArrivalDate())
                                        .departureTime(schedule.getDepartureTime())
                                        .arrivalTime(schedule.getArrivalTime())
                                        .stock(schedule.getStock())
                                        .createdAt(schedule.getCreatedAt())
                                        .updatedAt(schedule.getUpdatedAt())
                                        .build())
                                .aircraftSeat(AircraftSeatResponse.builder()
                                        .seatCode(bookingDetail.getSeatCode())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(bookingDetail.getSeatPrice()))
                                                .amount(bookingDetail.getSeatPrice())
                                                .build())
                                        .build())
                                .bagage(BaggageBookingResponse.builder()
                                        .extraBagage(bookingDetail.getExtraBagage())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(baggage.getPrice()))
                                                .amount(baggage.getPrice())
                                                .build())
                                        .freeCabinCapacity(baggage.getFreeCabinBagageCapacity())
                                        .freeBagageCapacity(baggage.getFreeBagageCapacity())
                                        .price(PriceResponse.builder()
                                                .currencyCode(getIndonesiaCurrencyCode())
                                                .display(convertToDisplayCurrency(bookingDetail.getBagagePrice()))
                                                .amount(bookingDetail.getBagagePrice())
                                                .build())
                                        .build())
                                .createdAt(bookingDetail.getCreatedAt())
                                .build();
                        returns.add(returnsBookingResponse);
                    }
                });

                log.info("Success get booking response");
                HistoryResponse historyResponse = HistoryResponse.builder()
                        .totalAmount(PriceResponse.builder()
                                .currencyCode(getIndonesiaCurrencyCode())
                                .display(convertToDisplayCurrency(booking.getTotal()))
                                .amount(booking.getTotal())
                                .build())
                        .bookingId(booking.getId())
                        .bookingType(booking.getBookingType())
                        .adult(booking.getAdult())
                        .child(booking.getChild())
                        .infant(booking.getInfant())
                        .orderedAt(booking.getCreatedAt())
                        .departure(BookingDetailResponse.builder()
                                .data(departures)
                                .build())
                        .returns(BookingDetailResponse.builder()
                                .data(returns)
                                .build())
                        .build();
                historyResponses.add(historyResponse);
            });

            return historyResponses;
        } else {
            throw new ValidationException("Sorting must be ASC or DESC");
        }
    }

    private String convertToDisplayCurrency(BigDecimal amount) {
        Locale indonesia = new Locale("id", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(indonesia);

        String rupiah = numberFormat.format(amount.doubleValue());
        return rupiah;
    }

    private String getIndonesiaCurrencyCode() {
        Locale japan = new Locale("id", "ID");
        Currency currency = Currency.getInstance(japan);

        return currency.getCurrencyCode();
    }
}
