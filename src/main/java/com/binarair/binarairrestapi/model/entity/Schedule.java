package com.binarair.binarairrestapi.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE schedule SET active = FALSE WHERE id = ?")
@Where(clause = "active=true")
public class Schedule {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "origin_iata_airport_code_id", referencedColumnName = "iataAirportCode")
    private Airport originIataAirportCode;

    @ManyToOne
    @JoinColumn(name = "dest_iata_airport_code_id", referencedColumnName = "iataAirportCode")
    private Airport destIataAirportCode;

    @ManyToOne
    @JoinColumn(name = "aircraft_code_id")
    private Aircraft aircraft;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<SeatScheduleBooking> seatScheduleBookings;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private BigDecimal price;

    private boolean active = Boolean.TRUE;

    private Integer stock;

    private Integer sold;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
