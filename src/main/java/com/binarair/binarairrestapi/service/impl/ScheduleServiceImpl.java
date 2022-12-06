package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.*;
import com.binarair.binarairrestapi.model.request.ScheduleRequest;
import com.binarair.binarairrestapi.model.response.*;
import com.binarair.binarairrestapi.repository.*;
import com.binarair.binarairrestapi.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final static Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;

    private final FacilityRepository facilityRepository;

    private final BenefitRepository benefitRepository;

    private final AircraftRepository aircraftRepository;

    private final AirportRepository airportRepository;

    private final BagageRepository bagageRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, FacilityRepository facilityRepository, BenefitRepository benefitRepository, AircraftRepository aircraftRepository, AirportRepository airportRepository, BagageRepository bagageRepository) {
        this.scheduleRepository = scheduleRepository;
        this.facilityRepository = facilityRepository;
        this.benefitRepository = benefitRepository;
        this.aircraftRepository = aircraftRepository;
        this.airportRepository = airportRepository;
        this.bagageRepository = bagageRepository;
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
            List<Bagage> bagages = bagageRepository.findByAircraftId(departureScheduleOrigin.getAircraft().getId());
            if (bagages.isEmpty()) {
                bagages  = Collections.singletonList(Bagage.builder()
                        .freeBagageCapacity(0).freeCabinBagageCapacity(0)
                        .build());
            }

            PriceResponse priceResponse = PriceResponse.builder()
                    .amount(departureScheduleOrigin.getPrice())
                    .currencyCode(getIndonesiaCurrencyCode())
                    .display(convertToDisplayCurrency(departureScheduleOrigin.getPrice()))
                    .build();
            AircraftResponse aircraftResponse = AircraftResponse.builder()
                    .id(departureScheduleOrigin.getAircraft().getId())
                    .type(departureScheduleOrigin.getAircraft().getModel())
                    .seatArrangement(departureScheduleOrigin.getAircraft().getSeatArrangement())
                    .distanceBetweenSeats(departureScheduleOrigin.getAircraft().getDistanceBetweenSeats())
                    .seatLengthUnit(departureScheduleOrigin.getAircraft().getSeatLengthUnit())
                    .freeBaggage(bagages.get(0).getFreeBagageCapacity())
                    .freeBaggageCabin(bagages.get(0).getFreeCabinBagageCapacity())
                    .build();
            List<Facility> facilities = facilityRepository.findFacilitiesByAircraftId(departureScheduleOrigin.getAircraft().getId());
            List<FacilityResponse> facilityResponses = new ArrayList<>();
            facilities.stream().forEach(facility -> {
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .name(facility.getName())
                        .description(facility.getDescription())
                        .status(facility.isStatus())
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
                    .flightDuration(duration(departureScheduleOrigin.getDepartureTime(), departureScheduleOrigin.getArrivalTime(), departureScheduleOrigin.getDepartureDate(), departureScheduleOrigin.getArrivalDate()))
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
            List<Bagage> bagages = bagageRepository.findByAircraftId(arrivalScheduleDestination.getAircraft().getId());
            if (bagages.isEmpty()) {
                bagages  = Collections.singletonList(Bagage.builder()
                        .freeBagageCapacity(0).freeCabinBagageCapacity(0)
                        .build());
            }

            PriceResponse priceResponse = PriceResponse.builder()
                    .amount(arrivalScheduleDestination.getPrice())
                    .currencyCode(getIndonesiaCurrencyCode())
                    .display(convertToDisplayCurrency(arrivalScheduleDestination.getPrice()))
                    .build();
            AircraftResponse aircraftResponse = AircraftResponse.builder()
                    .id(arrivalScheduleDestination.getAircraft().getId())
                    .type(arrivalScheduleDestination.getAircraft().getModel())
                    .seatArrangement(arrivalScheduleDestination.getAircraft().getSeatArrangement())
                    .distanceBetweenSeats(arrivalScheduleDestination.getAircraft().getDistanceBetweenSeats())
                    .seatLengthUnit(arrivalScheduleDestination.getAircraft().getSeatLengthUnit())
                    .freeBaggage(bagages.get(0).getFreeBagageCapacity())
                    .freeBaggageCabin(bagages.get(0).getFreeCabinBagageCapacity())
                    .build();
            List<Facility> facilities = facilityRepository.findFacilitiesByAircraftId(arrivalScheduleDestination.getAircraft().getId());
            List<FacilityResponse> facilityResponses = new ArrayList<>();
            facilities.stream().forEach(facility -> {
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .name(facility.getName())
                        .description(facility.getDescription())
                        .status(facility.isStatus())
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
                    .flightDuration(duration(arrivalScheduleDestination.getDepartureTime(), arrivalScheduleDestination.getArrivalTime(), arrivalScheduleDestination.getDepartureDate(), arrivalScheduleDestination.getArrivalDate()))
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
            List<Bagage> bagages = bagageRepository.findByAircraftId(departureScheduleOrigin.getAircraft().getId());
            if (bagages.isEmpty()) {
                bagages  = Collections.singletonList(Bagage.builder()
                        .freeBagageCapacity(0).freeCabinBagageCapacity(0)
                        .build());
            }

            PriceResponse priceResponse = PriceResponse.builder()
                    .amount(departureScheduleOrigin.getPrice())
                    .currencyCode(getIndonesiaCurrencyCode())
                    .display(convertToDisplayCurrency(departureScheduleOrigin.getPrice()))
                    .build();
            AircraftResponse aircraftResponse = AircraftResponse.builder()
                    .id(departureScheduleOrigin.getAircraft().getId())
                    .type(departureScheduleOrigin.getAircraft().getModel())
                    .seatArrangement(departureScheduleOrigin.getAircraft().getSeatArrangement())
                    .distanceBetweenSeats(departureScheduleOrigin.getAircraft().getDistanceBetweenSeats())
                    .seatLengthUnit(departureScheduleOrigin.getAircraft().getSeatLengthUnit())
                    .freeBaggage(bagages.get(0).getFreeBagageCapacity())
                    .freeBaggageCabin(bagages.get(0).getFreeCabinBagageCapacity())
                    .build();
            List<Facility> facilities = facilityRepository.findFacilitiesByAircraftId(departureScheduleOrigin.getAircraft().getId());
            List<FacilityResponse> facilityResponses = new ArrayList<>();
            facilities.stream().forEach(facility -> {
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .name(facility.getName())
                        .description(facility.getDescription())
                        .status(facility.isStatus())
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
                    .flightDuration(duration(departureScheduleOrigin.getDepartureTime(), departureScheduleOrigin.getArrivalTime(), departureScheduleOrigin.getDepartureDate(),departureScheduleOrigin.getArrivalDate()))
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

    @Override
    public ScheduleResponse save(ScheduleRequest scheduleRequest) {
        Airport originAirport = airportRepository.findById(scheduleRequest.getOriginIataAirportCode())
                .orElseThrow(() -> new DataNotFoundException(String.format("Airport with iata code %s not found", scheduleRequest.getOriginIataAirportCode())));
        Airport destinationAirport = airportRepository.findById(scheduleRequest.getDestinationIataAirportCode())
                .orElseThrow(() -> new DataNotFoundException(String.format("Airport with iata code %s not found", scheduleRequest.getDestinationIataAirportCode())));
        Aircraft aircraft = aircraftRepository.findById(scheduleRequest.getAircraftId())
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", scheduleRequest.getAircraftId())));

        Schedule schedule = Schedule.builder()
                .id(String.format("x-%s", UUID.randomUUID().toString()))
                .originIataAirportCode(originAirport)
                .destIataAirportCode(destinationAirport)
                .aircraft(aircraft)
                .price(scheduleRequest.getPrice())
                .departureDate(scheduleRequest.getDepartureDate())
                .arrivalDate(scheduleRequest.getArrivalDate())
                .departureTime(scheduleRequest.getDepartureTime())
                .arrivalTime(scheduleRequest.getArrivalTime())
                .stock(scheduleRequest.getStock())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        List<Bagage> bagages = bagageRepository.findByAircraftId(schedule.getAircraft().getId());
        if (bagages.isEmpty()) {
            bagages  = Collections.singletonList(Bagage.builder()
                    .freeBagageCapacity(0).freeCabinBagageCapacity(0)
                    .build());
        }

        log.info("Do save schedule data");
        scheduleRepository.save(schedule);
        log.info("Successul save schedule data");
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .originAirport(AirportResponse.builder()
                        .iata(originAirport.getIataAirportCode())
                        .name(originAirport.getName())
                        .cityCode(originAirport.getCity().getCodeId())
                        .countryCode(originAirport.getCity().getCountry().getCountryCode())
                        .city(originAirport.getCity().getName())
                        .country(originAirport.getCity().getCountry().getName())
                        .createdAt(originAirport.getCreatedAt())
                        .build())
                .destinationAirport(AirportResponse.builder()
                        .iata(destinationAirport.getIataAirportCode())
                        .name(destinationAirport.getName())
                        .cityCode(destinationAirport.getCity().getCodeId())
                        .countryCode(destinationAirport.getCity().getCountry().getCountryCode())
                        .city(destinationAirport.getCity().getName())
                        .country(destinationAirport.getCity().getCountry().getName())
                        .createdAt(destinationAirport.getCreatedAt())
                        .build())
                .aircraft(AircraftResponse.builder()
                        .id(aircraft.getId())
                        .type(aircraft.getModel())
                        .seatArrangement(aircraft.getSeatArrangement())
                        .distanceBetweenSeats(aircraft.getDistanceBetweenSeats())
                        .seatLengthUnit(aircraft.getSeatLengthUnit())
                        .freeBaggage(bagages.get(0).getFreeBagageCapacity())
                        .freeBaggageCabin(bagages.get(0).getFreeCabinBagageCapacity())
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
                .build();
    }

    @Override
    public List<ScheduleResponse> getAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        schedules.stream().forEach(schedule -> {
            List<Bagage> bagages = bagageRepository.findByAircraftId(schedule.getAircraft().getId());
            if (bagages.isEmpty()) {
                bagages  = Collections.singletonList(Bagage.builder()
                        .freeBagageCapacity(0).freeCabinBagageCapacity(0)
                        .build());
            }

            ScheduleResponse scheduleResponse = ScheduleResponse.builder()
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
                            .freeBaggage(bagages.get(0).getFreeBagageCapacity())
                            .freeBaggageCabin(bagages.get(0).getFreeCabinBagageCapacity())
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
                    .build();
            scheduleResponses.add(scheduleResponse);
        });
        return scheduleResponses;
    }

    @Override
    public Boolean delete(String scheduleId) {
        boolean isExists = scheduleRepository.existsById(scheduleId);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Schedule with id %s not found", scheduleId));
        }
        log.info("Do delete data by id");
        scheduleRepository.deleteById(scheduleId);
        log.info("Successful delete data by id");
        return true;
    }

    @Override
    public ScheduleResponse findById(String scheduleId) {
        log.info("Do get schedule data");
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Schedule with id %s not found", scheduleId)));
        List<Bagage> bagages = bagageRepository.findByAircraftId(schedule.getAircraft().getId());
        if (bagages.isEmpty()) {
            bagages  = Collections.singletonList(Bagage.builder()
                    .freeBagageCapacity(0).freeCabinBagageCapacity(0)
                    .build());
        }
        log.info("Successful get shcedule data");
        return ScheduleResponse.builder()
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
                        .freeBaggage(bagages.get(0).getFreeBagageCapacity())
                        .freeBaggageCabin(bagages.get(0).getFreeCabinBagageCapacity())
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
                .build();
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

    private String duration(LocalTime departure, LocalTime arrival, LocalDate departureDate, LocalDate arrivalDate) {
        LocalDateTime localDepartureDate = LocalDateTime.of(departureDate, departure);
        LocalDateTime localArrivalDate = LocalDateTime.of(arrivalDate, arrival);

        LocalDateTime tempDateTime = LocalDateTime.from( localDepartureDate );

        long hours = tempDateTime.until( localArrivalDate, ChronoUnit.HOURS );
        tempDateTime = tempDateTime.plusHours( hours );

        long minutes = tempDateTime.until( localArrivalDate, ChronoUnit.MINUTES );

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
