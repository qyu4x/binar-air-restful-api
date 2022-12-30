package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Airport;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Country;
import com.binarair.binarairrestapi.model.request.AirportRequest;
import com.binarair.binarairrestapi.model.response.AirportResponse;
import com.binarair.binarairrestapi.repository.AirportRepository;
import com.binarair.binarairrestapi.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @Mock
    AirportRepository airportRepository;

    @Mock
    CityRepository cityRepository;

    @InjectMocks
    AirportServiceImpl airportService;

    @Test
    void testSaveAirportSuccessful() {
        Country country = Country.builder()
                .name("Indonesia")
                .countryCode("IDN")
                .build();
        City city = City.builder()
                .codeId("random-code-id")
                .name("Medan")
                .country(country)
                .build();
        AirportRequest airportRequest = AirportRequest.builder()
                .cityCode("random-code-id")
                .name("airport Kematian")
                .iata("random-iata-number")
                .build();
        Mockito.when(airportRepository.existsById(airportRequest.getIata()))
                .thenReturn(false);
        Mockito.when(cityRepository.findById(city.getCodeId()))
                .thenReturn(Optional.of(city));
        AirportResponse airportResponse = airportService.save(airportRequest);
        Assertions.assertNotNull(airportResponse);

        Assertions.assertEquals(airportResponse.getName(), airportRequest.getName());
        Assertions.assertEquals(airportResponse.getCityCode(), airportRequest.getCityCode());
        Assertions.assertEquals(airportResponse.getCity(), city.getName());
        Assertions.assertEquals(airportResponse.getCountryCode(), country.getCountryCode());
        Assertions.assertEquals(airportResponse.getCountry(), country.getName());
        Assertions.assertEquals(airportResponse.getIata(), airportRequest.getIata());

        Mockito.verify(airportRepository).existsById(airportRequest.getIata());
        Mockito.verify(cityRepository).findById(city.getCodeId());
    }

    @Test
    void testSaveAirportExistAlready() {
        Country country = Country.builder()
                .name("Indonesia")
                .countryCode("IDN")
                .build();
        City city = City.builder()
                .codeId("random-code-id")
                .name("Medan")
                .country(country)
                .build();
        AirportRequest airportRequest = AirportRequest.builder()
                .cityCode("random-code-id")
                .name("airport Kematian")
                .iata("random-iata-number")
                .build();

        Mockito.when(airportRepository.existsById(airportRequest.getIata()))
                .thenReturn(true);
        Mockito.when(cityRepository.findById(city.getCodeId()))
                .thenReturn(Optional.of(city));
        Assertions.assertThrows(DataAlreadyExistException.class, () -> {
            airportService.save(airportRequest);
        });
        Mockito.verify(airportRepository).existsById(airportRequest.getIata());
    }

    @Test
    void testSaveAirportNoCityFound() {
        AirportRequest airportRequest = AirportRequest.builder()
                .cityCode("random-code-id")
                .name("airport Kematian")
                .iata("random-iata-number")
                .build();
        String cityId = "random-code-id";
        Mockito.when(cityRepository.findById(cityId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            airportService.save(airportRequest);
        });
        Mockito.verify(cityRepository).findById(cityId);
    }

    @Test
    void testFindAirportByIataSuccess() {
        Country country = Country.builder()
                .name("Indonesia")
                .countryCode("IDN")
                .build();
        City city = City.builder()
                .codeId("random-code-id")
                .name("Medan")
                .country(country)
                .build();
        Airport airport = Airport.builder()
                .iataAirportCode("random-iata-airport-code")
                .name("random-airport-name")
                .city(city)
                .build();
        Mockito.when(airportRepository.findById(airport.getIataAirportCode()))
                .thenReturn(Optional.of(airport));
        AirportResponse airportResponse = airportService.findByIata(airport.getIataAirportCode());
        Assertions.assertNotNull(airportResponse);
        Assertions.assertEquals(airportResponse.getName(), airport.getName());
        Assertions.assertEquals(airportResponse.getIata(), airport.getIataAirportCode());
        Mockito.verify(airportRepository).findById(airport.getIataAirportCode());
    }

    @Test
    void testFindAirportNotFound() {
        String iata = "random-iata";
        Mockito.when(airportRepository.findById(iata))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            airportService.findByIata(iata);
        });
        Mockito.verify(airportRepository).findById(iata);
    }

    @Test
    void testGetAllAirportSuccess(){
        Country country = Country.builder()
                .name("Indonesia")
                .countryCode("IDN")
                .build();
        City city = City.builder()
                .codeId("random-code-id")
                .name("Medan")
                .country(country)
                .build();
        Airport airport1 = Airport.builder()
                .city(city)
                .name("Airport from heaven")
                .iataAirportCode("Iata Angel")
                .build();
        Airport airport2 = Airport.builder()
                .city(city)
                .name("Airport from hell")
                .iataAirportCode("Iata Lucifer")
                .build();
        List<Airport> airportList = new ArrayList<>();
        airportList.add(airport1);
        airportList.add(airport2);

        Mockito.when(airportRepository.findAll())
                .thenReturn(airportList);
        List<AirportResponse>airportResponseList = airportService.getAll();
        Assertions.assertEquals(2,airportResponseList.size());

        Mockito.verify(airportRepository).findAll();
    }

    @Test
    void testDeleteSuccess(){
        String iata = "random-iata";
        Mockito.when(airportRepository.existsById(iata))
                .thenReturn(true);
        Boolean stats = airportService.delete(iata);
        Assertions.assertTrue(stats);
        Mockito.verify(airportRepository).existsById(iata);
    }

    @Test
    void testDeleteFail(){
        String iata = "random-iata";
        Mockito.when(airportRepository.existsById(iata))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class,()-> {
            airportService.delete(iata);
        });
        Mockito.verify(airportRepository).existsById(iata);
    }

}
