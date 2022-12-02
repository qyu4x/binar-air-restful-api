package com.binarair.binarairrestapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airport {

    @Id
    private String iataAirportCode;

    private String name;

    @ManyToOne
    @JoinColumn(name = "city_code_id", referencedColumnName = "codeId")
    private City city;

    @OneToMany(mappedBy = "originIataAirportCode", fetch = FetchType.LAZY)
    private List<Schedule> originIataAirportCodes;

    @OneToMany(mappedBy = "destIataAirportCode", fetch = FetchType.LAZY)
    private List<Schedule> destIataAirportCodes;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


}
