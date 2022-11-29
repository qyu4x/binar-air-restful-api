package com.binarair.binarairrestapi.model.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatScheduleBooking {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unique_schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unique_aircraft_seat_id", referencedColumnName = "id")
    private AircraftSeat aircraftSeat;

    private boolean seatStatus = Boolean.TRUE;

    private LocalDate bookingDate;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
