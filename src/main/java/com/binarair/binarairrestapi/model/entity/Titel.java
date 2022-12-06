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
public class Titel {

    @Id
    private String id;

    private String titelName;

    private String description;

    @OneToMany(mappedBy = "titel", fetch = FetchType.LAZY)
    private List<Passenger> passenger;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
