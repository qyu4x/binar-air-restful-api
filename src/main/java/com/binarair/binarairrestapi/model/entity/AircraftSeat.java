package com.binarair.binarairrestapi.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE aircraft_seat SET active = FALSE WHERE id = ?")
@Where(clause = "active=true")
public class AircraftSeat {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
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
