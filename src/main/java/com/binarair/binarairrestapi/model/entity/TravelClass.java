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
public class TravelClass {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "travelClass", fetch = FetchType.LAZY)
    private List<Aircraft> aircrafts;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
