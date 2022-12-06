package com.binarair.binarairrestapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Facility {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "aircraft_unique_id", referencedColumnName = "id")
    private Aircraft aircraft;

    private String name;

    private String description;

    private boolean status = Boolean.TRUE;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


}
