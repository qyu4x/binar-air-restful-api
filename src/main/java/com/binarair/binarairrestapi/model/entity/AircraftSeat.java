package com.binarair.binarairrestapi.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftSeat {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "aircraft_unique_id", referencedColumnName = "id")
    private Aircraft aircraft;

    @OneToMany(mappedBy = "aircraftSeat", fetch = FetchType.EAGER)
    List<SeatScheduleBooking> seatScheduleBookings = new ArrayList<>();

    private String seatCode;

    private BigDecimal price;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
