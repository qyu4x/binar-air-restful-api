package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Benefit;
import com.binarair.binarairrestapi.model.entity.Facility;
import com.binarair.binarairrestapi.model.entity.Schedule;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.repository.BenefitRepository;
import com.binarair.binarairrestapi.repository.FacilityRepository;
import com.binarair.binarairrestapi.repository.ScheduleRepository;
import com.binarair.binarairrestapi.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final static Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;

    private final FacilityRepository facilityRepository;

    private final BenefitRepository benefitRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, FacilityRepository facilityRepository, BenefitRepository benefitRepository) {
        this.scheduleRepository = scheduleRepository;
        this.facilityRepository = facilityRepository;
        this.benefitRepository = benefitRepository;
    }

    @Override
    public RoundTripTicketResponse filterFullTwoSearch(String airport, String departure, String passenger, String serviceClass) {
        String[] splitAirport = splitValue(airport);
        String[] splitDeparture = splitValue(departure);
        String[] splitPassenger = splitValue(passenger);
        Integer totalPassengers = 0;
        for(int i = 0; i < splitPassenger.length-1; i++) {
            totalPassengers += Integer.valueOf(splitPassenger[i]);
        }

        if (splitDeparture[0].isEmpty()|| splitDeparture[1].isEmpty() || splitAirport.length < 2 || serviceClass.isEmpty()) {
            log.info("request is invalid");
            throw new DataNotFoundException("Opps, invalid request");
        }
        LocalDate originDeparture = convertToLocalDate(splitDeparture[0]);
        LocalDate departureFromDestination = convertToLocalDate(splitDeparture[1]);
        String originAirport = splitAirport[0];
        String destinationAirport = splitAirport[1];

        List<Schedule> schedulesFromOriginResponses = scheduleRepository
                .findFullTwoSearch(originDeparture, originAirport, destinationAirport, serviceClass.toUpperCase());
        log.info("Success to get flight origin data");
        List<TicketResponse> departureScheduleFromOrigins = new ArrayList<>();
        if (schedulesFromOriginResponses.size() < 1) {
            log.warn("Flight not available, please choose another flight");
            throw new DataNotFoundException("Flight not available, please choose another flight");
        }
        Integer finalTotalPassengers = totalPassengers;
        schedulesFromOriginResponses.stream().filter(schedule -> schedule.getStock() >= finalTotalPassengers).forEach(departureScheduleOrigin -> {
            PriceResponse priceResponse = PriceResponse.builder()
                    .amount(departureScheduleOrigin.getPrice())
                    .currencyCode(getIndonesiaCurrencyCode())
                    .display(convertToDisplayCurrency(departureScheduleOrigin.getPrice()))
                    .build();
            AircraftResponse aircraftResponse = AircraftResponse.builder()
                    .type(departureScheduleOrigin.getAircraft().getModel())
                    .seatArrangement(departureScheduleOrigin.getAircraft().getSeatArrangement())
                    .distanceBetweenSeats(departureScheduleOrigin.getAircraft().getDistanceBetweenSeats())
                    .seatLengthUnit(departureScheduleOrigin.getAircraft().getSeatLengthUnit())
                    .build();
            List<Facility> facilities = facilityRepository.findFacilitiesByAircraftId(departureScheduleOrigin.getAircraft().getId());
            List<FacilityResponse> facilityResponses = new ArrayList<>();
            facilities.stream().forEach(facility -> {
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .name(facility.getName())
                        .description(facility.getDescription())
                        .build();
                facilityResponses.add(facilityResponse);
            });

            List<Benefit> benerfits = benefitRepository.findBenefitByAircraftId(departureScheduleOrigin.getAircraft().getId());
            List<BenefitResponse> benefitResponses = new ArrayList<>();
            benerfits.stream().forEach(benefit -> {
                BenefitResponse benefitResponse = BenefitResponse.builder()
                        .name(benefit.getName())
                        .description(benefit.getDescription())
                        .status(benefit.isStatus())
                        .build();
                benefitResponses.add(benefitResponse);
            });

            TicketResponse ticketResponse = TicketResponse.builder()
                    .id(departureScheduleOrigin.getId())
                    .airlines(departureScheduleOrigin.getAircraft().getAirlines().getName())
                    .flightClass(departureScheduleOrigin.getAircraft().getTravelClass().getName())
                    .airLinesLogoURL(departureScheduleOrigin.getAircraft().getAirlines().getLogoURL())
                    .iataOriginAirport(departureScheduleOrigin.getOriginIataAirportCode().getIataAirportCode())
                    .originAirport(departureScheduleOrigin.getOriginIataAirportCode().getName())
                    .originCity(departureScheduleOrigin.getOriginIataAirportCode().getCity().getName())
                    .iataDestinationAirport(departureScheduleOrigin.getDestIataAirportCode().getIataAirportCode())
                    .destinationAirport(departureScheduleOrigin.getDestIataAirportCode().getName())
                    .destinationCity(departureScheduleOrigin.getDestIataAirportCode().getCity().getName())
                    .departureDate(departureScheduleOrigin.getDepartureDate())
                    .departureTime(departureScheduleOrigin.getDepartureTime())
                    .arrivalDate(departureScheduleOrigin.getArrivalDate())
                    .arrivalTime(departureScheduleOrigin.getArrivalTime())
                    .flightDuration(duration(departureScheduleOrigin.getDepartureTime(), departureScheduleOrigin.getArrivalTime()))
                    .stock(departureScheduleOrigin.getStock())
                    .sold(departureScheduleOrigin.getSold())
                    .capacity(departureScheduleOrigin.getAircraft().getPassangerCapacity())
                    .price(priceResponse)
                    .aircraft(aircraftResponse)
                    .facilities(facilityResponses)
                    .benefits(benefitResponses)
                    .createdAt(departureScheduleOrigin.getCreatedAt())
                    .updatedAt(departureScheduleOrigin.getUpdatedAt())
                    .build();

            departureScheduleFromOrigins.add(ticketResponse);
        });

        List<Schedule> schedulesFromDestinationResponses = scheduleRepository
                .findFullTwoSearch(departureFromDestination, destinationAirport, originAirport, serviceClass.toUpperCase());
        log.info("Success to get flight dest data");
        List<TicketResponse> departureScheduleFromDestinations = new ArrayList<>();
        if (schedulesFromDestinationResponses.size() < 1) {
            log.info("Flight to return not available, please choose another flight");
            throw new DataNotFoundException("Flight to return not available, please choose another flight");
        }

        schedulesFromDestinationResponses.stream().filter(schedule -> schedule.getStock() >= finalTotalPassengers).forEach(arrivalScheduleDestination -> {
            PriceResponse priceResponse = PriceResponse.builder()
                    .amount(arrivalScheduleDestination.getPrice())
                    .currencyCode(getIndonesiaCurrencyCode())
                    .display(convertToDisplayCurrency(arrivalScheduleDestination.getPrice()))
                    .build();
            AircraftResponse aircraftResponse = AircraftResponse.builder()
                    .type(arrivalScheduleDestination.getAircraft().getModel())
                    .seatArrangement(arrivalScheduleDestination.getAircraft().getSeatArrangement())
                    .distanceBetweenSeats(arrivalScheduleDestination.getAircraft().getDistanceBetweenSeats())
                    .seatLengthUnit(arrivalScheduleDestination.getAircraft().getSeatLengthUnit())
                    .build();
            List<Facility> facilities = facilityRepository.findFacilitiesByAircraftId(arrivalScheduleDestination.getAircraft().getId());
            List<FacilityResponse> facilityResponses = new ArrayList<>();
            facilities.stream().forEach(facility -> {
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .name(facility.getName())
                        .description(facility.getDescription())
                        .build();
                facilityResponses.add(facilityResponse);
            });
            List<Benefit> benefits = benefitRepository.findBenefitByAircraftId(arrivalScheduleDestination.getAircraft().getId());
            List<BenefitResponse> benefitResponses = new ArrayList<>();
            benefits.stream().forEach(benefit -> {
                BenefitResponse benefitResponse = BenefitResponse.builder()
                        .name(benefit.getName())
                        .description(benefit.getDescription())
                        .status(benefit.isStatus())
                        .build();
                benefitResponses.add(benefitResponse);
            });
            TicketResponse ticketResponse = TicketResponse.builder()
                    .id(arrivalScheduleDestination.getId())
                    .airlines(arrivalScheduleDestination.getAircraft().getAirlines().getName())
                    .flightClass(arrivalScheduleDestination.getAircraft().getTravelClass().getName())
                    .airLinesLogoURL(arrivalScheduleDestination.getAircraft().getAirlines().getLogoURL())
                    .iataOriginAirport(arrivalScheduleDestination.getOriginIataAirportCode().getIataAirportCode())
                    .originAirport(arrivalScheduleDestination.getOriginIataAirportCode().getName())
                    .originCity(arrivalScheduleDestination.getOriginIataAirportCode().getCity().getName())
                    .iataDestinationAirport(arrivalScheduleDestination.getDestIataAirportCode().getIataAirportCode())
                    .destinationAirport(arrivalScheduleDestination.getDestIataAirportCode().getName())
                    .destinationCity(arrivalScheduleDestination.getDestIataAirportCode().getCity().getName())
                    .departureDate(arrivalScheduleDestination.getDepartureDate())
                    .departureTime(arrivalScheduleDestination.getDepartureTime())
                    .arrivalDate(arrivalScheduleDestination.getArrivalDate())
                    .arrivalTime(arrivalScheduleDestination.getArrivalTime())
                    .flightDuration(duration(arrivalScheduleDestination.getDepartureTime(), arrivalScheduleDestination.getArrivalTime()))
                    .stock(arrivalScheduleDestination.getStock())
                    .sold(arrivalScheduleDestination.getSold())
                    .capacity(arrivalScheduleDestination.getAircraft().getPassangerCapacity())
                    .price(priceResponse)
                    .aircraft(aircraftResponse)
                    .facilities(facilityResponses)
                    .benefits(benefitResponses)
                    .createdAt(arrivalScheduleDestination.getCreatedAt())
                    .updatedAt(arrivalScheduleDestination.getUpdatedAt())
                    .build();
            departureScheduleFromDestinations.add(ticketResponse);
        });

        return RoundTripTicketResponse.builder()
                .departures(departureScheduleFromOrigins)
                .returns(departureScheduleFromDestinations)
                .build();
    }

    @Override
    public List<TicketResponse> filterFullSearch(String airport, String departure, String passenger, String serviceClass) {
        String[] splitAirport = splitValue(airport);
        String[] splitDeparture = splitValue(departure);
        String[] splitPassenger = splitValue(passenger);
        Integer totalPassengers = 0;
        for(int i = 0; i < splitPassenger.length-1; i++) {
            totalPassengers += Integer.valueOf(splitPassenger[i]);
        }

        if (splitDeparture[0].isEmpty()|| !splitDeparture[1].equals("NA") || splitAirport.length < 2 || serviceClass.isEmpty()) {
            log.info("request is invalid");
            throw new DataNotFoundException("Opps, invalid request");
        }
        LocalDate originDeparture = convertToLocalDate(splitDeparture[0]);
        String originAirport = splitAirport[0];
        String destinationAirport = splitAirport[1];

        List<Schedule> schedulesFromOriginResponses = scheduleRepository
                .findFullTwoSearch(originDeparture, originAirport, destinationAirport, serviceClass.toUpperCase());
        log.info("Success to get flight origin data");
        List<TicketResponse> ticketResponses = new ArrayList<>();
        if (schedulesFromOriginResponses.size() < 1) {
            log.warn("Flight not available, please choose another flight");
            throw new DataNotFoundException("Flight not available, please choose another flight");
        }
        Integer finalTotalPassengers = totalPassengers;
        schedulesFromOriginResponses.stream().filter(schedule -> schedule.getStock() >= finalTotalPassengers).forEach(departureScheduleOrigin -> {
            PriceResponse priceResponse = PriceResponse.builder()
                    .amount(departureScheduleOrigin.getPrice())
                    .currencyCode(getIndonesiaCurrencyCode())
                    .display(convertToDisplayCurrency(departureScheduleOrigin.getPrice()))
                    .build();
            AircraftResponse aircraftResponse = AircraftResponse.builder()
                    .type(departureScheduleOrigin.getAircraft().getModel())
                    .seatArrangement(departureScheduleOrigin.getAircraft().getSeatArrangement())
                    .distanceBetweenSeats(departureScheduleOrigin.getAircraft().getDistanceBetweenSeats())
                    .seatLengthUnit(departureScheduleOrigin.getAircraft().getSeatLengthUnit())
                    .build();
            List<Facility> facilities = facilityRepository.findFacilitiesByAircraftId(departureScheduleOrigin.getAircraft().getId());
            List<FacilityResponse> facilityResponses = new ArrayList<>();
            facilities.stream().forEach(facility -> {
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .name(facility.getName())
                        .description(facility.getDescription())
                        .build();
                facilityResponses.add(facilityResponse);
            });

            List<Benefit> benerfits = benefitRepository.findBenefitByAircraftId(departureScheduleOrigin.getAircraft().getId());
            List<BenefitResponse> benefitResponses = new ArrayList<>();
            benerfits.stream().forEach(benefit -> {
                BenefitResponse benefitResponse = BenefitResponse.builder()
                        .name(benefit.getName())
                        .description(benefit.getDescription())
                        .status(benefit.isStatus())
                        .build();
                benefitResponses.add(benefitResponse);
            });

            TicketResponse ticketResponse = TicketResponse.builder()
                    .id(departureScheduleOrigin.getId())
                    .airlines(departureScheduleOrigin.getAircraft().getAirlines().getName())
                    .flightClass(departureScheduleOrigin.getAircraft().getTravelClass().getName())
                    .airLinesLogoURL(departureScheduleOrigin.getAircraft().getAirlines().getLogoURL())
                    .iataOriginAirport(departureScheduleOrigin.getOriginIataAirportCode().getIataAirportCode())
                    .originAirport(departureScheduleOrigin.getOriginIataAirportCode().getName())
                    .originCity(departureScheduleOrigin.getOriginIataAirportCode().getCity().getName())
                    .iataDestinationAirport(departureScheduleOrigin.getDestIataAirportCode().getIataAirportCode())
                    .destinationAirport(departureScheduleOrigin.getDestIataAirportCode().getName())
                    .destinationCity(departureScheduleOrigin.getDestIataAirportCode().getCity().getName())
                    .departureDate(departureScheduleOrigin.getDepartureDate())
                    .departureTime(departureScheduleOrigin.getDepartureTime())
                    .arrivalDate(departureScheduleOrigin.getArrivalDate())
                    .arrivalTime(departureScheduleOrigin.getArrivalTime())
                    .flightDuration(duration(departureScheduleOrigin.getDepartureTime(), departureScheduleOrigin.getArrivalTime()))
                    .stock(departureScheduleOrigin.getStock())
                    .sold(departureScheduleOrigin.getSold())
                    .capacity(departureScheduleOrigin.getAircraft().getPassangerCapacity())
                    .price(priceResponse)
                    .aircraft(aircraftResponse)
                    .facilities(facilityResponses)
                    .benefits(benefitResponses)
                    .createdAt(departureScheduleOrigin.getCreatedAt())
                    .updatedAt(departureScheduleOrigin.getUpdatedAt())
                    .build();

            ticketResponses.add(ticketResponse);
        });

        return ticketResponses;
    }

    private String[] splitValue(String value) {
        return value.split(Pattern.quote("."));
    }

    private LocalDate convertToLocalDate(String departureDate) {
        if (departureDate.split("-")[0].length() == 1) {
            departureDate = String.format("0%s", departureDate);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(departureDate, formatter);
    }

    private String duration(LocalTime departure, LocalTime arrival) {
        Duration duration = Duration.between(departure, arrival);
        long totalSecs = duration.getSeconds();
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        return String.format("%02dh %02dm", hours, minutes);
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
