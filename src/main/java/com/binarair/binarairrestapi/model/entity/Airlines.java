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
public class Airlines {

    @Id
    private String id;

    private String name;

    private String logoURL;

    private String description;

    @OneToMany(mappedBy = "airlines",fetch = FetchType.LAZY)
    private List<Aircraft> aircrafts;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


}
