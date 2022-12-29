package com.binarair.binarairrestapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetail {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_unique_id", referencedColumnName = "id")
    private Booking booking;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_unique_id", referencedColumnName = "id")
    private Schedule schedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passenger_unique_id", referencedColumnName = "id")
    private Passenger passenger;

    private Integer quantity;

    private BigDecimal aircraftPrice;

    private String seatCode;

    private  BigDecimal seatPrice;

    private Integer extraBagage;

    private BigDecimal bagagePrice;

    private String bookingReferenceNumber;

    private boolean checkInStatus;

    private String status;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
