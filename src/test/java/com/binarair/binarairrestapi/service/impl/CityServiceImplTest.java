package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.Bagage;
import com.binarair.binarairrestapi.model.entity.City;
import com.binarair.binarairrestapi.model.entity.Country;
import com.binarair.binarairrestapi.model.request.CityRequest;
import com.binarair.binarairrestapi.model.response.CityResponse;
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
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    void testSaveCitySuccess() {
        CityRequest cityRequest = CityRequest.builder()
                .cityId("DT")
                .countryCodeId("Valve")
                .name("Dota")
                .build();
        Country country = Country.builder()
                .countryCode("Valve")
                .name("Jkt")
                .build();
        City city = City.builder()
                .codeId("code")
                .country(country)
                .name(cityRequest.getName())
                .build();
        Mockito.when(countryRepository.findById(cityRequest.getCountryCodeId()))
                .thenReturn(Optional.of(country));
        Mockito.when(cityRepository.save(ArgumentMatchers.any(City.class)))
                .thenReturn(city);
        CityResponse cityResponse = cityService.save(cityRequest);
        Assertions.assertNotNull(cityResponse);
        Assertions.assertEquals(cityResponse.getCityId(), cityRequest.getCityId());
        Assertions.assertEquals(cityResponse.getCityName(), cityRequest.getName());
        Assertions.assertEquals(cityResponse.getCountryCodeId(), cityRequest.getCountryCodeId());

        Mockito.verify(countryRepository, Mockito.times(1)).findById(cityRequest.getCountryCodeId());
        Mockito.verify(cityRepository, Mockito.times(1)).save(ArgumentMatchers.any(City.class));

    }

    @Test
    void testSaveCityNotFound() {
        CityRequest cityRequest = CityRequest.builder()
                .cityId("DT")
                .countryCodeId("Valve")
                .name("Dota")
                .build();
        Mockito.when(countryRepository.findById(cityRequest.getCountryCodeId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            cityService.save(cityRequest);
        });
        Mockito.verify(countryRepository).findById(cityRequest.getCountryCodeId());
    }

    @Test
    void testGetAllCitySuccess() {
        Country country = Country.builder()
                .countryCode("Idn")
                .name("Indonesia")
                .build();
        City city1 = City.builder()
                .codeId("code1")
                .country(country)
                .name("Medan")
                .build();
        City city2 = City.builder()
                .codeId("code2")
                .country(country)
                .name("Sunggal")
                .build();
        List<City> cityList = new ArrayList<>();
        cityList.add(city1);
        cityList.add(city2);

        Mockito.when(cityRepository.findAll())
                .thenReturn(cityList);
        List<CityResponse> cityResponseList = cityService.getAll();
        Assertions.assertEquals(2, cityResponseList.size());

        Mockito.verify(cityRepository).findAll();
    }


    @Test
    void testFindCityByCodeSuccess() {
        Country country = Country.builder()
                .countryCode("Idn")
                .name("Indonesia")
                .build();
        City city = City.builder()
                .codeId("code1")
                .country(country)
                .name("Medan")
                .build();
        Mockito.when(cityRepository.findById(city.getCodeId()))
                .thenReturn(Optional.of(city));
        CityResponse cityResponse = cityService.findByCode(city.getCodeId());
        Assertions.assertEquals("Idn", city.getCountry().getCountryCode());
        Assertions.assertEquals(cityResponse.getCityName(), city.getName());
        Mockito.verify(cityRepository).findById(city.getCodeId());
    }

    @Test
    void testFindCityByCodeNotFound() {
        City city = City.builder()
                .codeId("code1")
                .name("Medan")
                .build();
        Mockito.when(cityRepository.findById(city.getCodeId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            cityService.findByCode(city.getCodeId());
        });

        Mockito.verify(cityRepository).findById(city.getCodeId());

    }

    @Test
    void testDeleteCitySuccess() {
        String cityCodeId = "Code1";
        Mockito.when(cityRepository.existsById(cityCodeId))
                .thenReturn(true);
        Boolean stats = cityService.delete(cityCodeId);

        Assertions.assertTrue(stats);
        Mockito.verify(cityRepository).existsById(cityCodeId);
    }

    @Test
    void testDeleteCityNotFound() {
        String cityCodeId = "Code1";
        Mockito.when(cityRepository.existsById(cityCodeId))
                .thenReturn(false);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            cityService.delete(cityCodeId);
        });
        Mockito.verify(cityRepository).existsById(cityCodeId);
    }

}
