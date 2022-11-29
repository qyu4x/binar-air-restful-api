package com.binarair.binarairrestapi.model.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passenger {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_unique_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "titel_unique_id", referencedColumnName = "id")
    private Titel titel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "age_category_unique_id", referencedColumnName = "id")
    private AgeCategory ageCategory;

    @OneToOne(mappedBy = "passenger")
    private BookingDetail bookingDetail;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String passportNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cityzenship_country_code_id", referencedColumnName = "countryCode")
    private Country cityzenship;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "issuing_country_code_id", referencedColumnName = "countryCode")
    private Country issuingCountry;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
