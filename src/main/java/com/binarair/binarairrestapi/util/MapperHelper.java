package com.binarair.binarairrestapi.util;

import com.binarair.binarairrestapi.exception.ConversionException;
import com.binarair.binarairrestapi.model.entity.HeroBanner;
import com.binarair.binarairrestapi.model.request.AirlineRequest;
import com.binarair.binarairrestapi.model.request.HeroBannerRequest;
import com.binarair.binarairrestapi.model.request.PromoBannerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperHelper {

    public static HeroBannerRequest mapperToHeroBanner(String heroBannerRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(heroBannerRequest, HeroBannerRequest.class);
        } catch (JsonProcessingException exception) {
            throw new ConversionException("Invalid json data format");
        }
    }

    public static PromoBannerRequest mapperToPromoBanner(String promoBannerRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(promoBannerRequest, PromoBannerRequest.class);
        } catch (JsonProcessingException exception) {
            throw new ConversionException("Invalid json data format");
        }
    }

    public static AirlineRequest mapperToAirline(String airlineRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(airlineRequest, AirlineRequest.class);
        } catch (JsonProcessingException exception) {
            throw new ConversionException("Invalid json data format");
        }
    }

}
