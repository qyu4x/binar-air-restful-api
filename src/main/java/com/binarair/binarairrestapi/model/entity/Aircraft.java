package com.binarair.binarairrestapi.model.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE aircraft SET active = FALSE WHERE id = ?")
@Where(clause = "active=true")
public class Aircraft {

    @Id
    private String id;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @ManyToOne
    @JoinColumn(name = "airlines_code_id", referencedColumnName = "id")
    private Airlines airlines;

    @ManyToOne
    @JoinColumn(name = "aircraft_manufacture_code_id", referencedColumnName = "id")
    private AircraftManufacture aircraftManufacture;

    @ManyToOne
    @JoinColumn(name = "travel_class_code_id", referencedColumnName = "id")
    private TravelClass travelClass;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private List<AircraftSeat> aircraftSeats;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private List<Bagage> bagages;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private List<Facility> facilities;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private List<Benefit> benefits;

    private String model;

    private Integer passangerCapacity;

    private Integer totalUnit;

    private String seatArrangement;

    private Integer distanceBetweenSeats;

    private String seatLengthUnit;

    private boolean active = Boolean.TRUE;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
