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
public class Bagage {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "aircraft_unique_id", referencedColumnName = "id")
    private Aircraft aircraft;

    private BigDecimal bagagePricePer5kg;

    private Integer freeBagageCapacity;

    private Integer freeCabinBagageCapacity;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


}
