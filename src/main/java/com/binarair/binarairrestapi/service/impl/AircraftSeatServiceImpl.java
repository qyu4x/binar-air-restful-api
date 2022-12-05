package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Aircraft;
import com.binarair.binarairrestapi.model.entity.AircraftSeat;
import com.binarair.binarairrestapi.model.request.AircraftSeatRequest;
import com.binarair.binarairrestapi.model.response.AircraftSeatResponse;
import com.binarair.binarairrestapi.model.response.PriceResponse;
import com.binarair.binarairrestapi.repository.AircraftRepository;
import com.binarair.binarairrestapi.repository.AircraftSeatRepository;
import com.binarair.binarairrestapi.service.AircraftSeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AircraftSeatServiceImpl implements AircraftSeatService {

    private final static Logger log = LoggerFactory.getLogger(AircraftSeatServiceImpl.class);

    private final AircraftSeatRepository aircraftSeatRepository;

    private final AircraftRepository aircraftRepository;

    @Autowired
    public AircraftSeatServiceImpl(AircraftSeatRepository aircraftSeatRepository, AircraftRepository aircraftRepository) {
        this.aircraftSeatRepository = aircraftSeatRepository;
        this.aircraftRepository = aircraftRepository;
    }


    @Override
    public List<AircraftSeatResponse> findAllAvailableSeat(String aircraftId) {
        aircraftRepository.findById(aircraftId)
                        .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", aircraftId)));
        log.info("Do get all available seat from db");
        List<AircraftSeat> availableSeats = aircraftSeatRepository.findAllAvailableSeatByAircraftId(aircraftId);
        log.info("Successful get all available seat form db");
        List<AircraftSeatResponse> aircraftSeatResponses = new ArrayList<>();
        availableSeats.stream().forEach(availableSeat -> {
            AircraftSeatResponse airCraftSeatResponse = AircraftSeatResponse.builder()
                    .seatId(availableSeat.getId())
                    .seatCode(availableSeat.getSeatCode())
                    .aircraftId(availableSeat.getAircraft().getId())
                    .aircraftManufacture(availableSeat.getAircraft().getAircraftManufacture().getName())
                    .aircraftModel(availableSeat.getAircraft().getModel())
                    .status(true)
                    .price(PriceResponse.builder()
                            .amount(availableSeat.getPrice())
                            .display(convertToDisplayCurrency(availableSeat.getPrice()))
                            .currencyCode(getIndonesiaCurrencyCode())
                            .build())
                    .createdAt(availableSeat.getCreatedAt())
                    .upatedAt(availableSeat.getUpdatedAt())
                    .build();
            aircraftSeatResponses.add(airCraftSeatResponse);
        });
        return aircraftSeatResponses;
    }

    @Override
    public List<AircraftSeatResponse> findAllReservedSeat(String aircraftId) {
        aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", aircraftId)));
        log.info("Do get all reserved seat from db");
        List<AircraftSeat> reservedSeats = aircraftSeatRepository.findAllReservedSeatByAircraftId(aircraftId);
        log.info("Successful get all reserved seat form db");
        List<AircraftSeatResponse> aircraftSeatResponses = new ArrayList<>();
        reservedSeats.stream().forEach(reservedSeat -> {
            AircraftSeatResponse airCraftSeatResponse = AircraftSeatResponse.builder()
                    .seatId(reservedSeat.getId())
                    .seatCode(reservedSeat.getSeatCode())
                    .aircraftId(reservedSeat.getAircraft().getId())
                    .aircraftManufacture(reservedSeat.getAircraft().getAircraftManufacture().getName())
                    .aircraftModel(reservedSeat.getAircraft().getModel())
                    .status(false)
                    .price(PriceResponse.builder()
                            .amount(reservedSeat.getPrice())
                            .display(convertToDisplayCurrency(reservedSeat.getPrice()))
                            .currencyCode(getIndonesiaCurrencyCode())
                            .build())
                    .createdAt(reservedSeat.getCreatedAt())
                    .upatedAt(reservedSeat.getUpdatedAt())
                    .build();
            aircraftSeatResponses.add(airCraftSeatResponse);
        });
        return aircraftSeatResponses;
    }

    @Override
    public List<AircraftSeatResponse> getAllByAircraftId(String aircraftId) {
        aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", aircraftId)));
        log.info("Do get all seat with aircraft id");
        List<AircraftSeat> aircrafts = aircraftSeatRepository.findAllByAircraftId(aircraftId);
        List<AircraftSeatResponse> aircraftSeatResponses = new ArrayList<>();
        aircrafts.stream().forEach(aircraftSeat -> {
            boolean status = false;
            if (aircraftSeat.getSeatScheduleBookings().isEmpty()) {
                status = true;
            }
            AircraftSeatResponse aircraftSeatResponse = AircraftSeatResponse.builder()
                    .seatId(aircraftSeat.getId())
                    .seatCode(aircraftSeat.getSeatCode())
                    .aircraftId(aircraftSeat.getAircraft().getId())
                    .aircraftManufacture(aircraftSeat.getAircraft().getAircraftManufacture().getName())
                    .aircraftModel(aircraftSeat.getAircraft().getModel())
                    .status(status)
                    .price(PriceResponse.builder()
                            .amount(aircraftSeat.getPrice())
                            .display(convertToDisplayCurrency(aircraftSeat.getPrice()))
                            .currencyCode(getIndonesiaCurrencyCode())
                            .build())
                    .createdAt(aircraftSeat.getCreatedAt())
                    .upatedAt(aircraftSeat.getUpdatedAt())
                    .build();
            aircraftSeatResponses.add(aircraftSeatResponse);
            log.info("Successful get all seat airraft");
        });
        return aircraftSeatResponses;
    }

    @Override
    public AircraftSeatResponse save(AircraftSeatRequest aircraftSeatRequest) {
        AircraftSeat isExists = aircraftSeatRepository.findByAircraftAndSeatCode(aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode());
        if (isExists != null) {
            log.warn("Pairs aircraft and seat is already exists");
            throw new DataAlreadyExistException(String.format("Seat with aircraft %s and seat code %s already exist",aircraftSeatRequest.getAircraftId(), aircraftSeatRequest.getSeatCode()));
        }
        Aircraft aircraft =  aircraftRepository.findById(aircraftSeatRequest.getAircraftId())
                .orElseThrow(() -> new DataNotFoundException(String.format("Aircraft with id %s not found", aircraftSeatRequest.getAircraftId())));
        log.info("Do save aircraft seat");
        AircraftSeat airCraftSeat = AircraftSeat.builder()
                .id(String.format("as-%s", UUID.randomUUID().toString()))
                .seatCode(aircraftSeatRequest.getSeatCode())
                .price(aircraftSeatRequest.getPrice())
                .createdAt(LocalDateTime.now())
                .aircraft(aircraft)
                .build();
        aircraftSeatRepository.save(airCraftSeat);
        log.info("Succesful save aircraft seat data");
        return AircraftSeatResponse.builder()
                .seatId(airCraftSeat.getId())
                .seatCode(airCraftSeat.getSeatCode())
                .aircraftId(airCraftSeat.getAircraft().getId())
                .aircraftManufacture(airCraftSeat.getAircraft().getAircraftManufacture().getName())
                .aircraftModel(airCraftSeat.getAircraft().getModel())
                .status(true)
                .price(PriceResponse.builder()
                        .amount(airCraftSeat.getPrice())
                        .display(convertToDisplayCurrency(airCraftSeat.getPrice()))
                        .currencyCode(getIndonesiaCurrencyCode())
                        .build())
                .createdAt(airCraftSeat.getCreatedAt())
                .upatedAt(airCraftSeat.getUpdatedAt())
                .build();
    }

    @Override
    public Boolean delete(String id) {
        boolean isExists = aircraftSeatRepository.existsById(id);
        if (!isExists) {
            throw new DataNotFoundException(String.format("Data seat with id %s not found", id));
        }
        log.info("Do delete data aircraft seat");
        aircraftSeatRepository .deleteById(id);
        log.info("Successful delete aircraft seat data");
        return true;
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
