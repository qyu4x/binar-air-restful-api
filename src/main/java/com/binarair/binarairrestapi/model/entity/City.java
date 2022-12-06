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
public class City {

    @Id
    private String codeId;

    private  String name;

    @ManyToOne
    @JoinColumn(name = "country_code_id", referencedColumnName = "countryCode")
    private Country country;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<Airport> airports;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
