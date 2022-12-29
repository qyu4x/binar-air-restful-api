package com.binarair.binarairrestapi.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DirectionDetailId implements Serializable {

    @Column(name = "origin_iata_airport_code")
    String originIataAirportCode;

    @Column(name = "dest_iata_airport_code")
    String destIataAirportCode;

}
