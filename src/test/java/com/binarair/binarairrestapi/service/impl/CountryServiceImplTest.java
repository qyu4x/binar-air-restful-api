package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataAlreadyExistException;
import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Country;
import com.binarair.binarairrestapi.model.request.CountryRequest;
import com.binarair.binarairrestapi.model.response.CityResponse;
import com.binarair.binarairrestapi.model.response.CountryDetailResponse;
import com.binarair.binarairrestapi.model.response.CountryResponse;
import com.binarair.binarairrestapi.repository.CityRepository;
import com.binarair.binarairrestapi.repository.CountryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    CountryRepository countryRepository;

    @Mock
    CityRepository cityRepository;

    @InjectMocks
    CountryServiceImpl countryService;

    @InjectMocks
    CityServiceImpl cityService;

    @Test
    void testSaveCountrySuccess(){
        CountryRequest countryRequest = CountryRequest.builder()
                .countryCodeId("IDN")
                .name("Indonesia")
                .build();
        Country country = Country.builder()
                .countryCode(countryRequest.getCountryCodeId())
                .name(countryRequest.getName())
                .build();
        Mockito.when(countryRepository.save(ArgumentMatchers.any(Country.class)))
                .thenReturn(country);
        CountryResponse countryResponse = countryService.save(countryRequest);
        Assertions.assertNotNull(countryResponse);
        Assertions.assertEquals(countryResponse.getCountryCodeId(),countryRequest.getCountryCodeId());
        Assertions.assertEquals(countryResponse.getName(),countryRequest.getName());

        Mockito.verify(countryRepository,Mockito.times(1)).save(ArgumentMatchers.any(Country.class));

    }

    @Test
    void testSaveCountryAlreadyExist(){
        CountryRequest countryRequest = CountryRequest.builder()
                .countryCodeId("IDN")
                .name("Indonesia")
                .build();
        Country country = Country.builder()
                .countryCode(countryRequest.getCountryCodeId())
                .name(countryRequest.getName())
                .build();
        Mockito.when(countryRepository.existsById(country.getCountryCode()))
                .thenReturn(true);
        Assertions.assertThrows(DataAlreadyExistException.class, ()-> {
            countryService.save(countryRequest);
        });
        Mockito.verify(countryRepository).existsById(country.getCountryCode());
    }

    @Test
    void testGetAllCountrySuccess(){
        Country country1 = Country.builder()
                .countryCode("Idn")
                .name("Indonesia")
                .build();
        Country country2 = Country.builder()
                .countryCode("Jp")
                .name("Japanese")
                .build();
        City city1 = City.builder()
                .name("Medan")
                .codeId("MDN")
                .country(country1)
                .build();
        City city2 = City.builder()
                .name("Surabaya")
                .country(country1)
                .codeId("SRB")
                .build();
        List<City> cityList = new ArrayList<>();
        cityList.add(city1);
        cityList.add(city2);

        List<Country> countryList = new ArrayList<>();
        countryList.add(country1);
        countryList.add(country2);

        Mockito.when(cityRepository.findAllByCountry(country1.getCountryCode()))
                .thenReturn(cityList);
        Mockito.when(countryRepository.findAll())
                .thenReturn(countryList);
        List<CountryDetailResponse>countryDetailResponseList = countryService.getAll();
        Assertions.assertEquals(2,countryDetailResponseList.size());

        Mockito.verify(cityRepository).findAllByCountry(country1.getCountryCode());
        Mockito.verify(countryRepository).findAll();
    }

    @Test
    void testFindCountryByCodeIdSuccess(){
        Country country = Country.builder()
                .countryCode("IDN")
                .name("Indonesia")
                .build();
        City city1 = City.builder()
                .codeId("JKT")
                .country(country)
                .name("Jakarta")
                .build();
        City city2 = City.builder()
                .codeId("MDN")
                .country(country)
                .name("Medan")
                .build();

        List<City> cityList = new ArrayList<>();
        cityList.add(city1);
        cityList.add(city2);
        Mockito.when(countryRepository.findById(country.getCountryCode()))
                .thenReturn(Optional.of(country));
        Mockito.when(cityRepository.findAllByCountry(country.getCountryCode()))
                .thenReturn(cityList);
        CountryDetailResponse countryDetailResponse = countryService.findByCodeId(country.getCountryCode());
        Assertions.assertEquals("IDN",countryDetailResponse.getCountryCodeId());
        Assertions.assertEquals("Indonesia",countryDetailResponse.getName());
        Mockito.verify(cityRepository).findAllByCountry(country.getCountryCode());
        Mockito.verify(countryRepository).findById(country.getCountryCode());
    }

    @Test
    void testFindCountryByCodeIdNotFound(){
        Country country = Country.builder()
                .countryCode("IDN")
                .name("Indonesia")
                .build();


        Mockito.when(countryRepository.findById(country.getCountryCode()))
                .thenReturn(Optional.empty());


        Assertions.assertThrows(DataNotFoundException.class,() -> {
            countryService.findByCodeId(country.getCountryCode());
        });

        Mockito.verify(countryRepository).findById(country.getCountryCode());
    }

    @Test
    void testDeleteCountrySuccess(){
        String countryCode = "country-code";
        Mockito.when(countryRepository.existsById(countryCode))
                .thenReturn(true);
        Boolean stats = countryService.delete(countryCode);
        Assertions.assertTrue(stats);
        Mockito.verify(countryRepository).existsById(countryCode);
    }

    @Test
    void testDeleteCountryNotFound(){
        String countryCode = "country-code";
        Mockito.when(countryRepository.existsById(countryCode))
                .thenReturn(false);
        Assertions.assertThrows(DataNotFoundException.class,() -> {
            countryService.delete(countryCode);
        });
        Mockito.verify(countryRepository).existsById(countryCode);
    }


}
