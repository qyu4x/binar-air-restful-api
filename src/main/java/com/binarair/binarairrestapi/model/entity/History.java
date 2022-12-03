package com.binarair.binarairrestapi.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class History {
    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User_unique_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Passenger_unique_id" , referencedColumnName = "id")
    private Passenger passenger;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Booking_unique_id" , referencedColumnName = "id")
    private Booking booking;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "City_unique_id", referencedColumnName = "id")
    private City city;

    private java.time.LocalDate departureDate;

    private LocalDate arrivalDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private LocalDateTime updatedAt;

}
