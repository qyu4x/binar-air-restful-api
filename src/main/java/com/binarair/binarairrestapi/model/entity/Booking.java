package com.binarair.binarairrestapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_unique_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER)
    private List<BookingDetail> bookingDetails;

    private Integer adult;

    private Integer child;

    private Integer infant;

    private BigDecimal total;

    private String bookingType;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
